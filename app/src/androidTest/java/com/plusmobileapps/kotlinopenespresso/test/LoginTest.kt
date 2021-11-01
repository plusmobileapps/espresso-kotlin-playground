package com.plusmobileapps.kotlinopenespresso.test

import androidx.test.core.app.launchActivity
import com.plusmobileapps.kotlinopenespresso.R
import com.plusmobileapps.kotlinopenespresso.extensions.startOnPage
import com.plusmobileapps.kotlinopenespresso.extensions.typeText
import com.plusmobileapps.kotlinopenespresso.extensions.verifyText
import com.plusmobileapps.kotlinopenespresso.extensions.verifyTextFieldError
import com.plusmobileapps.kotlinopenespresso.page.LoginPage
import com.plusmobileapps.kotlinopenespresso.ui.login.LoginActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

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