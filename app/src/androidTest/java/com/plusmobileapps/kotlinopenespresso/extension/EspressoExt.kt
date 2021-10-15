package com.plusmobileapps.kotlinopenespresso.extension

import android.app.Activity
import androidx.annotation.StringRes
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.plusmobileapps.kotlinopenespresso.R

inline fun <reified T : Activity> launchActivity(): ActivityScenario<T> =
    ActivityScenario.launch(T::class.java)

fun ViewInteraction.typeText(text: String): ViewInteraction = perform(ViewActions.typeText(text))

fun ViewInteraction.click(): ViewInteraction = perform(ViewActions.click())

fun ViewInteraction.verifyText(text: String): ViewInteraction = check(matches(withText(text)))

fun ViewInteraction.verifyVisible(): ViewInteraction = check(matches(isDisplayed()))

fun ViewInteraction.verifyError(@StringRes resId: Int): ViewInteraction =
    check(matches(hasErrorText(getString(resId))))

fun getString(@StringRes resId: Int): String =
    InstrumentationRegistry.getInstrumentation().targetContext.getString(resId)