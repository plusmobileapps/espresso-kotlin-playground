package com.plusmobileapps.kotlinopenespresso.pages

import androidx.test.espresso.ViewInteraction
import com.plusmobileapps.kotlinopenespresso.R
import com.plusmobileapps.kotlinopenespresso.extension.*

class LoginPage : BasePage {

    override fun assertScreen() {
        onEmail().verifyVisible()
        onPassword().verifyVisible()
        onSignInOrRegisterButton().verifyVisible()
    }

    fun onEmail(): ViewInteraction = R.id.username.toViewInteraction()
    fun onPassword(): ViewInteraction = R.id.password.toViewInteraction()
    fun onSignInOrRegisterButton(): ViewInteraction = R.id.login.toViewInteraction()
    fun onErrorMessage(): ViewInteraction = R.id.error_message.toViewInteraction()

    fun enterInfo(username: String, password: String) {
        onEmail().typeText(username)
        onPassword().typeText(password)
    }

    fun goToLoggedInPage(block: PageScope<LoggedInPage> = {}): LoggedInPage =
        navigateToPageWithClick(onSignInOrRegisterButton(), block)
}