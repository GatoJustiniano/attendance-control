package com.example.tallerarquitectura.validation

 fun enterpriseFormValidate(
    name: String,
): List<String> {
    val errors = mutableListOf<String>()
    if (name.isEmpty()) {
        errors.add("El nombre no puede estar vacío")
    }
    return errors
}