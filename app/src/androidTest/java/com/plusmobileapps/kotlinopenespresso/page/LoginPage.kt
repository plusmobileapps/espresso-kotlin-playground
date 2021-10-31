package com.plusmobileapps.kotlinopenespresso.page

import androidx.test.espresso.ViewInteraction
import com.plusmobileapps.kotlinopenespresso.R
import com.plusmobileapps.kotlinopenespresso.extensions.PageScope
import com.plusmobileapps.kotlinopenespresso.extensions.navigateToPageWithClick
import com.plusmobileapps.kotlinopenespresso.extensions.verifyVisible

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

    fun goToLoggedInPage(block: PageScope<LoggedInPage>): LoggedInPage =
        navigateToPageWithClick(onSignInOrRegisterButton(), block)

}