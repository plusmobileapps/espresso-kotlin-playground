package com.plusmobileapps.kotlinopenespresso.extensions

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.performClick
import androidx.test.espresso.ViewInteraction
import com.plusmobileapps.kotlinopenespresso.page.BasePage

/**
 * Generic lambda with a receiver for navigation functions that are providing a scoped block
 * to the next [com.plusmobileapps.kotlinopenespresso.pages.BasePage]
 */
typealias PageScope <T> = T.() -> Unit

/**
 * Will navigate to the selected screen by clicking on the provided [viewInteraction],
 * then create a new instance of the screen being navigated to asserting that screen and applying the [block]
 */
inline fun <reified T : BasePage> BasePage.navigateToPageWithClick(
    semanticsNodeInteraction: SemanticsNodeInteraction,
    block: PageScope<T>
): T {
    semanticsNodeInteraction.performClick()
    return T::class.java.newInstance().apply {
        this.composeTestRule = this@navigateToPageWithClick.composeTestRule
        assertScreen()
        block()
    }
}

/**
 * Will navigate to the selected screen by clicking on the provided [viewInteraction],
 * then create a new instance of the screen being navigated to asserting that screen and applying the [block]
 */
inline fun <reified T : BasePage> BasePage.navigateToPageWithClick(
    viewInteraction: ViewInteraction,
    block: PageScope<T>
): T {
    viewInteraction.click()
    return T::class.java.newInstance().apply {
        this.composeTestRule = this@navigateToPageWithClick.composeTestRule
        assertScreen()
        block()
    }
}

/**
 * Use at the start of a test to create a [PageScope] of the object and assert the screen
 */
inline fun <reified T : BasePage> ComposeTestRule.startOnPage(block: PageScope<T> = {}): T =
    T::class.java.newInstance().apply {
        this.composeTestRule = this@startOnPage
        assertScreen()
        block()
    }