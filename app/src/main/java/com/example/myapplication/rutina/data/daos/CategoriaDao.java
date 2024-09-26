package com.example.myapplication.rutina.data.daos;

// no te olvides de los try caths
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.rutina.data.models.Categoria;
import com.example.myapplication.database.GymBroDatabaseHelper;

public class CategoriaDao {
    private SQLiteDatabase db;

    public CategoriaDao(Context context) {
        GymBroDatabaseHelper dbHelper = new GymBroDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Insertar Categoria
    public long insertarCategoria(Categoria categoria) {
        ContentValues values = new ContentValues();
        values.put("nombre", categoria.getNombre());
        values.put("descripcion", categoria.getDescripcion());

        return db.insert("Categoria", null, values);
    }

    // Obtener todas las categorías (Devuelve un cursor en lugar de una lista)
    public Cursor obtenerTodasLasCategorias() {
        return db.query("Categoria", null, null, null, null, null, null);
    }

    // Actualizar Categoria
    public int actualizarCategoria(Categoria categoria) {
        ContentValues values = new ContentValues();
        values.put("nombre", categoria.getNombre());
        values.put("descripcion", categoria.getDescripcion());

        return db.update("Categoria", values, "id_categoria = ?", new String[]{String.valueOf(categoria.getIdCategoria())});
    }

    // Eliminar Categoria
    public void eliminarCategoria(int idCategoria) {
        db.delete("Categoria", "id_categoria = ?", new String[]{String.valueOf(idCategoria)});
    }
    // Obtener una categoría por ID
    public Categoria obtenerCategoriaPorId(int idCategoria) {
        Categoria categoria = null;
        Cursor cursor = null;

        try {
            cursor = db.query("Categoria", null, "id_categoria = ?", new String[]{String.valueOf(idCategoria)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
                categoria = new Categoria(idCategoria, nombre, descripcion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return categoria;
    }

}
