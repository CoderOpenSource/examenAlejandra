package com.example.myapplication.rutina.business.repository;

import android.content.Context;
import android.database.Cursor;

import com.example.myapplication.rutina.data.daos.CategoriaDao;
import com.example.myapplication.rutina.data.models.Categoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriaRepository {
    private CategoriaDao categoriaDao;
    private Context context;

    public CategoriaRepository(Context context) {
        this.context = context;
        categoriaDao = new CategoriaDao(context);
    }

    // Insertar una categoría
    public long insertarCategoria(Categoria categoria) {
        return categoriaDao.insertarCategoria(categoria);
    }

    // Obtener todas las categorías
// Obtener todas las categorías y convertir el Cursor a una lista
    public List<Categoria> obtenerTodasLasCategorias() {
        List<Categoria> listaCategorias = new ArrayList<>();
        Cursor cursor = categoriaDao.obtenerTodasLasCategorias();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idCategoria = cursor.getInt(cursor.getColumnIndexOrThrow("id_categoria"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));

                Categoria categoria = new Categoria(idCategoria, nombre, descripcion);
                listaCategorias.add(categoria);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return listaCategorias;
    }
    // Actualizar una categoría
    public int actualizarCategoria(Categoria categoria) {
        return categoriaDao.actualizarCategoria(categoria);
    }

    // Eliminar una categoría por ID
    public void eliminarCategoria(int idCategoria) {
        categoriaDao.eliminarCategoria(idCategoria);
    }

    // Obtener una categoría por ID
    public Categoria obtenerCategoriaPorId(int idCategoria) {
        return categoriaDao.obtenerCategoriaPorId(idCategoria);
    }

}
