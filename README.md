# Kotlin Open Espresso App

Espresso testing with [MockK](https://mockk.io/) has its limits when it comes to mocking final classes which can be a problem with Kotlin since all classes are final by default. This is a simple Android application that uses the [Kotlin all open plugin](https://kotlinlang.org/docs/all-open-plugin.html#gradle) to open up final classes so that they can be mocked in Espresso tests. 

## Other Libraries

* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - dependency injection
* [Mockk](https://mockk.io/) - Kotlin mocking library