package com.example.tallerarquitectura.model

import android.content.ContentValues
import com.example.tallerarquitectura.dblocal.CarAtelierContract
import com.example.tallerarquitectura.dblocal.CarAtelierDbHelper
import com.example.tallerarquitectura.dto.MeasureUnit

class MeasureUnitModel(
    private val connection: CarAtelierDbHelper=
        CarAtelierDbHelper.getInstance()
) {

    fun getAll(): List<MeasureUnit> {
        val db = connection.readableDatabase
        val sortOrder = "${CarAtelierContract.MeasureUnit.COLUMN_NAME_NAME} DESC"
        val cursor = db.query(
            CarAtelierContract.MeasureUnit.TABLE_NAME, null, null, null, null, null, sortOrder
        )
        val data = mutableListOf<MeasureUnit>()

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(CarAtelierContract.MeasureUnit.COLUMN_NAME_ID))
                val name =
                    getString(getColumnIndexOrThrow(CarAtelierContract.MeasureUnit.COLUMN_NAME_NAME))
                val short =
                    getString(getColumnIndexOrThrow(CarAtelierContract.MeasureUnit.COLUMN_NAME_SHORT))
                data.add(MeasureUnit(id, name, short))
            }
        }
        cursor.close()
        return data
    }

    fun getById(id: Long): MeasureUnit? {
        val db = connection.readableDatabase
        val selection = "${CarAtelierContract.MeasureUnit.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        val cursor = db.query(
            CarAtelierContract.MeasureUnit.TABLE_NAME, null, selection, selectionArgs, null, null, null
        )
        var measureUnit: MeasureUnit? = null
        with(cursor) {
            if (moveToFirst()) {
                val name =
                    getString(getColumnIndexOrThrow(CarAtelierContract.MeasureUnit.COLUMN_NAME_NAME))
                val short =
                    getString(getColumnIndexOrThrow(CarAtelierContract.MeasureUnit.COLUMN_NAME_SHORT))
                measureUnit = MeasureUnit(id, name, short)
            }
        }
        cursor.close()
        return measureUnit
    }

    fun save(measureUnit: MeasureUnit): Long {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(CarAtelierContract.MeasureUnit.COLUMN_NAME_NAME, measureUnit.name)
            put(CarAtelierContract.MeasureUnit.COLUMN_NAME_SHORT, measureUnit.short)
        }
        return db.insert(CarAtelierContract.MeasureUnit.TABLE_NAME, null, values)
    }

    fun update(measureUnit: MeasureUnit): Int {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(CarAtelierContract.MeasureUnit.COLUMN_NAME_NAME, measureUnit.name)
            put(CarAtelierContract.MeasureUnit.COLUMN_NAME_SHORT, measureUnit.short)
        }
        val selection = "${CarAtelierContract.MeasureUnit.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(measureUnit.id.toString())
        return db.update(
            CarAtelierContract.MeasureUnit.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }

    fun delete(id: Long): Int {
        val db = connection.writableDatabase
        val selection = "${CarAtelierContract.MeasureUnit.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(CarAtelierContract.MeasureUnit.TABLE_NAME, selection, selectionArgs)
    }
}