# Automating Google Maps 

## Setup UIAutomator

```groovy
androidTestImplementation "androidx.test:runner:1.4.0"
androidTestImplementation "androidx.test:rules:1.4.0"
androidTestImplementation "androidx.test.uiautomator:uiautomator:2.2.0"
```

## Extension Functions

```kotlin
import androidx.test.uiautomator.UiObject
import org.junit.Assert.assertTrue

fun UiObject.performClick(): UiObject {
    assertTrue("Clicking UiObject", click())
    return this
}

fun UiObject.verifyVisible(timeout: Long = 0): UiObject {
    assertTrue("View is Visible", waitForExists(timeout))
    return this
}
```

## Create Map Page Object

```kotlin
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
```

### Bottom Sheet Behavior

```kotlin
fun getFriendlyBottomSheetBehaviorStateDescription(state: Int): String = when (state) {
    BottomSheetBehavior.STATE_DRAGGING -> "dragging"
    BottomSheetBehavior.STATE_SETTLING -> "settling"
    BottomSheetBehavior.STATE_EXPANDED -> "expanded"
    BottomSheetBehavior.STATE_COLLAPSED -> "collapsed"
    BottomSheetBehavior.STATE_HIDDEN -> "hidden"
    BottomSheetBehavior.STATE_HALF_EXPANDED -> "half-expanded"
    else -> "unknown but the value was $state"
}

fun hasBottomSheetBehaviorState(expectedState: Int): Matcher<in View>? {
    return object : BoundedMatcher<View, View>(View::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has BottomSheetBehavior state: ${getFriendlyBottomSheetBehaviorStateDescription(expectedState)}")
        }

        override fun matchesSafely(view: View): Boolean {
            val bottomSheetBehavior = BottomSheetBehavior.from(view)
            return expectedState == bottomSheetBehavior.state
        }
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
```

```kotlin
    private fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Log.d("andrew", "New state: $newState")
                if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    countingIdlingResource.increment()
                } else {
                    countingIdlingResource.decrement()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBinding.imageButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }
```

## Map Test

```kotlin
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
```

## Resources 

* [StackOverflow answer](https://stackoverflow.com/questions/29924564/using-espresso-to-unit-test-google-maps) - how to test google maps 
* [Source code](https://github.com/plusmobileapps/espresso-kotlin-playground/compare/8-jetpack-compose...9-maps)