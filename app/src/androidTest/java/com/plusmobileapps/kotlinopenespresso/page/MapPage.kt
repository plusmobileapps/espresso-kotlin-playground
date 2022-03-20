package com.plusmobileapps.kotlinopenespresso.page

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector

class MapPage : BasePage() {
    override fun assertScreen() {

    }

    fun onMarker(title: String): UiObject {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        return device.findObject(UiSelector().descriptionContains(title))
    }
}