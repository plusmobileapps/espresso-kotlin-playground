package com.plusmobileapps.kotlinopenespresso.pageobjects

import androidx.test.espresso.ViewInteraction
import com.plusmobileapps.kotlinopenespresso.R
import com.plusmobileapps.kotlinopenespresso.extension.*

class LoginUI : BaseUI {

    override fun assertScreen() {
        onUsername().verifyVisible()
        onPassword().verifyVisible()
        onSignInOrRegisterButton().verifyVisible()
    }

    fun onUsername(): ViewInteraction = R.id.username.toViewInteraction()
    fun onPassword(): ViewInteraction = R.id.password.toViewInteraction()
    fun onSignInOrRegisterButton(): ViewInteraction = R.id.login.toViewInteraction()

    fun enterInfo(username: String, password: String) {
        onUsername().typeText(username)
        onPassword().typeText(password)
    }

    fun submitAndGoToResultUI(block: ScopedUI<ResultUI>): ResultUI =
        navigateToWithClick(onSignInOrRegisterButton(), block)
}