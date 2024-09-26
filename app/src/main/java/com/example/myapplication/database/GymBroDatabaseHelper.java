package com.example.myapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GymBroDatabaseHelper extends SQLiteOpenHelper {

    // Nombre de la base de datos
    private static final String DATABASE_NAME = "gymbro.db";
    // Versión de la base de datos
    private static final int DATABASE_VERSION = 1;

    public GymBroDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // Habilitar claves foráneas
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear las tablas
        createTables(db);
    }

    private void createTables(SQLiteDatabase db) {
        // Crear tabla Cliente
        String CREATE_TABLE_CLIENTE = "CREATE TABLE Cliente (" +
                "id_cliente INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "apellido_paterno TEXT, " +
                "apellido_materno TEXT, " +
                "celular TEXT, " +
                "email TEXT, " +
                "edad INTEGER, " +
                "altura REAL, " +
                "peso REAL)";

        // Crear tabla Categoria
        String CREATE_TABLE_CATEGORIA = "CREATE TABLE Categoria (" +
                "id_categoria INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "descripcion TEXT)";

        // Crear tabla Ejercicio
        String CREATE_TABLE_EJERCICIO = "CREATE TABLE Ejercicio (" +
                "id_ejercicio INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "posicion TEXT, " +
                "ejecucion TEXT, " +
                "equipo TEXT, " +
                "multimedia TEXT, " +
                "id_categoria INTEGER, " +
                "FOREIGN KEY(id_categoria) REFERENCES Categoria(id_categoria))";

        // Crear tabla Rutina
        String CREATE_TABLE_RUTINA = "CREATE TABLE Rutina (" +
                "id_rutina INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_cliente INTEGER, " +
                "fecha_inicio TEXT NOT NULL, " +
                "fecha_final TEXT, " +
                "descripcion TEXT, " +
                "FOREIGN KEY(id_cliente) REFERENCES Cliente(id_cliente))";

        // Crear tabla RutinaEjercicio
        String CREATE_TABLE_RUTINA_EJERCICIO = "CREATE TABLE RutinaEjercicio (" +
                "id_rutina INTEGER, " +
                "id_ejercicio INTEGER, " +
                "repeticiones INTEGER, " +
                "series INTEGER, " +
                "descanso INTEGER, " +
                "PRIMARY KEY (id_rutina, id_ejercicio), " +
                "FOREIGN KEY(id_rutina) REFERENCES Rutina(id_rutina) ON DELETE CASCADE, " +
                "FOREIGN KEY(id_ejercicio) REFERENCES Ejercicio(id_ejercicio))";

        // Ejecutar las creaciones de tablas
        db.execSQL(CREATE_TABLE_CLIENTE);
        db.execSQL(CREATE_TABLE_CATEGORIA);
        db.execSQL(CREATE_TABLE_EJERCICIO);
        db.execSQL(CREATE_TABLE_RUTINA);
        db.execSQL(CREATE_TABLE_RUTINA_EJERCICIO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS RutinaEjercicio");
        db.execSQL("DROP TABLE IF EXISTS Rutina");
        db.execSQL("DROP TABLE IF EXISTS Ejercicio");
        db.execSQL("DROP TABLE IF EXISTS Categoria");
        db.execSQL("DROP TABLE IF EXISTS Cliente");
        onCreate(db);
    }
}
