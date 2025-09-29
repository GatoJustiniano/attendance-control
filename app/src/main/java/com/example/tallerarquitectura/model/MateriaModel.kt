package com.example.tallerarquitectura.model

import android.content.ContentValues
import androidx.core.database.getStringOrNull
import com.example.tallerarquitectura.dblocal.AttendanceControlData
import com.example.tallerarquitectura.dblocal.AttendanceControlDbHelper
import com.example.tallerarquitectura.dto.Materia

class MateriaModel(private val connection: AttendanceControlDbHelper=AttendanceControlDbHelper.getInstance()) {
    fun getAll(): List<Materia> {
        val db = connection.readableDatabase
        val sortOrder = "${AttendanceControlData.Materia.COLUMN_NAME_NAME} DESC"
        val cursor = db.query(
            AttendanceControlData.Materia.TABLE_NAME, null, null, null, null, null, sortOrder
        )
        val data = mutableListOf<Materia>()

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(AttendanceControlData.Materia.COLUMN_NAME_ID))
                val name =
                    getString(getColumnIndexOrThrow(AttendanceControlData.Materia.COLUMN_NAME_NAME))
                data.add(Materia(id, name))
            }
        }
        cursor.close()
        return data
    }

    fun getById(id: Long): Materia? {
        val db = connection.readableDatabase
        val selection = "${AttendanceControlData.Materia.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        val cursor = db.query(
            AttendanceControlData.Materia.TABLE_NAME, null, selection, selectionArgs, null, null, null
        )
        var materia: Materia? = null
        with(cursor) {
            if (moveToFirst()) {
                val name =
                    getString(getColumnIndexOrThrow(AttendanceControlData.Materia.COLUMN_NAME_NAME))
                materia = Materia(id, name)
            }
        }
        cursor.close()
        return materia
    }

    fun save(materia: Materia): Long {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(AttendanceControlData.Materia.COLUMN_NAME_NAME, materia.name)
        }
        return db.insert(AttendanceControlData.Materia.TABLE_NAME, null, values)
    }
    fun update(materia: Materia): Int {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(AttendanceControlData.Materia.COLUMN_NAME_NAME, materia.name)
        }
        val selection = "${AttendanceControlData.Materia.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(materia.id.toString())
        return db.update(
            AttendanceControlData.Materia.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }

    fun delete(id: Long): Int {
        val db = connection.writableDatabase
        val selection = "${AttendanceControlData.Materia.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(AttendanceControlData.Materia.TABLE_NAME, selection, selectionArgs)
    }
}