package com.plusmobileapps.kotlinopenespresso.test

import androidx.test.core.app.ActivityScenario
import com.plusmobileapps.kotlinopenespresso.data.LoginDataSource
import com.plusmobileapps.kotlinopenespresso.data.Result
import com.plusmobileapps.kotlinopenespresso.data.model.LoggedInUser
import com.plusmobileapps.kotlinopenespresso.pageobjects.LoginUI
import com.plusmobileapps.kotlinopenespresso.ui.login.LoginActivity
import com.plusmobileapps.kotlinopenespresso.extension.verifyText
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

    private val username = "some-awesome-user-name"
    private val password = "password123"

    @Test
    fun successfulLogin() {
        everyLoginReturns { Result.Success(LoggedInUser("some-id", username)) }

        launchLogin {
            enterInfo(username = username, password = password)
        }.submitAndGoToResultUI {
            onBodyText().verifyText("Welcome $username!")
        }
    }

    @Test
    fun errorLogin() {
        everyLoginReturns { Result.Error(IllegalArgumentException()) }

        launchLogin {
            enterInfo(username = username, password = password)
        }.submitAndGoToResultUI {
            onBodyText().verifyText("Login failed")
        }
    }

    private fun launchLogin(block: LoginUI.() -> Unit = {}): LoginUI {
        ActivityScenario.launch(LoginActivity::class.java)
        return LoginUI().apply(block)
    }

    private fun everyLoginReturns(result: () -> Result<LoggedInUser>) {
        every { loginDataSource.login(username, password) } returns result()
    }
}