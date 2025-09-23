package com.example.tallerarquitectura.validation

import com.example.tallerarquitectura.dto.Product


fun serviceNoteDetailFormValidate(
    price: String,
    quantity: String,
    product: Product?
): List<String> {
    val errors = mutableListOf<String>()
    if(price.trim().isEmpty()) {
        errors.add("Vehiculo es requerido.")
    }
    if (quantity.trim().isEmpty() ){
        errors.add("Taller es requerido.")
    }
    if (product==null){
        errors.add("Producto es requerido.")
    }
    return errors
}