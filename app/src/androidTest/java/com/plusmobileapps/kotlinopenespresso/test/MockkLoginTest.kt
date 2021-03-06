package com.plusmobileapps.kotlinopenespresso.test

import androidx.test.core.app.launchActivity
import com.plusmobileapps.kotlinopenespresso.R
import com.plusmobileapps.kotlinopenespresso.data.LoginDataSource
import com.plusmobileapps.kotlinopenespresso.data.Result
import com.plusmobileapps.kotlinopenespresso.data.model.LoggedInUser
import com.plusmobileapps.kotlinopenespresso.extension.*
import com.plusmobileapps.kotlinopenespresso.pages.LoginPage
import com.plusmobileapps.kotlinopenespresso.ui.login.LoginActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MockkLoginTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val loginDataSource: LoginDataSource = mockk(relaxed = true)

    private val username = "some-awesome-user-name"
    private val password = "password123"

    @Test
    fun successfulLogin() {
        everyLoginReturns { Result.Success(LoggedInUser("some-id", username)) }

        val scenario = launchActivity<LoginActivity>()

        startOnPage<LoginPage> {
            enterInfo(username = username, password = password)
        }.goToLoggedInPage {
            onBodyText().verifyText("Welcome $username!")
        }

        scenario.close()
    }

    @Test
    fun errorLogin() {
        val expectedError = "Something bad happened"
        everyLoginReturns { Result.Error(IllegalArgumentException(expectedError)) }

        val scenario = launchActivity<LoginActivity>()

        startOnPage<LoginPage> {
            enterInfo(username = username, password = password)
            onSignInOrRegisterButton().click()
            onErrorMessage().verifyText(expectedError).verifyVisible()
        }

        scenario.close()
    }

    @Test
    fun tooShortOfPasswordError() {
        val scenario = launchActivity<LoginActivity>()
        startOnPage<LoginPage> {
            onEmail().typeText("1")
            onPassword().typeText("4")
            onEmail().typeText("2")
            onPassword().verifyTextFieldError(R.string.invalid_password)
        }
        scenario.close()
    }

    private fun everyLoginReturns(result: () -> Result<LoggedInUser>) {
        coEvery { loginDataSource.login(username, password) } returns result()
    }
}