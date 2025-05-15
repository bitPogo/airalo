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
import androidx.compose.ui.unit.dp
import com.airalo.example.offer.domain.entity.Id
import com.airalo.example.offer.presentation.ui.command.LoadOfferPackageForCountryCommand
import com.airalo.example.offer.presentation.ui.screen.OfferPackageScreen
import com.airalo.example.offer.presentation.ui.token.Color
import com.airalo.example.offer.presentation.viewmodel.OfferViewModel
import com.airalo.example.ui.theme.AiraloTheme
import org.koin.androidx.compose.koinViewModel
import androidx.core.view.WindowCompat
import com.airalo.example.offer.presentation.ui.token.Spacing

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
                    Box(
                        modifier = Modifier.padding(top = Spacing.m)
                    ) {
                        val overViewModel: OfferViewModel = koinViewModel()
                        overViewModel(LoadOfferPackageForCountryCommand(Id(195)))
                        OfferPackageScreen(overViewModel) { }
                        //CountryOfferOverview(overViewModel, overViewModel)
                    }
                }
            }
        }
    }
}
