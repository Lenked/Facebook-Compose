package com.lenked.facebook.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lenked.facebook.compose.ui.theme.Facebook_composeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Facebook_composeTheme {
                TransparentSystemBars()
                val navController = rememberNavController()
                val homeRoute = "home"
                val signinRoute = "signin"
                NavHost(navController = navController, startDestination = homeRoute){
                    composable(homeRoute) {
                        HomeScreen(
                            navigateToSignIn = {
                                navController.navigate(signinRoute) {
                                    popUpTo(homeRoute){
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                    composable(signinRoute){
                        SignInScreen()
                    }
                }
            }
        }
    }

    @Composable
    fun TransparentSystemBars() {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = MaterialTheme.colors.isLight

        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = useDarkIcons
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Facebook_composeTheme {
        HomeScreen(
            navigateToSignIn = {

            }
        )
    }
}