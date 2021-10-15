package com.plusmobileapps.kotlinopenespresso.pageobjects

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withId

interface BaseUI {

    fun assertScreen()

    fun Int.toViewInteraction(): ViewInteraction = onView(withId(this))

}