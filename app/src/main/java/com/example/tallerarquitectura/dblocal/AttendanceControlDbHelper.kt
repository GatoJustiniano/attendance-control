package com.example.tallerarquitectura.dblocal

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tallerarquitectura.MainActivity

class AttendanceControlDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "ControlDeAsistencia.db"
        const val DATABASE_VERSION = 9

        @Volatile
        private var INSTANCE: AttendanceControlDbHelper? = null

        fun getInstance(context: Context= MainActivity.Companion.appContext): AttendanceControlDbHelper {

            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AttendanceControlDbHelper(context).also {
                    INSTANCE = it
                }
            }
        }

        private const val SQL_CREATE_MATERIAS = """
            CREATE TABLE IF NOT EXISTS ${AttendanceControlData.Materia.TABLE_NAME} (
            ${AttendanceControlData.Materia.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${AttendanceControlData.Materia.COLUMN_NAME_NAME} TEXT NOT NULL
            );
        """
        private const val SQL_CREATE_GRUPOS = """
            CREATE TABLE IF NOT EXISTS ${AttendanceControlData.Grupo.TABLE_NAME}(
            ${AttendanceControlData.Grupo.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${AttendanceControlData.Grupo.COLUMN_NAME_NAME} TEXT NOT NULL
            );
        """

        private const val SQL_CREATE_HORARIOS = """
            CREATE TABLE IF NOT EXISTS ${AttendanceControlData.Horario.TABLE_NAME}(
            ${AttendanceControlData.Horario.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${AttendanceControlData.Horario.COLUMN_NAME_NAME} TEXT NOT NULL,
            ${AttendanceControlData.Horario.COLUMN_NAME_STARTTIME} TEXT NOT NULL,
            ${AttendanceControlData.Horario.COLUMN_NAME_ENDTIME} TEXT
            );
        """

        private const val SQL_CREATE_ALUMNOS="""
            CREATE TABLE IF NOT EXISTS ${AttendanceControlData.Alumno.TABLE_NAME}(
            ${AttendanceControlData.Alumno.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${AttendanceControlData.Alumno.COLUMN_NAME_NAME} TEXT NOT NULL,
            ${AttendanceControlData.Alumno.COLUMN_NAME_URL_IMAGE} TEXT
            );
        """

        private const val SQL_CREATE_CLASES="""
            CREATE TABLE IF NOT EXISTS ${AttendanceControlData.Clase.TABLE_NAME}(
            ${AttendanceControlData.Clase.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${AttendanceControlData.Clase.COLUMN_NAME_CLASS_DATE} TEXT DEFAULT (datetime('now','localtime')),
            ${AttendanceControlData.Clase.COLUMN_NAME_CLASS_CREATE} TEXT DEFAULT (datetime('now','localtime')),
            ${AttendanceControlData.Clase.COLUMN_NAME_CODE} TEXT,
            
            ${AttendanceControlData.Clase.COLUMN_NAME_MATERIA_ID} INTEGER NOT NULL,
            ${AttendanceControlData.Clase.COLUMN_NAME_GRUPO_ID} INTEGER,
            ${AttendanceControlData.Clase.COLUMN_NAME_HORARIO_ID} INTEGER NOT NULL,
            
            FOREIGN KEY(${AttendanceControlData.Clase.COLUMN_NAME_MATERIA_ID}) REFERENCES ${AttendanceControlData.Materia.TABLE_NAME}(${AttendanceControlData.Materia.COLUMN_NAME_ID}),
            
            FOREIGN KEY(${AttendanceControlData.Clase.COLUMN_NAME_GRUPO_ID}) REFERENCES ${AttendanceControlData.Grupo.TABLE_NAME}(${AttendanceControlData.Grupo.COLUMN_NAME_ID}),
            
            FOREIGN KEY(${AttendanceControlData.Clase.COLUMN_NAME_HORARIO_ID}) REFERENCES ${AttendanceControlData.Horario.TABLE_NAME}(${AttendanceControlData.Horario.COLUMN_NAME_ID})
            
            );
        """

        private const val SQL_CREATE_DETALLE_CLASES="""
            CREATE TABLE IF NOT EXISTS ${AttendanceControlData.DetalleClase.TABLE_NAME}(
            ${AttendanceControlData.DetalleClase.COLUMN_NAME_CLASE_ID} INTEGER NOT NULL,
            ${AttendanceControlData.DetalleClase.COLUMN_NAME_ALUMNO_ID} INTEGER NOT NULL,
            ${AttendanceControlData.DetalleClase.COLUMN_NAME_CODE} INTEGER NOT NULL,
            ${AttendanceControlData.DetalleClase.COLUMN_NAME_MARK_DATE} TEXT DEFAULT (datetime('now','localtime')),
            
            PRIMARY KEY(${AttendanceControlData.DetalleClase.COLUMN_NAME_CLASE_ID},${AttendanceControlData.DetalleClase.COLUMN_NAME_ALUMNO_ID}),
            
            FOREIGN KEY(${AttendanceControlData.DetalleClase.COLUMN_NAME_CLASE_ID}) REFERENCES ${AttendanceControlData.Clase.TABLE_NAME}(${AttendanceControlData.Clase.COLUMN_NAME_ID}),
            FOREIGN KEY(${AttendanceControlData.DetalleClase.COLUMN_NAME_ALUMNO_ID}) REFERENCES ${AttendanceControlData.Alumno.TABLE_NAME}(${AttendanceControlData.Alumno.COLUMN_NAME_ID})
            
            );
        """

    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_MATERIAS)
        db.execSQL(SQL_CREATE_GRUPOS)
        db.execSQL(SQL_CREATE_HORARIOS)
        db.execSQL(SQL_CREATE_ALUMNOS)
        db.execSQL(SQL_CREATE_CLASES)
        db.execSQL(SQL_CREATE_DETALLE_CLASES)
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
        if (oldVersion < 2) {
            db.execSQL(SQL_CREATE_MATERIAS)
        }
        if (oldVersion < 3) {
            db.execSQL(SQL_CREATE_GRUPOS)
        }
        if (oldVersion < 4) {
            db.execSQL(SQL_CREATE_HORARIOS)
        }
        if (oldVersion < 5) {
            db.execSQL(SQL_CREATE_HORARIOS)
        }
        if (oldVersion < 6) {
            db.execSQL(SQL_CREATE_ALUMNOS)
        }
        if (oldVersion < 7) {
            db.execSQL(SQL_CREATE_CLASES)
        }
        if (oldVersion < 8) {
            db.execSQL(SQL_CREATE_DETALLE_CLASES)
        }
    }

}

