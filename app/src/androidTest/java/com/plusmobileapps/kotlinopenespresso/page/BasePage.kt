package com.plusmobileapps.kotlinopenespresso.page

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withId

abstract class BasePage {

    lateinit var composeTestRule: ComposeTestRule

    abstract fun assertScreen()

    fun Int.toViewInteraction(): ViewInteraction = onView(withId(this))

}