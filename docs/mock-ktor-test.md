# Mock Ktor Testing

One of the most popular frameworks used for networking on Android most notably would be [Retrofit](https://square.github.io/retrofit/) and is a great library to use with a simple API. Although when it comes to writing a mock network test in Espresso, the solution to mock the network traffic is running a [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver) or any other JVM server of your choice. Instead of running a separate server, what if the networking library used could swap out the underlying engine executing the Http calls with a mock engine? That is exactly what [Ktor](https://ktor.io/) from Jetbrains can do with the [`MockEngine`](https://ktor.io/docs/http-client-testing.html) and will be used to write a mock network Espresso test. 

## Architecture

Working with the previous architecture, a Ktor `HttpClient` will be injected into the `LoginDataSource` except the test will inject the `MockEngine`. 

![](img/login-mock-network.png)

## Adding Ktor and KotlinxSerialization

```groovy
// build.gradle 

buildscript {
    ext.kotlin_version = "1.6.0"
    ext.ktor_version = "1.6.7"

    dependencies {
        classpath "org.jetbrains.kotlin:kotlin-serialization:$kotlin_version"
    }
}

// app/build.gradle   
plugins {
    id 'org.jetbrains.kotlin.plugin.serialization'
}

dependencies {

    // Ktor
    implementation "io.ktor:ktor-client-android:$ktor_version"
    implementation "io.ktor:ktor-client-serialization-jvm:$ktor_version"
    implementation "io.ktor:ktor-client-json:$ktor_version"
    androidTestImplementation "io.ktor:ktor-client-mock:$ktor_version"

    // Kotlinx Serialization - JSON
    implementation "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0"

}
```

## Create an Http Client

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object HttpEngineModule {
    @Provides
    @Singleton
    fun providesHttpEngine(): HttpClientEngine = Android.create()
}

@Module
@InstallIn(SingletonComponent::class)
object HttpClientModule {

    @Provides
    @Singleton
    fun providesHttpClient(httpClientEngine: HttpClientEngine): HttpClient =
        HttpClient(httpClientEngine) {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }

}
```

## Implement the API Call 

```kotlin
@Serializable
data class LoginRequest(val username: String, val password: String)

@Serializable
data class LoginResponse(val id: String, val displayName: String)

@Serializable
data class LoginError(val errorCode: Int, val message: String)

class LoginDataSource @Inject constructor(private val httpClient: HttpClient) {

    companion object {
        const val LOGIN_URL = "https://plusmobileapps.com/login"
    }

    suspend fun login(email: String, password: String): Result<LoggedInUser> = withContext(Dispatchers.IO) {
        try {
            val response = httpClient.post<LoginResponse>(LOGIN_URL) {
                contentType(ContentType.Application.Json)
                body = LoginRequest(email, password)
            }
            val user = LoggedInUser(response.id, response.displayName)
            Result.Success(user)
        } catch (e: Throwable) {
            val errorMessage = if (e is ClientRequestException) {
                val response = e.response.readText(Charsets.UTF_8)
                val error = Json.decodeFromString<LoginError>(response)
                error.message
            } else {
                "Don't know the error"
            }
            Result.Error(IOException(errorMessage, e))
        }
    }
}
```

## Write a MockNetworkHelper

In order to keep the tests a tidy, it would help to write a `MockNetworkHelper` to abstract away and consolidate the logic for responding to specific endpoints. 

```kotlin
class MockNetworkTestHelper {

    val httpClientEngine: HttpClientEngine = MockEngine { request ->
        when (request.url.fullUrl) {
            LoginDataSource.LOGIN_URL -> getLoginResponse.invoke(this, request)
            else -> error("Unhandled ${request.url.fullUrl}")
        }
    }

    private var getLoginResponse: MockRequestHandler = defaultLoginResponseHandler

    fun everyLoginReturns(response: MockRequestHandler) {
        this.getLoginResponse = response
    }

    fun destroy() {
        getLoginResponse = defaultLoginResponseHandler
    }

    companion object {
        val defaultLoginResponseHandler: MockRequestHandler = {
            respond(
                Json.encodeToString(LoginResponse("default-id", "Buzz Killington")),
                HttpStatusCode.OK,
                headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
    }
}

val Url.hostWithPortIfRequired: String get() = if (port == protocol.defaultPort) host else hostWithPort
val Url.fullUrl: String get() = "${protocol.name}://$hostWithPortIfRequired$fullPath"
```

## Write the test

```kotlin
@UninstallModules(HttpEngineModule::class)
@HiltAndroidTest
class MockNetworkLoginTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val networkHelper = MockNetworkTestHelper()

    @BindValue
    @JvmField
    val mockClient: HttpClientEngine = networkHelper.httpClientEngine

    private val username = "andrew"
    private val password = "password123"
    private val displayName = "Buzz Killington"

    @After
    fun tearDown() {
        networkHelper.destroy()
    }

    @Test
    fun successfulLogin() {
        networkHelper.everyLoginReturns {
            respond(
                Json.encodeToString(LoginResponse("first-id", displayName)),
                HttpStatusCode.OK,
                headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val activityScenario = launchActivity<LoginActivity>()

        startOnPage<LoginPage> {
            enterInfo(username, password)
        }.goToLoggedInPage {
            onWelcomeGreeting().verifyText("Welcome $displayName!")
        }.goToSettings()

        activityScenario.close()
    }

    @Test
    fun errorLogin() {
        val expectedError = LoginError(1, "There was an error")
        networkHelper.everyLoginReturns {
            respond(
                Json.encodeToString(LoginError.serializer(), expectedError),
                HttpStatusCode.BadRequest
            )
        }

        val activityScenario = launchActivity<LoginActivity>()

        startOnPage<LoginPage> {
            enterInfo(username, password)
            onSignInOrRegisterButton().click()
            onErrorMessage().verifyText(expectedError.message).verifyVisible()
        }

        activityScenario.close()
    }
}
```

## Counting Idling Resource 

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val idlingResource: CountingIdlingResource
) : ViewModel() {

    fun login(email: String, password: String) {
        idlingResource.increment()
        viewModelScope.launch {
            // delay is just for local development to ensure espresso waits for job to finish
            delay(2_000)

            // can be launched in a separate asynchronous job
            val result = loginRepository.login(email, password)

            when (result) {
                is Result.Error -> LoginResult(errorString = result.exception.message)
                is Result.Success -> LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
            }.let { _loginResult.value = it }

            idlingResource.decrement()
        }
    }
}
```

## Resources 

* [Add ktor, kotlinx.serialization, and mock network test commit](https://github.com/plusmobileapps/espresso-kotlin-playground/commit/8e5766bd0fd175ffadfa64d046b386ed04562d3a)
* [Add CI/CD workflow commit](https://github.com/plusmobileapps/espresso-kotlin-playground/commit/234a8950325a315a287a4defb3b18e646a81330f)
* [Add counting idling resource to fix flaky test commit](https://github.com/plusmobileapps/espresso-kotlin-playground/commit/9a2747eb768f2a70640ef0f10d35ce36fccec71c)
* [`6-mock-ktor-network-test` branch](https://github.com/plusmobileapps/espresso-kotlin-playground/tree/6-mock-ktor-network-test)
* [Coroutine idling resource workaround](https://github.com/Kotlin/kotlinx.coroutines/issues/242#issuecomment-561503344) and [commit](https://github.com/plusmobileapps/espresso-kotlin-playground/commit/b539a4bcc0f25455fbcc267d4b12d02635475c65)