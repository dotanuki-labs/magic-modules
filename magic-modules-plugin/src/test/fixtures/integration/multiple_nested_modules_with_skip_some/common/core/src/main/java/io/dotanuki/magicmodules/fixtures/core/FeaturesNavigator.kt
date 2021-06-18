package io.dotanuki.magicmodules.fixtures.core

import android.app.Activity
import android.content.Intent

class FeaturesNavigator(private val origin: Activity) {

    fun navigate(destination: Destination) {
        origin.startActivity(
            Intent().setClassName(origin, destination.classFullName)
        )
    }
}