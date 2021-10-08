package com.plusmobileapps.kotlinopenespresso

/**
 * Since mockk cannot mock final classes in espresso tests before android pie,
 * this annotation can be used to make an application open on debug builds
 * using the kotlin open compiler plugin
 */
annotation class OpenForTest