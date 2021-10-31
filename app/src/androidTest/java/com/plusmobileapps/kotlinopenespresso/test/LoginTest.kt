package com.plusmobileapps.kotlinopenespresso.test

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.plusmobileapps.kotlinopenespresso.page.LoggedInPage
import com.plusmobileapps.kotlinopenespresso.page.LoginPage
import com.plusmobileapps.kotlinopenespresso.ui.login.LoginActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest {
    @Test
    fun successfulLogin() {
        val scenario = ActivityScenario.launch(LoginActivity::class.java)

        LoginPage().apply {
            onEmail().perform(ViewActions.typeText("andrew@test.com"))
            onPassword().perform(ViewActions.typeText("password123"))
            onSignInOrRegisterButton().perform(ViewActions.click())
        }

        LoggedInPage().onWelcomeGreeting().check(matches(withText("Welcome Andrew!")))

        scenario.close()
    }
}