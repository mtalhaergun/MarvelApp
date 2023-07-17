package com.mte.marvelapp.utils.extensions

import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.safeNavigate(action : NavDirections){
    currentDestination?.getAction(action.actionId)?.let {
        if(it.destinationId != currentDestination?.id){
            navigate(action)
        }
    }
}