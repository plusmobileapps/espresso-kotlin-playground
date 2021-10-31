package com.plusmobileapps.kotlinopenespresso.page

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withId

interface BasePage {

    fun assertScreen()

    fun Int.toViewInteraction(): ViewInteraction = onView(withId(this))

}