package com.example.tallerarquitectura.model

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import com.example.tallerarquitectura.dblocal.CarAtelierContract
import com.example.tallerarquitectura.dblocal.CarAtelierDbHelper
import com.example.tallerarquitectura.dto.MeasureUnit
import com.example.tallerarquitectura.dto.ReminderNoteCreate
import com.example.tallerarquitectura.dto.ReminderNoteWithMeasureUnit

class ReminderNoteModel(
    private val connection: SQLiteOpenHelper=CarAtelierDbHelper.getInstance()
) {
    companion object {
        const val TODO = 1
        const val DONE = 2
    }

    fun save(reminderNoteCreate: ReminderNoteCreate): Long {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(CarAtelierContract.ReminderNote.COLUMN_NAME_STATUS, TODO)
            put(CarAtelierContract.ReminderNote.COLUMN_NAME_END_DATE, reminderNoteCreate.endDate)
            put(
                CarAtelierContract.ReminderNote.COLUMN_NAME_END_OTHER_DATA,
                reminderNoteCreate.endOtherData
            )
            put(
                CarAtelierContract.ReminderNote.COLUMN_NAME_SERVICE_NOTE_ID,
                reminderNoteCreate.serviceNoteId
            )
            put(
                CarAtelierContract.ReminderNote.COLUMN_NAME_UNIT_MEASURE_ID,
                reminderNoteCreate.measureUnitId
            )

        }
        return db.insert(CarAtelierContract.ReminderNote.TABLE_NAME, null, values)
    }

    fun getByServiceNoteId(serviceNoteId: Long): ReminderNoteWithMeasureUnit? {
        val db = connection.readableDatabase
        val query = db.rawQuery(
            """
                SELECT 
                rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_ID} ReminderNoteId,
                rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_STATUS} ReminderNoteStatus,
                rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_END_DATE} ReminderNoteEndDate,
                rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_END_OTHER_DATA} ReminderNoteEndOtherData,
                rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_SERVICE_NOTE_ID} ServiceNoteId,
                
                mu.${CarAtelierContract.MeasureUnit.COLUMN_NAME_ID} MeasureUnitId,
                mu.${CarAtelierContract.MeasureUnit.COLUMN_NAME_NAME} MeasureUnitName,
                mu.${CarAtelierContract.MeasureUnit.COLUMN_NAME_SHORT} MeasureUnitShort
                
                FROM ${CarAtelierContract.ReminderNote.TABLE_NAME} rn
                LEFT JOIN ${CarAtelierContract.MeasureUnit.TABLE_NAME} mu
                 ON rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_UNIT_MEASURE_ID} = mu.${CarAtelierContract.MeasureUnit.COLUMN_NAME_ID}
                WHERE rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_SERVICE_NOTE_ID}= ?
            """.trimIndent(), arrayOf(serviceNoteId.toString())
        )
        return query.use {
            val reminderNoteId =
                it.getColumnIndexOrThrow("ReminderNoteId")
            val reminderNoteStatus =
                it.getColumnIndexOrThrow("ReminderNoteStatus")
            val reminderNoteEndDate =
                it.getColumnIndexOrThrow("ReminderNoteEndDate")
            val reminderNoteEndOtherData =
                it.getColumnIndexOrThrow("ReminderNoteEndOtherData")

            val measureUnitId =
                it.getColumnIndexOrThrow("MeasureUnitId")
            val measureUnitName =
                it.getColumnIndexOrThrow("MeasureUnitName")
            val measureUnitSymbol =
                it.getColumnIndexOrThrow("MeasureUnitShort")
            val serviceNoteId=it.getColumnIndexOrThrow("ServiceNoteId")

            if (!it.moveToFirst()) return null
            val measureUnit = if (it.getLong(measureUnitId) == 0L) null else MeasureUnit(
                id = it.getLong(measureUnitId),
                name = it.getString(measureUnitName),
                short = it.getString(measureUnitSymbol)
            )
            ReminderNoteWithMeasureUnit(
                id = it.getLong(reminderNoteId),
                status = it.getInt(reminderNoteStatus),
                endDate = it.getString(reminderNoteEndDate),
                endOtherData = it.getInt(reminderNoteEndOtherData),
                measureUnit = measureUnit,
                serviceNoteId = it.getLong(serviceNoteId)
            )

        }
    }


    fun getAll(): List<ReminderNoteWithMeasureUnit> {
        val reminderNote = mutableListOf<ReminderNoteWithMeasureUnit>()
        val db = connection.readableDatabase
        val query = db.rawQuery(
            """
                SELECT 
                rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_ID} ReminderNoteId,
                rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_STATUS} ReminderNoteStatus,
                rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_END_DATE} ReminderNoteEndDate,
                rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_END_OTHER_DATA} ReminderNoteEndOtherData,
                rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_SERVICE_NOTE_ID} ServiceNoteId,
                
                mu.${CarAtelierContract.MeasureUnit.COLUMN_NAME_ID} MeasureUnitId,
                mu.${CarAtelierContract.MeasureUnit.COLUMN_NAME_NAME} MeasureUnitName,
                mu.${CarAtelierContract.MeasureUnit.COLUMN_NAME_SHORT} MeasureUnitShort
                
                FROM ${CarAtelierContract.ReminderNote.TABLE_NAME} rn
                LEFT JOIN ${CarAtelierContract.MeasureUnit.TABLE_NAME} mu
                 ON rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_UNIT_MEASURE_ID} = mu.${CarAtelierContract.MeasureUnit.COLUMN_NAME_ID}
            """.trimIndent(), null
        )

        return query.use {
            val reminderNoteId =
                it.getColumnIndexOrThrow("ReminderNoteId")
            val reminderNoteStatus =
                it.getColumnIndexOrThrow("ReminderNoteStatus")
            val reminderNoteEndDate =
                it.getColumnIndexOrThrow("ReminderNoteEndDate")
            val reminderNoteEndOtherData =
                it.getColumnIndexOrThrow("ReminderNoteEndOtherData")

            val measureUnitId =
                it.getColumnIndexOrThrow("MeasureUnitId")
            val measureUnitName =
                it.getColumnIndexOrThrow("MeasureUnitName")
            val measureUnitSymbol =
                it.getColumnIndexOrThrow("MeasureUnitShort")
            val serviceNoteId=it.getColumnIndexOrThrow("ServiceNoteId")
            while (it.moveToNext()) {
                val measureUnit = if (it.getLong(measureUnitId) == 0L) null else MeasureUnit(
                    id = it.getLong(measureUnitId),
                    name = it.getString(measureUnitName),
                    short = it.getString(measureUnitSymbol)
                )
                reminderNote.add(
                    ReminderNoteWithMeasureUnit(
                        id = it.getLong(reminderNoteId),
                        status = it.getInt(reminderNoteStatus),
                        endDate = it.getString(reminderNoteEndDate),
                        endOtherData = it.getInt(reminderNoteEndOtherData),
                        measureUnit = measureUnit,
                        serviceNoteId = it.getLong(serviceNoteId)
                    )
                )
            }
            reminderNote

        }
    }

    fun update(reminderNoteCreate: ReminderNoteCreate): Int {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(CarAtelierContract.ReminderNote.COLUMN_NAME_STATUS,reminderNoteCreate.status)
            put(CarAtelierContract.ReminderNote.COLUMN_NAME_END_DATE,reminderNoteCreate.endDate)
            put(CarAtelierContract.ReminderNote.COLUMN_NAME_END_OTHER_DATA, reminderNoteCreate.endOtherData)
            put(CarAtelierContract.ReminderNote.COLUMN_NAME_UNIT_MEASURE_ID,reminderNoteCreate.measureUnitId)
            put(CarAtelierContract.ReminderNote.COLUMN_NAME_SERVICE_NOTE_ID,reminderNoteCreate.serviceNoteId)
        }
        val selection = "${CarAtelierContract.ReminderNote.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(reminderNoteCreate.id.toString())
        return db.update(
            CarAtelierContract.ReminderNote.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }

    fun delete(id: Long): Int {
        val db = connection.writableDatabase
        val selection = "${CarAtelierContract.ReminderNote.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(CarAtelierContract.ReminderNote.TABLE_NAME, selection, selectionArgs)
    }
}