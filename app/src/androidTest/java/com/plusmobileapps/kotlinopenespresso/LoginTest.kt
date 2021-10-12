package com.plusmobileapps.kotlinopenespresso

import androidx.test.core.app.ActivityScenario
import com.plusmobileapps.kotlinopenespresso.data.LoginDataSource
import com.plusmobileapps.kotlinopenespresso.data.Result
import com.plusmobileapps.kotlinopenespresso.data.model.LoggedInUser
import com.plusmobileapps.kotlinopenespresso.ui.login.LoginActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val loginDataSource: LoginDataSource = mockk(relaxed = true)

    private val userName = "some-awesome-user-name"
    private val password = "password123"

    @Test
    fun successfulLogin() {
        everyLoginReturns { Result.Success(LoggedInUser("some-id", userName)) }

        ActivityScenario.launch(LoginActivity::class.java)

        login {
            onWelcomeGreeting().verifyText("Welcome $userName!")
        }
    }

    @Test
    fun errorLogin() {
        everyLoginReturns { Result.Error(IllegalArgumentException()) }

        ActivityScenario.launch(LoginActivity::class.java)

        login {
            onWelcomeGreeting().verifyText("Login failed")
        }
    }

    private fun login(block: LoginUI.() -> Unit) {
        LoginUI.apply {
            onEmail().typeText(userName)
            onPassword().typeText(password)
            onSignInOrRegisterButton().click()
            block()
        }
    }

    private fun everyLoginReturns(result: () -> Result<LoggedInUser>) {
        every { loginDataSource.login(userName, password) } returns result()
    }
}