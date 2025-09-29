package com.example.tallerarquitectura

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.tallerarquitectura.dblocal.AttendanceControlApp
import com.example.tallerarquitectura.ui.UiProvider
import com.example.tallerarquitectura.ui.theme.TallerArquitecturaTheme
import com.example.tallerarquitectura.controller.ControllerProvider
import com.example.tallerarquitectura.ui.UiAppViewModel
import com.example.tallerarquitectura.view.navigation.NavManager

class MainActivity : ComponentActivity() {
    companion object{
        lateinit var uiProvider: UiProvider

        private lateinit var instanceApp: MainActivity
        val appContext: Context
            get()= instanceApp.applicationContext

        lateinit var navManager: NavManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instanceApp=this
        enableEdgeToEdge()

        setContent {
            val navHostController=rememberNavController()

            val uiAppViewModel = UiAppViewModel()
            navManager= NavManager(navHostController)
            uiProvider= UiProvider(uiAppViewModel)

            val controllerProvider= ControllerProvider()

            AttendanceControlApp(controllerProvider, uiProvider)
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
    TallerArquitecturaTheme {
        Box(
               modifier = Modifier.fillMaxSize()
        ){
            Greeting("Android")
        }
    }
}