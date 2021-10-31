package com.plusmobileapps.kotlinopenespresso.extensions

import androidx.annotation.StringRes
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry

/**
 * Type the provided [text] into the given view
 */
fun ViewInteraction.typeText(text: String): ViewInteraction = perform(ViewActions.typeText(text))

/**
 * Perform a click on the given view
 */
fun ViewInteraction.click(): ViewInteraction = perform(ViewActions.click())

/**
 * Verify the text in the view is the provided [text]
 */
fun ViewInteraction.verifyText(text: String): ViewInteraction = check(matches(withText(text)))

/**
 * Verify that the view is visible to the user
 */
fun ViewInteraction.verifyVisible(): ViewInteraction = check(matches(isDisplayed()))

/**
 * Verify an error exists on a text field with the provided [resId]
 */
fun ViewInteraction.verifyTextFieldError(@StringRes resId: Int): ViewInteraction =
    check(matches(hasErrorText(getString(resId))))

/**
 * Helper function to retrieve string resources from the application using the target context
 */
fun getString(@StringRes resId: Int): String =
    InstrumentationRegistry.getInstrumentation().targetContext.getString(resId)