package com.plusmobileapps.kotlinopenespresso.extensions

import androidx.test.uiautomator.UiObject
import org.junit.Assert.assertTrue

fun UiObject.performClick(): UiObject {
    click()
    return this
}

fun UiObject.verifyVisible(timeout: Long = 0): UiObject {
    assertTrue("View is Visible", waitForExists(timeout))
    return this
}