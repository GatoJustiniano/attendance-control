package com.example.tallerarquitectura.dblocal

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tallerarquitectura.MainActivity

class CarAtelierDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "CarAtelier.db"
        const val DATABASE_VERSION = 9

        @Volatile
        private var INSTANCE: CarAtelierDbHelper? = null

        fun getInstance(context: Context= MainActivity.Companion.appContext): CarAtelierDbHelper {

            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CarAtelierDbHelper(context).also {
                    INSTANCE = it
                }
            }
        }

        private const val SQL_CREATE_MEASURE_UNITS = """
            CREATE TABLE IF NOT EXISTS ${CarAtelierContract.MeasureUnit.TABLE_NAME} (
            ${CarAtelierContract.MeasureUnit.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${CarAtelierContract.MeasureUnit.COLUMN_NAME_NAME} TEXT NOT NULL,
            ${CarAtelierContract.MeasureUnit.COLUMN_NAME_SHORT} TEXT NOT NULL
         );
        """
        private const val SQL_CREATE_SERVICES = """
            CREATE TABLE IF NOT EXISTS ${CarAtelierContract.Service.TABLE_NAME} (
            ${CarAtelierContract.Service.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${CarAtelierContract.Service.COLUMN_NAME_NAME} TEXT NOT NULL,
            ${CarAtelierContract.Service.COLUMN_NAME_URL_IMAGE} TEXT
            );
        """
        private const val SQL_CREATE_ENTERPRISES = """
            CREATE TABLE IF NOT EXISTS ${CarAtelierContract.Enterprise.TABLE_NAME}(
            ${CarAtelierContract.Enterprise.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${CarAtelierContract.Enterprise.COLUMN_NAME_NAME} TEXT NOT NULL,
            ${CarAtelierContract.Enterprise.COLUMN_NAME_LOCATION_NAME} TEXT,
            ${CarAtelierContract.Enterprise.COLUMN_NAME_LATITUDE} REAL,
            ${CarAtelierContract.Enterprise.COLUMN_NAME_LONGITUDE} REAL,
            ${CarAtelierContract.Enterprise.COLUMN_NAME_URL_IMAGE} TEXT
            );
        """

        private const val SQL_CREATE_CARS = """
            CREATE TABLE IF NOT EXISTS ${CarAtelierContract.Car.TABLE_NAME}(
            ${CarAtelierContract.Car.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${CarAtelierContract.Car.COLUMN_NAME_PLATE} TEXT NOT NULL,
            ${CarAtelierContract.Car.COLUMN_NAME_ALIAS} TEXT,
            ${CarAtelierContract.Car.COLUMN_NAME_MODEL} TEXT NOT NULL,
            ${CarAtelierContract.Car.COLUMN_NAME_CYLINDER} TEXT  NOT NULL,
            ${CarAtelierContract.Car.COLUMN_NAME_URL_IMAGE} TEXT,
            ${CarAtelierContract.Car.COLUMN_NAME_MARK} TEXT NOT NULL,
            ${CarAtelierContract.Car.COLUMN_NAME_YEAR} INTEGER NOT NULL
            );
        """

        private const val SQL_CREATE_PRODUCT="""
            CREATE TABLE IF NOT EXISTS ${CarAtelierContract.Product.TABLE_NAME}(
            ${CarAtelierContract.Product.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${CarAtelierContract.Product.COLUMN_NAME_NAME} TEXT NOT NULL,
            ${CarAtelierContract.Product.COLUMN_NAME_DETAIL} TEXT,
            ${CarAtelierContract.Product.COLUMN_NAME_URL_IMAGE} TEXT
            );
        """

        private const val SQL_CREATE_SERVICE_NOTES="""
            CREATE TABLE IF NOT EXISTS ${CarAtelierContract.ServiceNote.TABLE_NAME}(
            ${CarAtelierContract.ServiceNote.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${CarAtelierContract.ServiceNote.COLUMN_NAME_SERVICE_DATE} TEXT DEFAULT (datetime('now','localtime')),
            ${CarAtelierContract.ServiceNote.COLUMN_NAME_CREATION_DATE} TEXT NOT NULL,
            ${CarAtelierContract.ServiceNote.COLUMN_NAME_VOUCHER_CODE} TEXT,
            ${CarAtelierContract.ServiceNote.COLUMN_NAME_VOUCHER_URL_IMAGE} TEXT,
            
            ${CarAtelierContract.ServiceNote.COLUMN_NAME_SERVICE_ID} INTEGER NOT NULL,
            ${CarAtelierContract.ServiceNote.COLUMN_NAME_ENTERPRISE_ID} INTEGER,
            ${CarAtelierContract.ServiceNote.COLUMN_NAME_CAR_ID} INTEGER NOT NULL,
            
            FOREIGN KEY(${CarAtelierContract.ServiceNote.COLUMN_NAME_SERVICE_ID}) REFERENCES ${CarAtelierContract.Service.TABLE_NAME}(${CarAtelierContract.Service.COLUMN_NAME_ID}),
            
            FOREIGN KEY(${CarAtelierContract.ServiceNote.COLUMN_NAME_ENTERPRISE_ID}) REFERENCES ${CarAtelierContract.Enterprise.TABLE_NAME}(${CarAtelierContract.Enterprise.COLUMN_NAME_ID}),
            
            FOREIGN KEY(${CarAtelierContract.ServiceNote.COLUMN_NAME_CAR_ID}) REFERENCES ${CarAtelierContract.Car.TABLE_NAME}(${CarAtelierContract.Car.COLUMN_NAME_ID})
            
            );
        """

        private const val SQL_CREATE_SERVICE_NOTES_DETAILS="""
            CREATE TABLE IF NOT EXISTS ${CarAtelierContract.ServiceNoteDetail.TABLE_NAME}(
            ${CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_SERVICE_NOTE_ID} INTEGER NOT NULL,
            ${CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_PRODUCT_ID} INTEGER NOT NULL,
            ${CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_QUANTITY} INTEGER NOT NULL,
            ${CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_PRICE} REAL NOT NULL,
            
            PRIMARY KEY(${CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_SERVICE_NOTE_ID},${CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_PRODUCT_ID}),
            
            FOREIGN KEY(${CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_SERVICE_NOTE_ID}) REFERENCES ${CarAtelierContract.ServiceNote.TABLE_NAME}(${CarAtelierContract.ServiceNote.COLUMN_NAME_ID}),
            FOREIGN KEY(${CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_PRODUCT_ID}) REFERENCES ${CarAtelierContract.Product.TABLE_NAME}(${CarAtelierContract.Product.COLUMN_NAME_ID})
            
            );
        """

        private const val SQL_CREATE_SERVICE_REMINDER_NOTE="""
            CREATE TABLE IF NOT EXISTS ${CarAtelierContract.ReminderNote.TABLE_NAME}(
            ${CarAtelierContract.ReminderNote.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${CarAtelierContract.ReminderNote.COLUMN_NAME_STATUS} INTEGER NOT NULL,
            ${CarAtelierContract.ReminderNote.COLUMN_NAME_END_DATE} TEXT NOT NULL,
            ${CarAtelierContract.ReminderNote.COLUMN_NAME_END_OTHER_DATA} INTEGER NOT NULL,
            
            ${CarAtelierContract.ReminderNote.COLUMN_NAME_UNIT_MEASURE_ID} INTEGER,
            ${CarAtelierContract.ReminderNote.COLUMN_NAME_SERVICE_NOTE_ID} INTEGER NOT NULL,
            
            FOREIGN KEY(${CarAtelierContract.ReminderNote.COLUMN_NAME_UNIT_MEASURE_ID}) REFERENCES ${CarAtelierContract.MeasureUnit.TABLE_NAME}(${CarAtelierContract.MeasureUnit.COLUMN_NAME_ID}),
            FOREIGN KEY(${CarAtelierContract.ReminderNote.COLUMN_NAME_SERVICE_NOTE_ID}) REFERENCES ${CarAtelierContract.ServiceNote.TABLE_NAME}(${CarAtelierContract.ServiceNote.COLUMN_NAME_ID})
            );
        """

        private const val SQL_DELETE_MEASURE_UNITS = """
            DROP TABLE IF EXISTS ${CarAtelierContract.MeasureUnit.TABLE_NAME};
        """
        private const val SQL_DELETE_CAR = """
            DROP TABLE IF EXISTS ${CarAtelierContract.Car.TABLE_NAME};
        """

    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_MEASURE_UNITS)
        db.execSQL(SQL_CREATE_SERVICES)
        db.execSQL(SQL_CREATE_ENTERPRISES)
        db.execSQL(SQL_CREATE_CARS)
        db.execSQL(SQL_CREATE_PRODUCT)
        db.execSQL(SQL_CREATE_SERVICE_NOTES)
        db.execSQL(SQL_CREATE_SERVICE_NOTES_DETAILS)
        db.execSQL(SQL_CREATE_SERVICE_REMINDER_NOTE)
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.execSQL("PRAGMA foreign_keys = ON")
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        //db.execSQL(SQL_DELETE_MEASURE_UNITS)
        if (oldVersion < 2) {
            db.execSQL(SQL_CREATE_SERVICES)
        }
        if (oldVersion < 3) {
            db.execSQL(SQL_CREATE_ENTERPRISES)
        }
        if (oldVersion<4){
            db.execSQL(SQL_CREATE_CARS)
        }
        if (oldVersion<5){
            db.execSQL(SQL_DELETE_CAR)
            db.execSQL(SQL_CREATE_CARS)
        }
        if(oldVersion<6){
            db.execSQL(SQL_CREATE_PRODUCT)
        }
        if (oldVersion<7){
            db.execSQL(SQL_CREATE_SERVICE_NOTES)
        }
        if (oldVersion<8){
            db.execSQL(SQL_CREATE_SERVICE_NOTES_DETAILS)
        }
        if (oldVersion<9){
            db.execSQL(SQL_CREATE_SERVICE_REMINDER_NOTE)
        }
    }

}

