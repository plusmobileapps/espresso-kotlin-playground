package com.plusmobileapps.kotlinopenespresso.pages

import androidx.test.espresso.ViewInteraction
import com.plusmobileapps.kotlinopenespresso.R
import com.plusmobileapps.kotlinopenespresso.extension.*

class LoginPage : BasePage {

    override fun assertScreen() {
        onUsername().verifyVisible()
        onPassword().verifyVisible()
        onSignInOrRegisterButton().verifyVisible()
    }

    fun onUsername(): ViewInteraction = R.id.username.toViewInteraction()
    fun onPassword(): ViewInteraction = R.id.password.toViewInteraction()
    fun onSignInOrRegisterButton(): ViewInteraction = R.id.login.toViewInteraction()
    fun onErrorMessage(): ViewInteraction = R.id.error_message.toViewInteraction()

    fun enterInfo(username: String, password: String) {
        onUsername().typeText(username)
        onPassword().typeText(password)
    }

    fun submitAndGoToLoggedInPage(block: PageScope<LoggedInPage>): LoggedInPage =
        navigateToPageWithClick(onSignInOrRegisterButton(), block)
}