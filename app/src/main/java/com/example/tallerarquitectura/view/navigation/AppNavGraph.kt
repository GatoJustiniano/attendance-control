package com.example.tallerarquitectura.view.navigation


import androidx.compose.runtime.Composable

import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.tallerarquitectura.controller.ControllerProvider


@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: Any,
    controllerProvider: ControllerProvider
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        composable<MateriaRoute>{
            controllerProvider.materiaController.index()()
        }
        composable<MateriaCreateRoute> {
            controllerProvider.materiaController.create()()
        }
        composable <MateriaEditRoute>{
            val materiaID=it.toRoute<MateriaEditRoute>().id
            controllerProvider.materiaController.edit(materiaID)()
        }

        composable<GrupoRoute>{
            controllerProvider.grupoController.index()()
        }
        composable<GrupoCreateRoute> {
            controllerProvider.grupoController.create()()
        }
        composable <GrupoEditRoute>{
            val materiaID=it.toRoute<GrupoEditRoute>().id
            controllerProvider.grupoController.edit(materiaID)()
        }

        composable<HorarioRoute>{
            controllerProvider.horarioController.index()()
        }
        composable<HorarioCreateRoute> {
            controllerProvider.horarioController.create()()
        }
        composable <HorarioEditRoute>{
            val horarioID=it.toRoute<HorarioEditRoute>().id
            controllerProvider.horarioController.edit(horarioID)()
        }

        composable<AlumnoRoute>{
            controllerProvider.alumnoController.index()()
        }
        composable<AlumnoCreateRoute> {
            controllerProvider.alumnoController.create()()
        }
        composable <AlumnoEditRoute>{
            val horarioID=it.toRoute<HorarioEditRoute>().id
            controllerProvider.alumnoController.edit(horarioID)()
        }

        composable<ClaseRoute> {
            controllerProvider.claseController.index()()
        }
        composable <ClaseCreateRoute>{
            controllerProvider.claseController.create()()
        }
        composable <ClaseEditRoute>{
            val clase_id=it.toRoute<ClaseEditRoute>().id
            controllerProvider.claseController.edit(clase_id)()
        }
        composable <ClaseShowRoute>{
            val clase_id=it.toRoute<ClaseShowRoute>().id
            controllerProvider.claseController.show(clase_id)()
        }
        composable <DetalleClaseCreateRoute>{
            val clase_id=it.toRoute<DetalleClaseCreateRoute>().id
            controllerProvider.claseController.createDetail(clase_id)()
        }
        composable <DetalleClaseEditRoute>{
            val clase_id=it.toRoute<DetalleClaseEditRoute>().clase_id
            val alumno_id=it.toRoute<DetalleClaseEditRoute>().alumno_id
            controllerProvider.claseController.editDetail(clase_id,alumno_id)()
        }

    }
}


@Composable
@Preview(showBackground = true)
private fun ShowInQueueNavGraph(){

    AppNavGraph(
        navController = rememberNavController(),
        startDestination = MateriaRoute,
        controllerProvider = ControllerProvider()
    )
}