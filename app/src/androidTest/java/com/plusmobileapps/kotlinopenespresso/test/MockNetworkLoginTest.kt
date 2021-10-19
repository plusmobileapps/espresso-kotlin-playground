package com.plusmobileapps.kotlinopenespresso.test

import androidx.test.espresso.IdlingRegistry
import com.plusmobileapps.kotlinopenespresso.data.model.LoginResponse
import com.plusmobileapps.kotlinopenespresso.di.EspressoModule
import com.plusmobileapps.kotlinopenespresso.di.NetworkModule
import com.plusmobileapps.kotlinopenespresso.extension.launchActivity
import com.plusmobileapps.kotlinopenespresso.extension.onUI
import com.plusmobileapps.kotlinopenespresso.extension.verifyText
import com.plusmobileapps.kotlinopenespresso.pageobjects.LoginUI
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

    val _idlingResource = TestCountingIdlingResource()

    private val networkHelper = MockNetworkTestHelper()

    @BindValue
    @JvmField
    val idlingResource: CountingIdlingResource = _idlingResource

    @BindValue
    @JvmField
    val mockClient: HttpClient = networkHelper.client

    private val username = "some-awesome-user-name"
    private val password = "password123"

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(_idlingResource.idlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(_idlingResource.idlingResource)
        networkHelper.destroy()
    }

    @Test
    fun successfulLogin() {
        networkHelper.everyLoginReturns {
            respond(
                Json.encodeToString(LoginResponse("first-id")),
                HttpStatusCode.OK,
                headersOf("Content-Type", ContentType.Application.Json.toString())
            )
        }

        val activityScenario = launchActivity<LoginActivity>()

        onUI<LoginUI>() {
            enterInfo(username, password)
        }.submitAndGoToResultUI {
            onBodyText().verifyText("Welcome $username!")
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

        onUI<LoginUI>() {
            enterInfo(username, password)
        }.submitAndGoToResultUI {
            onBodyText().verifyText(expectedError)
        }

        activityScenario.close()
    }
}