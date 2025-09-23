package com.example.tallerarquitectura.view

import androidx.compose.runtime.Composable
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.ui.UiProvider

class View(private val uiProvider: UiProvider= MainActivity.uiProvider) {

    fun render(content: @Composable ()->Unit): @Composable ()-> Unit {
        return content
    }

    fun getUiProvider(): UiProvider {
        return uiProvider
    }
}