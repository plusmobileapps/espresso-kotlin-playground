package com.plusmobileapps.kotlinopenespresso.extension

import androidx.test.espresso.ViewInteraction
import com.plusmobileapps.kotlinopenespresso.pageobjects.BaseUI

/**
 * Generic lambda with a receiver for navigation functions that are providing a scoped block
 * to the next [com.plusmobileapps.kotlinopenespresso.pageobjects.BaseUI]
 */
typealias ScopedUI <T> = T.() -> Unit

/**
 * Will navigate to the selected screen by clicking on the provided [viewInteraction],
 * then create a new instance of the screen being navigated to asserting that screen and applying the [block]
 */
inline fun <reified T : BaseUI> BaseUI.navigateToWithClick(viewInteraction: ViewInteraction, block: ScopedUI<T>): T {
    viewInteraction.click()
    return T::class.java.newInstance().apply {
        Thread.sleep(5000L) // TODO add idling resource to get rid of this
        assertScreen()
        block()
    }
}
