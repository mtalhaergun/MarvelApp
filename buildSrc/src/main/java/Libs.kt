package com.mte.marvelapp

import Versions

object Libs {
    object AndroidX{
        const val core = "androidx.core:core-ktx:" + Versions.coreKtx
        const val appCompat = "androidx.appcompat:appcompat:" + Versions.appCompat
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:" + Versions.constraint
        const val navigationUI = "androidx.navigation:navigation-ui-ktx:" + Versions.navigation
        const val navigation = "androidx.navigation:navigation-fragment-ktx:" + Versions.navigation
        const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:" + Versions.navigation

    }

    object Google{
        const val material = "com.google.android.material:material:" + Versions.material
    }

    object Test{
        const val junit = "junit:junit:" + Versions.junit
        const val testExt = "androidx.test.ext:junit:" + Versions.testExt
        const val espresso = "androidx.test.espresso:espresso-core:" + Versions.espresso
    }
}