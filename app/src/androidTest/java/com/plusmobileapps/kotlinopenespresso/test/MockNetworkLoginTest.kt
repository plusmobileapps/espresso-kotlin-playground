package com.plusmobileapps.kotlinopenespresso.test

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.espresso.IdlingRegistry
import com.plusmobileapps.model.LoginResponse
import com.plusmobileapps.kotlinopenespresso.di.EspressoModule
import com.plusmobileapps.kotlinopenespresso.di.NetworkModule
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
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UninstallModules(NetworkModule::class, EspressoModule::class)
@HiltAndroidTest
class MockNetworkLoginTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    private val networkHelper = MockNetworkTestHelper()
    private val _idlingResource = TestCountingIdlingResource()

    @BindValue
    @JvmField
    val mockClient: HttpClient = networkHelper.client

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
        networkHelper.reset()
    }

    @Test
    fun successfulLogin() {
        networkHelper.everyLoginReturns {
            respond(
                Json.encodeToString(LoginResponse("first-id", displayName)),
                HttpStatusCode.OK,
                headersOf("Content-Type", ContentType.Application.Json.toString())
            )
        }

        composeTestRule.startOnPage<LoginPage> {
            enterInfo(username, password)
        }.goToLoggedInPage {
            onWelcomeGreeting().verifyText("Welcome $displayName!")
        }

    }

    @Test
    fun errorLogin() {
        val expectedError = "some error happened when logging with the mock"
        networkHelper.everyLoginReturns {
            respond(
                expectedError,
                HttpStatusCode.BadRequest
            )
        }

        composeTestRule.startOnPage<LoginPage> {
            enterInfo(username, password)
            onSignInOrRegisterButton().performClick()
            onErrorMessage().verifyText(expectedError).verifyVisible()
        }
    }
}