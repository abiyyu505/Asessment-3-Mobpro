package com.abiyyu0003.asessment2mobpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.abiyyu0003.asessment2mobpro.navigation.SetupNavGraph
import com.abiyyu0003.asessment2mobpro.ui.theme.Asessment2MobproTheme
import com.abiyyu0003.asessment2mobpro.util.SettingsDataStore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val dataStore = SettingsDataStore(this)
            val isDarkTheme by dataStore.themeFlow.collectAsState(initial = true)

            Asessment2MobproTheme(
                darkTheme = isDarkTheme
            ) {
                SetupNavGraph()
            }
        }
    }
}