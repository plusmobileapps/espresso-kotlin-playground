package com.plusmobileapps.kotlinopenespresso.page

import androidx.test.espresso.ViewInteraction
import com.plusmobileapps.kotlinopenespresso.R
import com.plusmobileapps.kotlinopenespresso.extensions.verifyVisible

class LoggedInPage : BasePage {

    override fun assertScreen() {
        onWelcomeGreeting().verifyVisible()
    }

    fun onWelcomeGreeting(): ViewInteraction = R.id.logged_in_greeting.toViewInteraction()
    fun onProfileImage(): ViewInteraction = R.id.user_profile_image.toViewInteraction()

}