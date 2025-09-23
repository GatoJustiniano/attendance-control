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

        composable<MeasureUnitRoute> {
            controllerProvider.measureUnitController.index()()
        }
        composable<MeasureUnitCreateRoute>{
            controllerProvider.measureUnitController.create()()
        }
        composable<MeasureUnitEditRoute>{
            val measureUnitId=it.toRoute<MeasureUnitEditRoute>().id
            controllerProvider.measureUnitController.edit(measureUnitId)()
        }

        composable<ServiceRoute>{
            controllerProvider.serviceController.index()()
        }
        composable<ServiceCreateRoute> {
            controllerProvider.serviceController.create()()
        }
        composable <ServiceEditRoute>{
            val serviceId=it.toRoute<ServiceEditRoute>().id
            controllerProvider.serviceController.edit(serviceId)()
        }

        composable<EnterpriseRoute>{
            controllerProvider.enterpriseController.index()()
        }
        composable<EnterpriseCreateRoute> {
            controllerProvider.enterpriseController.create()()
        }
        composable <EnterpriseEditRoute>{
            val serviceId=it.toRoute<EnterpriseEditRoute>().id
            controllerProvider.enterpriseController.edit(serviceId)()
        }

        composable<CarRoute>{
            controllerProvider.carController.index()()
        }
        composable<CarCreateRoute> {
            controllerProvider.carController.create()()
        }
        composable <CarEditRoute>{
            val carId=it.toRoute<CarEditRoute>().id
            controllerProvider.carController.edit(carId)()
        }

        composable<ProductRoute>{
            controllerProvider.productController.index()()
        }
        composable<ProductCreateRoute> {
            controllerProvider.productController.create()()
        }
        composable <ProductEditRoute>{
            val carId=it.toRoute<CarEditRoute>().id
            controllerProvider.productController.edit(carId)()
        }

        composable<ServiceNoteRoute> {
            controllerProvider.serviceNoteController.index()()
        }
        composable <ServiceNoteCreateRoute>{
            controllerProvider.serviceNoteController.create()()
        }
        composable <ServiceNoteEditRoute>{
            val serviceNoteId=it.toRoute<ServiceNoteEditRoute>().id
            controllerProvider.serviceNoteController.edit(serviceNoteId)()
        }
        composable <ServiceNoteShowRoute>{
            val serviceNoteId=it.toRoute<ServiceNoteShowRoute>().id
            controllerProvider.serviceNoteController.show(serviceNoteId)()
        }
        composable <ServiceNoteDetailCreateRoute>{
            val serviceNoteId=it.toRoute<ServiceNoteDetailCreateRoute>().id
            controllerProvider.serviceNoteController.createDetail(serviceNoteId)()
        }
        composable <ServiceNoteDetailEditRoute>{
            val serviceNoteId=it.toRoute<ServiceNoteDetailEditRoute>().serviceNoteId
            val productId=it.toRoute<ServiceNoteDetailEditRoute>().productId
            controllerProvider.serviceNoteController.editDetail(serviceNoteId,productId)()
        }

        composable<ReminderNoteCreateRoute> {
            val serviceNoteId=it.toRoute<ReminderNoteCreateRoute>().serviceNoteId
            controllerProvider.reminderNoteController.create(serviceNoteId)()
        }
        composable<ReminderNoteRoute> {
            controllerProvider.reminderNoteController.index()()
        }
        composable<ReminderNoteEditRoute> {
            val serviceNoteId=it.toRoute<ReminderNoteEditRoute>().serviceNoteId
            controllerProvider.reminderNoteController.edit(serviceNoteId)()
        }

    }
}


@Composable
@Preview(showBackground = true)
private fun ShowInQueueNavGraph(){

    AppNavGraph(
        navController = rememberNavController(),
        startDestination = MeasureUnitRoute,
        controllerProvider = ControllerProvider()
    )
}