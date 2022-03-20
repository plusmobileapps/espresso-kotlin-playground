package com.plusmobileapps.kotlinopenespresso.test

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.launchActivity
import com.plusmobileapps.kotlinopenespresso.extensions.startOnPage
import com.plusmobileapps.kotlinopenespresso.extensions.verifyVisible
import com.plusmobileapps.kotlinopenespresso.page.MapPage
import com.plusmobileapps.kotlinopenespresso.ui.MapActivity
import com.plusmobileapps.kotlinopenespresso.ui.login.LoginActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MapTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MapActivity>()

    @Test
    fun markerIsInViewOnStart() {
        val scenario = launchActivity<MapActivity>()

        composeTestRule.startOnPage<MapPage>  {
            onMarker(MapActivity.SYDNEY_MARKER_TITLE).verifyVisible()
        }

        scenario.close()
    }
}