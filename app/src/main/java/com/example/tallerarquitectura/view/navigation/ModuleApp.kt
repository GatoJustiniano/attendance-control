package com.example.tallerarquitectura.view.navigation

import com.example.tallerarquitectura.R

object ModuleApp{
    val register=mutableListOf<Module>(
        Module(
            icon = R.drawable.outline_inventory,
            name = "Unidad de medidas",
            route = MeasureUnitRoute
        ),
        Module(
            icon = R.drawable.outline_settings,
            name = "Tipo de servicios",
            route = ServiceRoute
        ),
        Module(
            icon = R.drawable.outline_car_repair,
            name = "Talleres",
            route = EnterpriseRoute
        ),
        Module(
            icon = R.drawable.outline_directions_car,
            name = "Vehiculos",
            route = CarRoute
        ),
        Module(
            icon = R.drawable.outline_shopping_cart,
            name = "Productos",
            route = ProductRoute
        ),
    )
}

data class Module(
    val icon: Int,
    val name: String,
    val route: Any,
)