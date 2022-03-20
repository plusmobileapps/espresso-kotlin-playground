package com.plusmobileapps.kotlinopenespresso.extensions

import androidx.test.uiautomator.UiObject

fun UiObject.performClick(): UiObject {
    click()
    return this
}