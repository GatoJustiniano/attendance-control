package com.example.tallerarquitectura.validation

fun productFormValidate(
    name: String,
): List<String> {
    val errors = mutableListOf<String>()
    if (name.isEmpty()) {
        errors.add("Nombre es requerido.")
    }
    return errors
}
