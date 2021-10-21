# Page Object Pattern

The page object pattern is a very popular design pattern when writing automation tests as it helps abstract out the logic of interacting with a page in the UI from the test. In doing so, will help writing tests that are maintainable 

Looking at the following UI, what might be some objects on the Login page object? 

![](img/login-success.gif)

??? answer
    * username 
    * password
    * sign in or register button

## BasePage

```kotlin
interface BasePage {

    fun assertScreen()

}
```

## LoginPage

```kotlin
class LoginPage : BasePage {

    override fun assertScreen() = TODO()

    fun onUsername(): ViewInteraction = TODO()
    fun onPassword(): ViewInteraction = TODO()
    fun onSignInOrRegisterButton(): ViewInteraction = TODO()

}
```

## Resources

[Page object pattern by Martin Fowler](https://martinfowler.com/bliki/PageObject.html)
