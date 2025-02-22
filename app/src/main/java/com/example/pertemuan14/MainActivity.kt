package com.example.pertemuan14

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.pertemuan14.ui.home.pages.HomeView
import com.example.pertemuan14.ui.navigasi.PengelolaHalaman
import com.example.pertemuan14.ui.theme.Pertemuan14Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pertemuan14Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PengelolaHalaman(
                        modifier = Modifier.padding(innerPadding),
                        navController = rememberNavController()
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Pertemuan14Theme {
        Greeting("Android")
    }
}