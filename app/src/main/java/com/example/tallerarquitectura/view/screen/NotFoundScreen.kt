package com.example.tallerarquitectura.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.R
import com.example.tallerarquitectura.ui.UiProvider
import com.example.tallerarquitectura.ui.UiAppViewModel

@Composable
fun  NotFoundScreen(
    message: String,
    uiProvider: UiProvider
) {
    Scaffold {
        Box(modifier = Modifier
            .padding(it).fillMaxSize(),
            contentAlignment = Alignment.Center

        ){
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(R.drawable.undraw_no_data_ig65),
                    contentDescription = "not found",
                    modifier = Modifier
                        .width(150.dp)
                )
                Text(message, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.error)
                TextButton(
                    onClick = {
                        MainActivity.navManager.popBackStack()
                    },

                    ) {
                    Text("Regresar")
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShowNotFoundScreen() {
    val context= LocalContext.current
    val navHostController=rememberNavController()
    val uiAppViewModel= UiAppViewModel()
    val uiProvider= UiProvider(uiAppViewModel)
    NotFoundScreen(
        message = "Página no encontrada.",
        uiProvider
    )
}