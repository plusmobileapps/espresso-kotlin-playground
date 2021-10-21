package com.plusmobileapps.kotlinopenespresso.util

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import java.io.IOException

object KeyboardUtil {
    val isKeyboardShown: Boolean
        get() {
            val checkKeyboardCmd = "dumpsys input_method | grep mInputShown"

            try {
                return UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                    .executeShellCommand(checkKeyboardCmd).contains("mInputShown=true")
            } catch (e: IOException) {
                throw RuntimeException("Keyboard check failed", e)
            }
        }

    fun verifyKeyboardShowing() = assertTrue(isKeyboardShown)

    fun verifyKeyboardGone() = assertFalse(isKeyboardShown)
}