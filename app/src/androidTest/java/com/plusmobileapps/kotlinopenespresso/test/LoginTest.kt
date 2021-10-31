package com.plusmobileapps.kotlinopenespresso.test

import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.plusmobileapps.kotlinopenespresso.R
import com.plusmobileapps.kotlinopenespresso.extensions.startOnPage
import com.plusmobileapps.kotlinopenespresso.extensions.typeText
import com.plusmobileapps.kotlinopenespresso.extensions.verifyText
import com.plusmobileapps.kotlinopenespresso.extensions.verifyTextFieldError
import com.plusmobileapps.kotlinopenespresso.page.LoginPage
import com.plusmobileapps.kotlinopenespresso.ui.login.LoginActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest {
    @Test
    fun successfulLogin() {
        val scenario = launchActivity<LoginActivity>()

        startOnPage<LoginPage> {
            onEmail().typeText("andrew@test.com")
            onPassword().typeText("password123")
        }.goToLoggedInPage {
            onWelcomeGreeting().verifyText("Welcome Andrew!")
        }.goToSettings()

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
}