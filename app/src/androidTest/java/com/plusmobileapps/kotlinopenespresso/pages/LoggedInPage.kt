package com.plusmobileapps.kotlinopenespresso.pages

import androidx.test.espresso.ViewInteraction
import com.plusmobileapps.kotlinopenespresso.R
import com.plusmobileapps.kotlinopenespresso.extension.verifyVisible
import com.plusmobileapps.kotlinopenespresso.util.KeyboardUtil

class LoggedInPage : BasePage {

    override fun assertScreen() {
        onBodyText().verifyVisible()
        KeyboardUtil.verifyKeyboardGone()
    }

    fun onBodyText(): ViewInteraction = R.id.logged_in_greeting.toViewInteraction()

    fun onProfileImage(): ViewInteraction = R.id.user_profile_image.toViewInteraction()

}