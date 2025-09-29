package com.example.tallerarquitectura.controller

import android.content.Context
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.model.HorarioModel
import com.example.tallerarquitectura.model.GrupoModel
import com.example.tallerarquitectura.model.AlumnoModel
import com.example.tallerarquitectura.model.MateriaModel
import com.example.tallerarquitectura.model.ClaseModel
import com.example.tallerarquitectura.view.View

class ControllerProvider(
    private val context: Context = MainActivity.Companion.appContext
) {

    val grupoController = GrupoController(
        grupoModel = GrupoModel(),
        view = View()
    )
    val horarioController = HorarioController(
        horarioModel = HorarioModel(),
        view = View()
    )
    val alumnoController= AlumnoController(
        alumnoModel = AlumnoModel(),
        view = View()
    )
    val claseController= ClaseController(
        claseModel = ClaseModel(),
        materiaModel = MateriaModel(),
        horarioModel = HorarioModel(),
        grupoModel = GrupoModel(),
        view = View(),
        alumnoModel = AlumnoModel(),
    )
}