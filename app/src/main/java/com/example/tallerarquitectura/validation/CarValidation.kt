package com.example.tallerarquitectura.validation

 fun carFormValidate(
    plate: String,
    model: String,
    cylinder: String,
    year: String,
    mark: String,
): List<String> {
    val errors = mutableListOf<String>()
    if (plate.trim().isEmpty()) {
        errors.add("Placa es requerido.")
    }
     if (model.trim().isEmpty()) {
         errors.add("Modelo es requerido.")
     }
     if (cylinder.trim().isEmpty()) {
         errors.add("Cilindraje es requerido.")
     }
     if (year.trim().isEmpty()) {
         errors.add("Año es requerido.")
     }
     if(year.trim().isNotEmpty()){
            val yearInt = year.toIntOrNull()
            if (yearInt == null || yearInt < 1900 || yearInt > 2100) {
                errors.add("Año no es válido.")
            }
     }
     if (mark.trim().isEmpty()) {
         errors.add("Marca es requerido.")
     }
    return errors
}