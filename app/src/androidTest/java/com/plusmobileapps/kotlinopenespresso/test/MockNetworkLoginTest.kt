package com.plusmobileapps.kotlinopenespresso.test

import androidx.test.core.app.launchActivity
import androidx.test.espresso.IdlingRegistry
import com.plusmobileapps.kotlinopenespresso.data.model.LoginError
import com.plusmobileapps.kotlinopenespresso.data.model.LoginResponse
import com.plusmobileapps.kotlinopenespresso.di.EspressoModule
import com.plusmobileapps.kotlinopenespresso.di.HttpEngineModule
import com.plusmobileapps.kotlinopenespresso.extensions.click
import com.plusmobileapps.kotlinopenespresso.extensions.startOnPage
import com.plusmobileapps.kotlinopenespresso.extensions.verifyText
import com.plusmobileapps.kotlinopenespresso.extensions.verifyVisible
import com.plusmobileapps.kotlinopenespresso.page.LoginPage
import com.plusmobileapps.kotlinopenespresso.ui.login.LoginActivity
import com.plusmobileapps.kotlinopenespresso.util.CountingIdlingResource
import com.plusmobileapps.kotlinopenespresso.util.MockNetworkTestHelper
import com.plusmobileapps.kotlinopenespresso.util.TestCountingIdlingResource
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.ktor.client.engine.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UninstallModules(HttpEngineModule::class, EspressoModule::class)
@HiltAndroidTest
class MockNetworkLoginTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val networkHelper = MockNetworkTestHelper()
    private val _idlingResource = TestCountingIdlingResource()

    @BindValue
    @JvmField
    val mockClient: HttpClientEngine = networkHelper.httpClientEngine

    @BindValue
    @JvmField
    val idlingResource: CountingIdlingResource = _idlingResource

    private val username = "andrew"
    private val password = "password123"
    private val displayName = "Buzz Killington"

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(_idlingResource.instance)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(_idlingResource.instance)
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