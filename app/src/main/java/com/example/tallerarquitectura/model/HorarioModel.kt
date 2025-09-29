package com.example.tallerarquitectura.model

import android.content.ContentValues
import androidx.core.database.getStringOrNull
import com.example.tallerarquitectura.dblocal.AttendanceControlData
import com.example.tallerarquitectura.dblocal.AttendanceControlDbHelper
import com.example.tallerarquitectura.dto.Horario

class HorarioModel(private val connection: AttendanceControlDbHelper= AttendanceControlDbHelper.getInstance()) {
    fun getAll(): List<Horario> {
        val db = connection.readableDatabase
        val sortOrder = "${AttendanceControlData.Horario.COLUMN_NAME_NAME} DESC"
        val query = db.query(
            AttendanceControlData.Horario.TABLE_NAME, null, null, null, null, null, sortOrder
        )

        return query.use {
            val id = it.getColumnIndexOrThrow(AttendanceControlData.Horario.COLUMN_NAME_ID)
            val name = it.getColumnIndexOrThrow(AttendanceControlData.Horario.COLUMN_NAME_NAME)
            val starttime = it.getColumnIndexOrThrow(AttendanceControlData.Horario.COLUMN_NAME_STARTTIME)
            val endtime = it.getColumnIndexOrThrow(AttendanceControlData.Horario.COLUMN_NAME_ENDTIME)

            val data = mutableListOf<Horario>()

            while (it.moveToNext()) {
                data.add(
                    Horario(
                        id = it.getLong(id),
                        name = it.getString(name),
                        endtime = it.getStringOrNull(endtime),
                        starttime = it.getString(starttime)
                    )
                )
            }
            data
        }
    }

    fun getById(id: Long): Horario? {
        val db = connection.readableDatabase
        val selection = "${AttendanceControlData.Horario.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())

        val query = db.query(
            AttendanceControlData.Horario.TABLE_NAME, null, selection, selectionArgs, null, null, null
        )

        return query.use {
            val id = it.getColumnIndexOrThrow(AttendanceControlData.Horario.COLUMN_NAME_ID)
            val name = it.getColumnIndexOrThrow(AttendanceControlData.Horario.COLUMN_NAME_NAME)
            val endtime = it.getColumnIndexOrThrow(AttendanceControlData.Horario.COLUMN_NAME_ENDTIME)
            val starttime = it.getColumnIndexOrThrow(AttendanceControlData.Horario.COLUMN_NAME_STARTTIME)

            if (it.moveToFirst()) {
                Horario(
                    id = it.getLong(id),
                    name = it.getString(name),
                    endtime = it.getStringOrNull(endtime),
                    starttime = it.getString(starttime)
                )
            } else {
                null
            }
        }
    }

    fun save(horario: Horario): Long {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(AttendanceControlData.Horario.COLUMN_NAME_NAME, horario.name)
            put(AttendanceControlData.Horario.COLUMN_NAME_STARTTIME, horario.starttime)
            put(AttendanceControlData.Horario.COLUMN_NAME_ENDTIME, horario.endtime)
        }
        return db.insert(AttendanceControlData.Horario.TABLE_NAME, null, values)
    }

    fun update(horario: Horario): Int {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(AttendanceControlData.Horario.COLUMN_NAME_NAME, horario.name)
            put(AttendanceControlData.Horario.COLUMN_NAME_STARTTIME, horario.starttime)
            put(AttendanceControlData.Horario.COLUMN_NAME_ENDTIME, horario.endtime)
        }
        val selection = "${AttendanceControlData.Horario.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(horario.id.toString())
        return db.update(
            AttendanceControlData.Horario.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }

    fun delete(id: Long): Int {
        val db = connection.writableDatabase
        val selection = "${AttendanceControlData.Horario.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(AttendanceControlData.Horario.TABLE_NAME, selection, selectionArgs)
    }
}