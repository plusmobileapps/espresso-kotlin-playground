package com.plusmobileapps.kotlinopenespresso.extension

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*

@IdRes
fun Int.toViewInteraction(): ViewInteraction = onView(withId(this))

fun ViewInteraction.typeText(text: String): ViewInteraction = perform(ViewActions.typeText(text))

fun ViewInteraction.click(): ViewInteraction = perform(ViewActions.click())

fun ViewInteraction.verifyText(text: String) = check(matches(withText(text)))

fun ViewInteraction.verifyVisible() = check(matches(isDisplayed()))