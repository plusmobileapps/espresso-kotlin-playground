package com.plusmobileapps.kotlinopenespresso.page

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.espresso.ViewInteraction
import androidx.test.platform.app.InstrumentationRegistry
import com.plusmobileapps.kotlinopenespresso.R
import com.plusmobileapps.kotlinopenespresso.extensions.PageScope
import com.plusmobileapps.kotlinopenespresso.extensions.navigateToPageWithClick
import com.plusmobileapps.kotlinopenespresso.extensions.typeText
import com.plusmobileapps.kotlinopenespresso.extensions.verifyVisible

class LoginPage : BasePage() {

    override fun assertScreen() {
        onEmail().verifyVisible()
        onPassword().verifyVisible()
        onSignInOrRegisterButton().assertIsDisplayed()
    }

    fun enterInfo(email: String, password: String) {
        onEmail().typeText(email)
        onPassword().typeText(password)
    }

    fun onEmail(): ViewInteraction = R.id.username.toViewInteraction()
    fun onPassword(): ViewInteraction = R.id.password.toViewInteraction()
    fun onSignInOrRegisterButton(): SemanticsNodeInteraction {
        val text =
            InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.action_sign_in)
        return composeTestRule.onNodeWithText(text = text)
    }

    fun onErrorMessage(): ViewInteraction = R.id.error_message.toViewInteraction()

    fun goToLoggedInPage(block: PageScope<LoggedInPage>): LoggedInPage =
        navigateToPageWithClick(onSignInOrRegisterButton(), block)


}