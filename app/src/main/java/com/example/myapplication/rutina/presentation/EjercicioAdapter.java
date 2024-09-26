package com.example.myapplication.rutina.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.rutina.data.models.Ejercicio;

import java.util.List;

public class EjercicioAdapter extends RecyclerView.Adapter<EjercicioAdapter.EjercicioViewHolder> {

    private List<Ejercicio> ejercicioList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Ejercicio ejercicio);
        void onDeleteClick(Ejercicio ejercicio);
    }

    public EjercicioAdapter(List<Ejercicio> ejercicioList, OnItemClickListener listener) {
        this.ejercicioList = ejercicioList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EjercicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ejercicio, parent, false);
        return new EjercicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EjercicioViewHolder holder, int position) {
        Ejercicio ejercicio = ejercicioList.get(position);
        holder.tvNombre.setText(ejercicio.getNombre());
        holder.tvEjecucion.setText(ejercicio.getEjecucion());

        // Acción para el botón de editar
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(ejercicio));

        // Acción para el botón de eliminar
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(ejercicio));
    }

    @Override
    public int getItemCount() {
        return ejercicioList.size();
    }

    public static class EjercicioViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre, tvEjecucion;
        ImageButton btnEdit, btnDelete;

        public EjercicioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_nombre_ejercicio);
            tvEjecucion = itemView.findViewById(R.id.tv_ejecucion_ejercicio);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    // Método para actualizar los datos del adaptador
    public void updateData(List<Ejercicio> nuevosEjercicios) {
        this.ejercicioList = nuevosEjercicios;
        notifyDataSetChanged(); // Notificar al RecyclerView que los datos han cambiado
    }
}
