package com.example.tallerarquitectura.controller

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.dto.Product
import com.example.tallerarquitectura.model.ProductModel
import com.example.tallerarquitectura.view.View
import com.example.tallerarquitectura.view.navigation.ProductRoute
import com.example.tallerarquitectura.view.screen.InternalErrorScreen
import com.example.tallerarquitectura.view.screen.NotFoundScreen
import com.example.tallerarquitectura.view.screen.product.ProductCreateScreen
import com.example.tallerarquitectura.view.screen.product.ProductEditScreen
import com.example.tallerarquitectura.view.screen.product.ProductScreen

class ProductController(private val productModel: ProductModel, private val view: View):
    ActionListener<Product> {
    fun index(): @Composable () -> Unit {
        try {
            val products = productModel.getAll()
            return view.render {
                ProductScreen(products, this,view.getUiProvider())
            }

        } catch (ex: Exception) {
            Log.d("ProductController.index", ex.message.toString())
            return view.render {
                InternalErrorScreen("Error al obtener los empresas, vuelva a intentarlo mas tarde.",view.getUiProvider())
            }
        }
    }

    fun create(): @Composable () -> Unit {
        return view.render {
            ProductCreateScreen(this,view.getUiProvider())
        }
    }

    override fun store(product: Product){
        try {
            productModel.save(product)
            MainActivity.navManager.navigateCurrentTo(ProductRoute)
        } catch (ex: Exception) {
            Log.d("ProductController.store", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(ProductRoute)
        }
    }

    override fun update(product: Product){
        try {
            productModel.update(product)
            MainActivity.navManager.navigateCurrentTo(ProductRoute)
        } catch (ex: Exception) {
            Log.d("ProductController.update", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(ProductRoute)
        }
    }

    fun edit(id: Long): @Composable () -> Unit {
        try {
            val product = productModel.getById(id)
            if(product==null){
                return view.render {
                    NotFoundScreen("No se encontro la empresa",view.getUiProvider())
                }
            }
            return view.render {
                ProductEditScreen(product, this,view.getUiProvider())
            }
        } catch (ex: Exception) {
            Log.d("ProductController.edit", ex.message.toString())
            return view.render {
                InternalErrorScreen("Error al intentar cargar el product.",view.getUiProvider())
            }
        }
    }

    override fun destroy(id: Long){
        try {
            productModel.delete(id)
            MainActivity.navManager.navigateCurrentTo(ProductRoute)
        } catch (ex: Exception) {
            Log.d("ProductController.destroy", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(ProductRoute)
        }
    }
}