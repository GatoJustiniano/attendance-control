package com.example.tallerarquitectura.view.navigation

import com.example.tallerarquitectura.R

object ModuleApp{
    val register=mutableListOf<Module>(
        Module(
            icon = R.drawable.outline_settings,
            name = "CRUD MATERIAS",
            route = MateriaRoute
        ),
        Module(
            icon = R.drawable.outline_car_repair,
            name = "CRUD GRUPO",
            route = GrupoRoute
        ),
        Module(
            icon = R.drawable.outline_directions_car,
            name = "CRUD HORARIO",
            route = HorarioRoute
        ),
        Module(
            icon = R.drawable.outline_shopping_cart,
            name = "CRUD ALUMNO",
            route = AlumnoRoute
        ),
    )
}

data class Module(
    val icon: Int,
    val name: String,
    val route: Any,
)