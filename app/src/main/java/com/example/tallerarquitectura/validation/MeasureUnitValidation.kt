package com.example.tallerarquitectura.validation

fun measureUniteFormValidate(
    name: String,
    short: String
): List<String> {
    val errors = mutableListOf<String>()
    if (name.isEmpty()) {
        errors.add("El nombre no puede estar vacío")
    }
    if( short.isEmpty()) {
        errors.add("La abreviatura no puede estar vacía")
    }
    return errors
}
