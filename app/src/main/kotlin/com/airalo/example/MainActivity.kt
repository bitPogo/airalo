package com.airalo.example

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.airalo.example.offer.presentation.navigation.OfferRouter
import com.airalo.example.offer.presentation.navigation.OfferRoutes
import com.airalo.example.offer.presentation.ui.token.Spacing
import com.airalo.example.ui.theme.AiraloTheme
import org.koin.compose.getKoin

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Airalo_Transparent)
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            AiraloTheme {
                Scaffold {
                    val navController = rememberNavController()
                    val koin = getKoin()

                    Box(
                        modifier = Modifier.padding(top = Spacing.m),
                    ) {
                        NavHost(
                            startDestination = OfferRoutes.COUNTRY_OVERVIEW.route,
                            navController = navController,
                        ) {
                            OfferRouter(navController, koin)
                        }
                    }
                }
            }
        }
    }
}
