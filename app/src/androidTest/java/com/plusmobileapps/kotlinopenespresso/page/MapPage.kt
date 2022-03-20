package com.plusmobileapps.kotlinopenespresso.page

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.plusmobileapps.kotlinopenespresso.R
import com.plusmobileapps.kotlinopenespresso.extensions.verifyText
import org.hamcrest.Description
import org.hamcrest.Matcher

class MapPage : BasePage() {
    override fun assertScreen() {

    }

    fun onMarker(title: String): UiObject {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        return device.findObject(UiSelector().descriptionContains(title))
    }

    fun onCloseBottomSheetButton(): ViewInteraction = R.id.imageButton.toViewInteraction()
    fun onMapMarkerBottomSheetText(): ViewInteraction = R.id.map_marker_detail_textview.toViewInteraction()
    fun onBottomSheet(): ViewInteraction = R.id.standard_bottom_sheet.toViewInteraction()
    fun verifyBottomSheetState(expected: Int) {
        onBottomSheet().check(matches(hasBottomSheetBehaviorState(expected)))
    }
}

fun hasBottomSheetBehaviorState(expectedState: Int): Matcher<in View>? {
    return object : BoundedMatcher<View, View>(View::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has BottomSheetBehavior state $expectedState")
        }

        override fun matchesSafely(view: View): Boolean {
            val bottomSheetBehavior = BottomSheetBehavior.from(view)
            return expectedState == bottomSheetBehavior.state
        }
    }
}