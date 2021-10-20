package com.plusmobileapps.kotlinopenespresso.pages

import androidx.test.espresso.ViewInteraction
import com.plusmobileapps.kotlinopenespresso.R
import com.plusmobileapps.kotlinopenespresso.extension.verifyVisible

class LoggedInPage : BasePage {

    override fun assertScreen() {
        onBodyText().verifyVisible()
    }

    fun onBodyText(): ViewInteraction = R.id.logged_in_greeting.toViewInteraction()
}