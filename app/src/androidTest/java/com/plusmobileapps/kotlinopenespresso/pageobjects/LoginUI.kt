package com.plusmobileapps.kotlinopenespresso.pageobjects

import androidx.test.espresso.ViewInteraction
import com.plusmobileapps.kotlinopenespresso.R
import com.plusmobileapps.kotlinopenespresso.click
import com.plusmobileapps.kotlinopenespresso.toViewInteraction

class LoginUI {

    fun onEmail(): ViewInteraction = R.id.username.toViewInteraction()
    fun onPassword(): ViewInteraction = R.id.password.toViewInteraction()
    fun onSignInOrRegisterButton(): ViewInteraction = R.id.login.toViewInteraction()

    fun submitAndGoToResultUI(block: ResultUI.() -> Unit): ResultUI {
        onSignInOrRegisterButton().click()
        return ResultUI().apply(block)
    }

}