package com.invesproject.web.ui

import androidx.compose.runtime.Composable
import com.invesproject.web.ui.screens.auth.LoginScreen
import com.invesproject.web.ui.screens.auth.SignUpScreen
import com.invesproject.web.ui.screens.home.HomeScreen
import com.invesproject.web.ui.theme.InvesProjectTheme
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.dom.Div

@Composable
fun App() {
    Style(InvesProjectTheme)
    
    Div {
        // TODO: Add navigation logic here
        // For now, let's show the login screen
        LoginScreen()
    }
} 