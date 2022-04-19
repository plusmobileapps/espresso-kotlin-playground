package com.plusmobileapps.kotlinopenespresso.test

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.IdlingRegistry
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.plusmobileapps.kotlinopenespresso.di.EspressoModule
import com.plusmobileapps.kotlinopenespresso.extensions.*
import com.plusmobileapps.kotlinopenespresso.page.MapPage
import com.plusmobileapps.kotlinopenespresso.ui.map.MapActivity
import com.plusmobileapps.kotlinopenespresso.util.CountingIdlingResource
import com.plusmobileapps.kotlinopenespresso.util.TestCountingIdlingResource
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UninstallModules(EspressoModule::class)
@HiltAndroidTest
class MapTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MapActivity>()

    private val _idlingResource = TestCountingIdlingResource()

    @BindValue
    @JvmField
    val idlingResource: CountingIdlingResource = _idlingResource

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(_idlingResource.instance)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(_idlingResource.instance)
    }

    @Test
    fun markerIsInViewOnStart() {
        composeTestRule.startOnPage<MapPage>  {
            onMarker(MapActivity.SYDNEY_MARKER_TITLE).verifyVisible()
        }
    }

    @Test
    fun clickMapMarkerOpensBottomSheet() {
        composeTestRule.startOnPage<MapPage>  {
            verifyBottomSheetState(BottomSheetBehavior.STATE_HIDDEN)
            onMarker(MapActivity.SYDNEY_MARKER_TITLE).performClick()
            verifyBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED)
            onMapMarkerBottomSheetText().verifyText(MapActivity.SYDNEY_MARKER_TITLE)
            onCloseBottomSheetButton().click()
            verifyBottomSheetState(BottomSheetBehavior.STATE_HIDDEN)
        }
    }
}