package com.plusmobileapps.kotlinopenespresso.test

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.launchActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.plusmobileapps.kotlinopenespresso.extensions.*
import com.plusmobileapps.kotlinopenespresso.page.MapPage
import com.plusmobileapps.kotlinopenespresso.ui.map.MapActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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

    @Test
    fun clickMapMarkerOpensBottomSheet() {
        val scenario = launchActivity<MapActivity>()

        composeTestRule.startOnPage<MapPage>  {
            verifyBottomSheetState(BottomSheetBehavior.STATE_HIDDEN)
            onMarker(MapActivity.SYDNEY_MARKER_TITLE).performClick()
            Thread.sleep(1000L)
            verifyBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED)
            onMapMarkerBottomSheetText().verifyText(MapActivity.SYDNEY_MARKER_TITLE)
            onCloseBottomSheetButton().click()
            Thread.sleep(1000L)
            verifyBottomSheetState(BottomSheetBehavior.STATE_HIDDEN)
        }

        scenario.close()
    }
}