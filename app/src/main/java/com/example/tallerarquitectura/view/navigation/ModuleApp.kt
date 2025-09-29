package com.example.tallerarquitectura.view.navigation

import com.example.tallerarquitectura.R

object ModuleApp{
    val register=mutableListOf<Module>(
        Module(
            icon = R.drawable.outline_inventory,
            name = "Materias",
            route = MateriaRoute
        ),
        Module(
            icon = R.drawable.outline_inventory,
            name = "Grupos",
            route = GrupoRoute
        ),
        Module(
            icon = R.drawable.outline_inventory,
            name = "Horarios",
            route = HorarioRoute
        ),
        Module(
            icon = R.drawable.outline_inventory,
            name = "Alumnos",
            route = AlumnoRoute
        ),
    )
}

data class Module(
    val icon: Int,
    val name: String,
    val route: Any,
)