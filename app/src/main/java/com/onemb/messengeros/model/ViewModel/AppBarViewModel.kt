package com.onemb.messengeros.model.ViewModel

import android.app.Activity
import android.app.Application
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.AndroidViewModel

class AppBarViewModel(application: Application) : AndroidViewModel(application){


    fun searchScreenAppBarChange(darkTheme: Boolean, view: View) {
        val window = (view.context as Activity).window
        window.statusBarColor = if(darkTheme) Color(0xFF2B201D).toArgb() else Color.White.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
    }

    fun mainScreenAppBarChange(darkTheme: Boolean, view: View) {
        val window = (view.context as Activity).window
        window.statusBarColor = if(darkTheme) Color(0xFF2B201D).toArgb() else Color(0xFFF9F2F5).toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
    }

}