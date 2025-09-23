package com.example.tallerarquitectura.model

import android.content.ContentValues
import androidx.core.database.getStringOrNull
import com.example.tallerarquitectura.dblocal.CarAtelierContract
import com.example.tallerarquitectura.dblocal.CarAtelierDbHelper
import com.example.tallerarquitectura.dto.Car

class CarModel(private val connection: CarAtelierDbHelper= CarAtelierDbHelper.getInstance()) {
    fun getAll(): List<Car> {
        val db = connection.readableDatabase
        val sortOrder = "${CarAtelierContract.Car.COLUMN_NAME_PLATE} DESC"
        val query = db.query(
            CarAtelierContract.Car.TABLE_NAME, null, null, null, null, null, sortOrder
        )

        return query.use {
            val id = it.getColumnIndexOrThrow(CarAtelierContract.Car.COLUMN_NAME_ID)
            val plate = it.getColumnIndexOrThrow(CarAtelierContract.Car.COLUMN_NAME_PLATE)
            val alias = it.getColumnIndexOrThrow(CarAtelierContract.Car.COLUMN_NAME_ALIAS)
            val model = it.getColumnIndexOrThrow(CarAtelierContract.Car.COLUMN_NAME_MODEL)
            val cylinder = it.getColumnIndexOrThrow(CarAtelierContract.Car.COLUMN_NAME_CYLINDER)
            val urlImage = it.getColumnIndexOrThrow(CarAtelierContract.Car.COLUMN_NAME_URL_IMAGE)
            val year = it.getColumnIndexOrThrow(CarAtelierContract.Car.COLUMN_NAME_YEAR)
            val mark = it.getColumnIndexOrThrow(CarAtelierContract.Car.COLUMN_NAME_MARK)

            val data = mutableListOf<Car>()

            while (it.moveToNext()) {
                data.add(
                    Car(
                        id = it.getLong(id),
                        plate = it.getString(plate),
                        alias = it.getStringOrNull(alias),
                        model = it.getString(model),
                        cylinder = it.getString(cylinder),
                        urlImage = it.getStringOrNull(urlImage),
                        year = it.getInt(year),
                        mark = it.getString(mark)
                    )
                )
            }
            data
        }
    }

    fun getById(id: Long): Car? {
        val db = connection.readableDatabase
        val selection = "${CarAtelierContract.Car.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())

        val query = db.query(
            CarAtelierContract.Car.TABLE_NAME, null, selection, selectionArgs, null, null, null
        )

        return query.use {
            val id = it.getColumnIndexOrThrow(CarAtelierContract.Car.COLUMN_NAME_ID)
            val plate = it.getColumnIndexOrThrow(CarAtelierContract.Car.COLUMN_NAME_PLATE)
            val alias = it.getColumnIndexOrThrow(CarAtelierContract.Car.COLUMN_NAME_ALIAS)
            val model = it.getColumnIndexOrThrow(CarAtelierContract.Car.COLUMN_NAME_MODEL)
            val cylinder = it.getColumnIndexOrThrow(CarAtelierContract.Car.COLUMN_NAME_CYLINDER)
            val urlImage = it.getColumnIndexOrThrow(CarAtelierContract.Car.COLUMN_NAME_URL_IMAGE)
            val year = it.getColumnIndexOrThrow(CarAtelierContract.Car.COLUMN_NAME_YEAR)
            val mark = it.getColumnIndexOrThrow(CarAtelierContract.Car.COLUMN_NAME_MARK)

            if (it.moveToFirst()) {
                Car(
                    id = it.getLong(id),
                    plate = it.getString(plate),
                    alias = it.getStringOrNull(alias),
                    model = it.getString(model),
                    cylinder = it.getString(cylinder),
                    urlImage = it.getStringOrNull(urlImage),
                    year = it.getInt(year),
                    mark = it.getString(mark)
                )
            } else {
                null
            }
        }
    }

    fun save(car: Car): Long {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(CarAtelierContract.Car.COLUMN_NAME_PLATE, car.plate)
            put(CarAtelierContract.Car.COLUMN_NAME_ALIAS, car.alias)
            put(CarAtelierContract.Car.COLUMN_NAME_MODEL, car.model)
            put(CarAtelierContract.Car.COLUMN_NAME_CYLINDER, car.cylinder)
            put(CarAtelierContract.Car.COLUMN_NAME_URL_IMAGE, car.urlImage)
            put(CarAtelierContract.Car.COLUMN_NAME_MARK, car.mark)
            put(CarAtelierContract.Car.COLUMN_NAME_YEAR, car.year)

        }
        return db.insert(CarAtelierContract.Car.TABLE_NAME, null, values)
    }

    fun update(car: Car): Int {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(CarAtelierContract.Car.COLUMN_NAME_PLATE, car.plate)
            put(CarAtelierContract.Car.COLUMN_NAME_ALIAS, car.alias)
            put(CarAtelierContract.Car.COLUMN_NAME_MODEL, car.model)
            put(CarAtelierContract.Car.COLUMN_NAME_CYLINDER, car.cylinder)
            put(CarAtelierContract.Car.COLUMN_NAME_URL_IMAGE, car.urlImage)
            put(CarAtelierContract.Car.COLUMN_NAME_MARK, car.mark)
            put(CarAtelierContract.Car.COLUMN_NAME_YEAR, car.year)
        }
        val selection = "${CarAtelierContract.Car.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(car.id.toString())
        return db.update(
            CarAtelierContract.Car.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }

    fun delete(id: Long): Int {
        val db = connection.writableDatabase
        val selection = "${CarAtelierContract.Car.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(CarAtelierContract.Car.TABLE_NAME, selection, selectionArgs)
    }
}