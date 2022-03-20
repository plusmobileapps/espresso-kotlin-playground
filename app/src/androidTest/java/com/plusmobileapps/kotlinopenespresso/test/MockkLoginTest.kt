package com.plusmobileapps.kotlinopenespresso.test

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.core.app.launchActivity
import com.plusmobileapps.kotlinopenespresso.R
import com.plusmobileapps.kotlinopenespresso.data.LoginDataSource
import com.plusmobileapps.kotlinopenespresso.data.Result
import com.plusmobileapps.kotlinopenespresso.data.model.LoggedInUser
import com.plusmobileapps.kotlinopenespresso.extensions.*
import com.plusmobileapps.kotlinopenespresso.page.LoginPage
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

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    @BindValue
    @JvmField
    val loginDataSource: LoginDataSource = mockk(relaxed = true)

    private val email = "andrew@test.com"
    private val password = "password123"
    private val displayName = "Buzz Killington"

    @Test
    fun successfulLogin() {
        everyLoginReturns { Result.Success(LoggedInUser("some-user-id", displayName)) }
        val scenario = launchActivity<LoginActivity>()

        composeTestRule.startOnPage<LoginPage> {
            enterInfo(email, password)
        }.goToLoggedInPage {
            onWelcomeGreeting().verifyText("Welcome $displayName!")
        }.goToSettings()

        scenario.close()

    }

    @Test
    fun errorLogin() {
        val expectedError = "Something bad happened"
        everyLoginReturns { Result.Error(IllegalArgumentException(expectedError)) }
        val scenario = launchActivity<LoginActivity>()

        composeTestRule.startOnPage<LoginPage> {
            enterInfo(email, password);
            onSignInOrRegisterButton().performClick()
            onErrorMessage().verifyText(expectedError).verifyVisible()
        }

        scenario.close()
    }

    @Test
    fun tooShortOfPasswordError() {
        val scenario = launchActivity<LoginActivity>()

        composeTestRule.startOnPage<LoginPage> {
            onEmail().typeText("1")
            onPassword().typeText("4")
            onEmail().typeText("2")
            onPassword().verifyTextFieldError(R.string.invalid_password)
        }

        scenario.close()
    }

    private fun everyLoginReturns(result: () -> Result<LoggedInUser>) {
        coEvery { loginDataSource.login(email, password) } returns result()
    }
}