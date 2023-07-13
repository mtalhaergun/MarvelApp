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

    object Network {
        const val retrofit = "com.squareup.retrofit2:retrofit:" + Versions.retrofit
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:" + Versions.retrofit
        const val moshiConverter = "com.squareup.retrofit2:converter-moshi:" + Versions.retrofit
        const val okhttp = "com.squareup.okhttp3:okhttp:" + Versions.ok_http
        const val interceptor = "com.squareup.okhttp3:logging-interceptor:" + Versions.ok_http
        const val moshi = "com.squareup.moshi:moshi-kotlin:" + Versions.moshi
        const val codegen = "com.squareup.moshi:moshi-kotlin-codegen:" + Versions.moshi
    }
}