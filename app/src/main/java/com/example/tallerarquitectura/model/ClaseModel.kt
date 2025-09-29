package com.example.tallerarquitectura.model

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.example.tallerarquitectura.dblocal.AttendanceControlData
import com.example.tallerarquitectura.dblocal.AttendanceControlDbHelper
import com.example.tallerarquitectura.dto.Horario
import com.example.tallerarquitectura.dto.Grupo
import com.example.tallerarquitectura.dto.Materia
import com.example.tallerarquitectura.dto.Clase
import com.example.tallerarquitectura.dto.DetalleClase

class ClaseModel(private val detalleClaseModel: DetalleClaseModel= DetalleClaseModel(), private val connection: SQLiteOpenHelper= AttendanceControlDbHelper.getInstance()) {
    fun getAll(): List<Clase> {
        val db = connection.readableDatabase
        val query = db.rawQuery(
            """
            SELECT
                sn.${AttendanceControlData.Clase.COLUMN_NAME_ID},
                sn.${AttendanceControlData.Clase.COLUMN_NAME_CODE},
                sn.${AttendanceControlData.Clase.COLUMN_NAME_CLASS_DATE},
                sn.${AttendanceControlData.Clase.COLUMN_NAME_CLASS_CREATE},
                sn.${AttendanceControlData.Clase.COLUMN_NAME_GRUPO_ID} AS ClaseID,

                s.${AttendanceControlData.Materia.COLUMN_NAME_ID} AS MateriaID,
                s.${AttendanceControlData.Materia.COLUMN_NAME_NAME} AS MateriaName,

                c.${AttendanceControlData.Horario.COLUMN_NAME_ID} AS HorarioID,
                c.${AttendanceControlData.Horario.COLUMN_NAME_NAME} AS HorarioName,
                c.${AttendanceControlData.Horario.COLUMN_NAME_STARTTIME} AS HorarioStartTime,
                c.${AttendanceControlData.Horario.COLUMN_NAME_ENDTIME} AS HorarioEndTime,

                e.${AttendanceControlData.Grupo.COLUMN_NAME_ID} AS GrupoID,
                e.${AttendanceControlData.Grupo.COLUMN_NAME_NAME} AS GrupoName
            FROM ${AttendanceControlData.Clase.TABLE_NAME} sn   
            JOIN ${AttendanceControlData.Materia.TABLE_NAME} s 
                ON sn.${AttendanceControlData.Clase.COLUMN_NAME_MATERIA_ID} = s.${AttendanceControlData.Materia.COLUMN_NAME_ID}
            JOIN ${AttendanceControlData.Horario.TABLE_NAME} c
                ON sn.${AttendanceControlData.Clase.COLUMN_NAME_HORARIO_ID} = c.${AttendanceControlData.Horario.COLUMN_NAME_ID}
            LEFT JOIN ${AttendanceControlData.Grupo.TABLE_NAME} e
                ON sn.${AttendanceControlData.Clase.COLUMN_NAME_GRUPO_ID} = e.${AttendanceControlData.Grupo.COLUMN_NAME_ID}
        """.trimIndent(), null
        )

        val data = mutableListOf<Clase>()

        query.use {
            val id = it.getColumnIndexOrThrow(AttendanceControlData.Clase.COLUMN_NAME_ID)
            val classDate = it.getColumnIndexOrThrow(AttendanceControlData.Clase.COLUMN_NAME_CLASS_DATE)
            val classCreate = it.getColumnIndexOrThrow(AttendanceControlData.Clase.COLUMN_NAME_CLASS_CREATE)
            val qrCode = it.getColumnIndexOrThrow(AttendanceControlData.Clase.COLUMN_NAME_CODE)
            val claseID = it.getColumnIndexOrThrow("ClaseID")

            val materiaID = it.getColumnIndexOrThrow("MateriaID")
            val materiaName = it.getColumnIndexOrThrow("MateriaName")

            val horarioID = it.getColumnIndexOrThrow("HorarioID")
            val carPlate = it.getColumnIndexOrThrow("HorarioName")
            val carMark = it.getColumnIndexOrThrow("HorarioStartTime")
            val carAlias = it.getColumnIndexOrThrow("HorarioEndTime")
            val horarioModel = it.getColumnIndexOrThrow("HorarioModel")

            val grupoID = it.getColumnIndexOrThrow("GrupoID")
            val grupoName = it.getColumnIndexOrThrow("GrupoName")

            while (it.moveToNext()) {
                val grupo = if (it.isNull(claseID)) {
                    null
                } else {
                    Grupo(
                        id = it.getLong(grupoID),
                        name = it.getString(grupoName)
                    )
                }

                val horario = Horario(
                    id = it.getLong(horarioID),
                    name = it.getString(carPlate),
                    starttime = it.getString(carMark),
                    endtime = it.getStringOrNull(carAlias)
                )

                val materia = Materia(
                    id = it.getLong(materiaID),
                    name = it.getString(materiaName),
                    urlImage = it.getStringOrNull(materiaUrlImage)
                )

                data.add(
                    Clase(
                        id = it.getLong(id),
                        classDate = it.getString(classDate),
                        classCreate = it.getString(classCreate),
                        qrCode = it.getString(qrCode),
                        grupo = grupo,
                        horario = horario,
                        materia = materia
                    )
                )
            }
        }

        return data
    }

    fun getById(id: Long): Clase? {
        val db = connection.readableDatabase

        val query = db.rawQuery(
            """
        SELECT
            sn.${AttendanceControlData.Clase.COLUMN_NAME_ID},
            sn.${AttendanceControlData.Clase.COLUMN_NAME_CODE},
            sn.${AttendanceControlData.Clase.COLUMN_NAME_CLASS_DATE},
            sn.${AttendanceControlData.Clase.COLUMN_NAME_CLASS_CREATE},
            sn.${AttendanceControlData.Clase.COLUMN_NAME_GRUPO_ID} AS ClaseID,
            
            s.${AttendanceControlData.Materia.COLUMN_NAME_ID} AS MateriaID,
            s.${AttendanceControlData.Materia.COLUMN_NAME_NAME} AS MateriaName,
            
            c.${AttendanceControlData.Horario.COLUMN_NAME_ID} AS HorarioID,
            c.${AttendanceControlData.Horario.COLUMN_NAME_NAME} AS HorarioName,
            c.${AttendanceControlData.Horario.COLUMN_NAME_STARTTIME} AS HorarioStartTime,
            c.${AttendanceControlData.Horario.COLUMN_NAME_ENDTIME} AS HorarioEndTime,
            
            e.${AttendanceControlData.Grupo.COLUMN_NAME_ID} AS GrupoID,
            e.${AttendanceControlData.Grupo.COLUMN_NAME_NAME} AS GrupoName,
            e.${AttendanceControlData.Grupo.COLUMN_NAME_LOCATION_NAME} AS EnterpriseLocationName,
            e.${AttendanceControlData.Grupo.COLUMN_NAME_LATITUDE} AS EnterpriseLatitude,
            e.${AttendanceControlData.Grupo.COLUMN_NAME_LONGITUDE} AS EnterpriseLongitude,
            e.${AttendanceControlData.Grupo.COLUMN_NAME_URL_IMAGE} AS EnterpriseUrlImage,
                       

        FROM ${AttendanceControlData.Clase.TABLE_NAME} sn
        JOIN ${AttendanceControlData.Materia.TABLE_NAME} s
            ON sn.${AttendanceControlData.Clase.COLUMN_NAME_MATERIA_ID} = s.${AttendanceControlData.Materia.COLUMN_NAME_ID}
        JOIN ${AttendanceControlData.Horario.TABLE_NAME} c
            ON sn.${AttendanceControlData.Clase.COLUMN_NAME_HORARIO_ID} = c.${AttendanceControlData.Horario.COLUMN_NAME_ID}
        LEFT JOIN ${AttendanceControlData.Grupo.TABLE_NAME} e
            ON sn.${AttendanceControlData.Clase.COLUMN_NAME_GRUPO_ID} = e.${AttendanceControlData.Grupo.COLUMN_NAME_ID}
        WHERE sn.${AttendanceControlData.Clase.COLUMN_NAME_ID} = ?
        """.trimIndent(),
            arrayOf(id.toString())
        )

        return query.use {
            if (!it.moveToFirst()) return null

            val grupo = buildGrupo(it)
            val horario = buildHorario(it)
            val materia = buildMateria(it)
            val detalleClase= detalleClaseModel.getByClaseId(id)

            Clase(
                id = it.getLong(it.getColumnIndexOrThrow(AttendanceControlData.Clase.COLUMN_NAME_ID)),
                classDate = it.getString(it.getColumnIndexOrThrow(AttendanceControlData.Clase.COLUMN_NAME_CLASS_DATE)),
                classCreate = it.getString(it.getColumnIndexOrThrow(AttendanceControlData.Clase.COLUMN_NAME_CLASS_CREATE)),
                qrCode = it.getString(it.getColumnIndexOrThrow(AttendanceControlData.Clase.COLUMN_NAME_CODE)),
                grupo = grupo,
                horario = horario,
                materia = materia,
                detail = detalleClase

            )
        }
    }

    fun addDetail(detalleClase: DetalleClase): Long {
        return detalleClaseModel.save(detalleClase)
    }
    fun deleteDetail(clase_id: Long, alumno_id: Long): Int {
        return detalleClaseModel.delete(clase_id, alumno_id)
    }
    fun updateDetail(detalleClase: DetalleClase): Int {
        return detalleClaseModel.update(detalleClase)
    }
    fun getDetail(clase_id: Long, alumno_id: Long): DetalleClase? {
        return detalleClaseModel.getById(clase_id, alumno_id)
    }


    fun save(clase: Clase): Long{
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(AttendanceControlData.Clase.COLUMN_NAME_CLASS_DATE, clase.classDate)
            put(AttendanceControlData.Clase.COLUMN_NAME_CODE, clase.qrCode)
            put(AttendanceControlData.Clase.COLUMN_NAME_MATERIA_ID, clase.materia.id)
            put(AttendanceControlData.Clase.COLUMN_NAME_GRUPO_ID, clase.grupo?.id)
            put(AttendanceControlData.Clase.COLUMN_NAME_CLASS_CREATE, clase.classCreate)
            put(AttendanceControlData.Clase.COLUMN_NAME_HORARIO_ID, clase.horario.id)

        }
        return db.insert(AttendanceControlData.Clase.TABLE_NAME, null, values)
    }

    fun update(clase: Clase): Int{
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(AttendanceControlData.Clase.COLUMN_NAME_CLASS_DATE, clase.classDate)
            put(AttendanceControlData.Clase.COLUMN_NAME_CODE, clase.qrCode)
            put(AttendanceControlData.Clase.COLUMN_NAME_MATERIA_ID, clase.materia.id)
            put(AttendanceControlData.Clase.COLUMN_NAME_GRUPO_ID, clase.grupo?.id)
            put(AttendanceControlData.Clase.COLUMN_NAME_HORARIO_ID, clase.horario.id)
        }
        return db.update(
            AttendanceControlData.Clase.TABLE_NAME,
            values,
            "${AttendanceControlData.Clase.COLUMN_NAME_ID}=?",
            arrayOf(clase.id.toString())
        )
    }

    fun delete(id: Long): Int {
        val db = connection.writableDatabase
        return db.delete(
            AttendanceControlData.Clase.TABLE_NAME,
            "${AttendanceControlData.Clase.COLUMN_NAME_ID}=?",
            arrayOf(id.toString())
        )
    }
    private fun buildGrupo(cursor: Cursor): Grupo? {
        val enterpriseIdCol = "ClaseID"
        return if (cursor.isNull(cursor.getColumnIndexOrThrow(enterpriseIdCol))) {
            null
        } else {
            Grupo(
                id = cursor.getLongByName("GrupoID"),
                name = cursor.getStringByName("GrupoName")
            )
        }
    }

    private fun buildHorario(cursor: Cursor): Horario {
        return Horario(
            id = cursor.getLongByName("HorarioID"),
            name = cursor.getStringByName("HorarioName"),
            starttime = cursor.getStringByName("HorarioStartTime"),
            endtime = cursor.getStringOrNullByName("HorarioEndTime")
        )
    }

    private fun buildMateria(cursor: Cursor): Materia {
        return Materia(
            id = cursor.getLongByName("MateriaID"),
            name = cursor.getStringByName("MateriaName")
        )
    }

    private fun Cursor.getStringByName(name: String) = getString(getColumnIndexOrThrow(name))
    private fun Cursor.getStringOrNullByName(name: String): String? =
        getColumnIndexOrNull(name)?.let { if (isNull(it)) null else getString(it) }

    private fun Cursor.getLongByName(name: String) = getLong(getColumnIndexOrThrow(name))
    private fun Cursor.getIntByName(name: String) = getInt(getColumnIndexOrThrow(name))
    private fun Cursor.getDoubleByName(name: String) = getDouble(getColumnIndexOrThrow(name))
    private fun Cursor.getColumnIndexOrNull(name: String): Int? =
        try {
            getColumnIndexOrThrow(name)
        } catch (e: Exception) {
            null
        }
}