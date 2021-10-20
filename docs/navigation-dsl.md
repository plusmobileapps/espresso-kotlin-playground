# Navigation DSL 

```kotlin
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
        assertScreen()
        block()
    }
}

/**
 * Use at the start of a test to create a scoped block of the object and assert the screen
 */
inline fun <reified T : BaseUI> onUI(block: ScopedUI<T> = {}): T =
    T::class.java.newInstance().apply {
        assertScreen()
        block()
    }
```

```kotlin
class LoginUI : BaseUI {

    fun submitAndGoToResultUI(block: ScopedUI<ResultUI>): ResultUI =
        navigateToWithClick(onSignInOrRegisterButton(), block)
        
}
```