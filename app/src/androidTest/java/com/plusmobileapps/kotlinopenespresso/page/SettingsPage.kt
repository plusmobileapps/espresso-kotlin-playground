package com.plusmobileapps.kotlinopenespresso.page

import androidx.test.espresso.ViewInteraction
import com.plusmobileapps.kotlinopenespresso.R
import com.plusmobileapps.kotlinopenespresso.extensions.verifyVisible

class SettingsPage : BasePage {

    override fun assertScreen() {
        onTitle().verifyVisible()
    }

    fun onTitle(): ViewInteraction = R.id.settings_title.toViewInteraction()
}