package com.plusmobileapps.kotlinopenespresso.pageobjects

import androidx.test.espresso.ViewInteraction
import com.plusmobileapps.kotlinopenespresso.R
import com.plusmobileapps.kotlinopenespresso.extension.toViewInteraction

class ResultUI {
    fun onBodyText(): ViewInteraction = R.id.logged_in_greeting.toViewInteraction()
}