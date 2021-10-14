package com.plusmobileapps.kotlinopenespresso.pageobjects

import androidx.test.espresso.ViewInteraction
import com.plusmobileapps.kotlinopenespresso.R
import com.plusmobileapps.kotlinopenespresso.extension.click
import com.plusmobileapps.kotlinopenespresso.extension.toViewInteraction
import com.plusmobileapps.kotlinopenespresso.extension.typeText

class LoginUI {

    fun onUsername(): ViewInteraction = R.id.username.toViewInteraction()
    fun onPassword(): ViewInteraction = R.id.password.toViewInteraction()
    fun onSignInOrRegisterButton(): ViewInteraction = R.id.login.toViewInteraction()

    fun enterInfo(username: String, password: String) {
        onUsername().typeText(username)
        onPassword().typeText(password)
    }

    fun submitAndGoToResultUI(block: ResultUI.() -> Unit): ResultUI {
        onSignInOrRegisterButton().click()
        return ResultUI().apply(block)
    }

}