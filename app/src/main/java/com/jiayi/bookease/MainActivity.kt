package com.jiayi.bookease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jiayi.bookease.navigation.AppNavigation
import com.jiayi.bookease.ui.theme.BookEaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BookEaseTheme {
                AppNavigation()
            }
        }
    }
}