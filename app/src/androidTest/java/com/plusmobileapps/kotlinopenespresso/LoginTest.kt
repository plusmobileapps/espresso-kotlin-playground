package com.plusmobileapps.kotlinopenespresso

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.plusmobileapps.kotlinopenespresso.ui.login.LoginActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest {

    @Test
    fun successfulLogin() {
        val scenario = ActivityScenario.launch(LoginActivity::class.java)
        val userName = "some-awesome-user-name"

        LoginUI.apply {
            onEmail().typeText(userName)
            onPassword().typeText("password123")
            onSignInOrRegisterButton().click()
            onWelcomeGreeting().verifyText("Welcome $userName!")
        }
    }
}