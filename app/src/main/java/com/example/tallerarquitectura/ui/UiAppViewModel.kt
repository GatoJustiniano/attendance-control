package com.example.tallerarquitectura.ui

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.lifecycle.ViewModel

class UiAppViewModel: ViewModel() {
    private val drawerState= DrawerState(initialValue = DrawerValue.Closed)

    fun getDrawerState(): DrawerState {
        return drawerState
    }
}