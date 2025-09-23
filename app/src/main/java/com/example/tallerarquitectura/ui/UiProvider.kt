package com.example.tallerarquitectura.ui

class UiProvider(
    private val uiAppViewModel: UiAppViewModel
) {
    fun getUiAppViewModel(): UiAppViewModel {
        return uiAppViewModel
    }
}