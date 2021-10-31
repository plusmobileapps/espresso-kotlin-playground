package com.plusmobileapps.kotlinopenespresso.page

import androidx.test.espresso.ViewInteraction
import com.plusmobileapps.kotlinopenespresso.R
import com.plusmobileapps.kotlinopenespresso.extensions.PageScope
import com.plusmobileapps.kotlinopenespresso.extensions.navigateToPageWithClick
import com.plusmobileapps.kotlinopenespresso.extensions.verifyVisible

class LoggedInPage : BasePage {

    override fun assertScreen() {
        onWelcomeGreeting().verifyVisible()
    }

    fun onWelcomeGreeting(): ViewInteraction = R.id.logged_in_greeting.toViewInteraction()
    fun onProfileImage(): ViewInteraction = R.id.user_profile_image.toViewInteraction()
    fun onSettingsButton(): ViewInteraction = R.id.settings_button.toViewInteraction()

    fun goToSettings(block: PageScope<SettingsPage> = {}): SettingsPage =
        navigateToPageWithClick(onSettingsButton(), block)

}