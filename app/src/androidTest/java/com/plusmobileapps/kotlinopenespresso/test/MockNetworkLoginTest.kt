package com.plusmobileapps.kotlinopenespresso.test

import androidx.test.core.app.launchActivity
import com.plusmobileapps.kotlinopenespresso.data.model.LoginResponse
import com.plusmobileapps.kotlinopenespresso.di.NetworkModule
import com.plusmobileapps.kotlinopenespresso.extensions.click
import com.plusmobileapps.kotlinopenespresso.extensions.startOnPage
import com.plusmobileapps.kotlinopenespresso.extensions.verifyText
import com.plusmobileapps.kotlinopenespresso.extensions.verifyVisible
import com.plusmobileapps.kotlinopenespresso.page.LoginPage
import com.plusmobileapps.kotlinopenespresso.ui.login.LoginActivity
import com.plusmobileapps.kotlinopenespresso.util.MockNetworkTestHelper
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
import org.junit.Rule
import org.junit.Test

@UninstallModules(NetworkModule::class)
@HiltAndroidTest
class MockNetworkLoginTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val networkHelper = MockNetworkTestHelper()

    @BindValue
    @JvmField
    val mockClient: HttpClient = networkHelper.client

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
                headersOf("Content-Type", ContentType.Application.Json.toString())
            )
        }

        val activityScenario = launchActivity<LoginActivity>()

        startOnPage<LoginPage> {
            enterInfo(username, password)
        }.goToLoggedInPage {
            onWelcomeGreeting().verifyText("Welcome $displayName!")
        }

        activityScenario.close()
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

        val activityScenario = launchActivity<LoginActivity>()

        startOnPage<LoginPage> {
            enterInfo(username, password)
            onSignInOrRegisterButton().click()
            onErrorMessage().verifyText(expectedError).verifyVisible()
        }

        activityScenario.close()
    }
}