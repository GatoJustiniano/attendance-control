package com.example.tallerarquitectura.model

import android.content.ContentValues
import androidx.core.database.getStringOrNull
import com.example.tallerarquitectura.dblocal.CarAtelierContract
import com.example.tallerarquitectura.dblocal.CarAtelierDbHelper
import com.example.tallerarquitectura.dto.Product

class ProductModel(private val connection: CarAtelierDbHelper=CarAtelierDbHelper.getInstance()) {
    fun getAll(): List<Product> {
        val db = connection.readableDatabase
        val sortOrder = "${CarAtelierContract.Product.COLUMN_NAME_NAME} DESC"
        val query = db.query(
            CarAtelierContract.Product.TABLE_NAME, null, null, null, null, null, sortOrder
        )
        val data = mutableListOf<Product>()
        return query.use {
            val id = it.getColumnIndexOrThrow(CarAtelierContract.Product.COLUMN_NAME_ID)
            val name = it.getColumnIndexOrThrow(CarAtelierContract.Product.COLUMN_NAME_NAME)
            val detail = it.getColumnIndexOrThrow(CarAtelierContract.Product.COLUMN_NAME_DETAIL)
            val urlImage = it.getColumnIndexOrThrow(CarAtelierContract.Product.COLUMN_NAME_URL_IMAGE)

            while (it.moveToNext()) {
                data.add(
                    Product(
                        id = it.getLong(id),
                        name = it.getString(name),
                        detail = it.getStringOrNull(detail),
                        urlImage = it.getStringOrNull(urlImage)
                    )
                )
            }
            data
        }
    }

    fun getById(id: Long): Product? {
        val db = connection.readableDatabase
        val selection = "${CarAtelierContract.Product.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        val cursor = db.query(
            CarAtelierContract.Product.TABLE_NAME, null, selection, selectionArgs, null, null, null
        )
        var measureUnit: Product? = null
        with(cursor) {
            if (moveToFirst()) {
                val name =
                    getString(getColumnIndexOrThrow(CarAtelierContract.Product.COLUMN_NAME_NAME))
                val detail=
                    getStringOrNull(getColumnIndexOrThrow(CarAtelierContract.Product.COLUMN_NAME_DETAIL))
                val urlImage=
                    getStringOrNull(getColumnIndexOrThrow(CarAtelierContract.Product.COLUMN_NAME_URL_IMAGE))
                measureUnit = Product(id, name, detail,urlImage)
            }
        }
        cursor.close()
        return measureUnit
    }

    fun save(measureUnit: Product): Long {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(CarAtelierContract.Product.COLUMN_NAME_NAME, measureUnit.name)
            put(CarAtelierContract.Product.COLUMN_NAME_DETAIL, measureUnit.detail)
            put(CarAtelierContract.Product.COLUMN_NAME_URL_IMAGE, measureUnit.urlImage)
        }
        return db.insert(CarAtelierContract.Product.TABLE_NAME, null, values)
    }

    fun update(measureUnit: Product): Int {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(CarAtelierContract.Product.COLUMN_NAME_NAME, measureUnit.name)
            put(CarAtelierContract.Product.COLUMN_NAME_DETAIL, measureUnit.detail)
            put(CarAtelierContract.Product.COLUMN_NAME_URL_IMAGE, measureUnit.urlImage)
        }
        val selection = "${CarAtelierContract.Product.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(measureUnit.id.toString())
        return db.update(
            CarAtelierContract.Product.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }

    fun delete(id: Long): Int {
        val db = connection.writableDatabase
        val selection = "${CarAtelierContract.Product.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(CarAtelierContract.Product.TABLE_NAME, selection, selectionArgs)
    }
}