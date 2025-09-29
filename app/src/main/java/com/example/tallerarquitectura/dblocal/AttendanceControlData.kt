package com.example.tallerarquitectura.dblocal

import android.provider.BaseColumns

object AttendanceControlData {

    object Materia : BaseColumns {
        const val TABLE_NAME = "Materias"
        const val COLUMN_NAME_ID = "nId"
        const val COLUMN_NAME_NAME = "sName"
    }

    object Grupo : BaseColumns {
        const val TABLE_NAME = "Grupos"
        const val COLUMN_NAME_ID = "nId"
        const val COLUMN_NAME_NAME = "sName"
    }

    object Horario : BaseColumns {
        const val TABLE_NAME = "Horarios"
        const val COLUMN_NAME_ID = "nId"
        const val COLUMN_NAME_NAME = "sName"
        const val COLUMN_NAME_STARTTIME = "sStartTime"
        const val COLUMN_NAME_ENDTIME = "sEndTime"
    }

    object Alumno : BaseColumns {
        const val TABLE_NAME = "Alumnos"
        const val COLUMN_NAME_ID = "nId"
        const val COLUMN_NAME_NAME = "sName"
        const val COLUMN_NAME_URL_IMAGE = "sUrlImage"
    }

    object Clase : BaseColumns {
        const val TABLE_NAME = "Clases"
        const val COLUMN_NAME_ID = "nId"
        const val COLUMN_NAME_CLASS_DATE = "dClassDate"
        const val COLUMN_NAME_CLASS_CREATE = "dClassCreate"
        const val COLUMN_NAME_CODE = "sCode"

        const val COLUMN_NAME_MATERIA_ID = "nMateria_id"
        const val COLUMN_NAME_GRUPO_ID = "nGrupo_id"
        const val COLUMN_NAME_HORARIO_ID = "nHorario_id"
    }

    object DetalleClase : BaseColumns {
        const val TABLE_NAME = "DetalleClases"
        const val COLUMN_NAME_CLASE_ID = "nClase_id"
        const val COLUMN_NAME_ALUMNO_ID = "nAlumno_id"
        const val COLUMN_NAME_CODE = "nCode"
        const val COLUMN_NAME_MARK_DATE = "nMarkCode"
    }


}
