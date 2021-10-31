# Mockk Test

![](img/login-mockk.png)

## Pre Android P mocking

If you are writing an Android app that has a minimum SDK less than Android P and need to mock a final class in a test. The recommended approach on the [Mockk Android](https://mockk.io/ANDROID.html) page is to use [Dexopener](https://github.com/tmurakami/dexopener), however if you look on the project readme you will see: 

!!!quote
    DexOpener will do the following things at runtime:

    1. Remove the final modifier from the classes belonging to the specified package
    2. Create dex files to make the application class loader load those classes

    However, they are not so lightweight. If you would like to save even a little testing time of your Kotlin app, you can introduce [the all-open compiler plugin](https://kotlinlang.org/docs/reference/compiler-plugins.html#all-open-compiler-plugin) instead of DexOpener.

So to keep the mock tests as performant as possible we will use the [Kotlin all open compiler](https://kotlinlang.org/docs/all-open-plugin.html) which will only open up classes marked with an annotation on debug builds. 

## Source

[github commit](https://github.com/plusmobileapps/espresso-kotlin-playground/commit/959b7b62136a498d35b3c72ec0206da93794a931)