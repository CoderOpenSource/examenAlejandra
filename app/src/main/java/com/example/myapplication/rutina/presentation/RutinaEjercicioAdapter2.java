package com.example.myapplication.rutina.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.rutina.data.models.RutinaEjercicio;

import java.util.ArrayList;
import java.util.List;

public class RutinaEjercicioAdapter2 extends RecyclerView.Adapter<RutinaEjercicioAdapter2.RutinaEjercicioViewHolder> {

    private List<RutinaEjercicio> rutinaEjercicioList;
    private List<RutinaEjercicio> ejerciciosParaEliminar;  // Lista para marcar ejercicios que se eliminarán de la base de datos
    private OnRutinaEjercicioClickListener listener;

    public interface OnRutinaEjercicioClickListener {
        void onEjercicioDeleteClick(RutinaEjercicio rutinaEjercicio);
    }

    public RutinaEjercicioAdapter2(List<RutinaEjercicio> rutinaEjercicioList, OnRutinaEjercicioClickListener listener) {
        this.rutinaEjercicioList = rutinaEjercicioList;
        this.listener = listener;
        this.ejerciciosParaEliminar = new ArrayList<>();
    }

    @NonNull
    @Override
    public RutinaEjercicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rutina_ejercicio2, parent, false);
        return new RutinaEjercicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RutinaEjercicioViewHolder holder, int position) {
        RutinaEjercicio rutinaEjercicio = rutinaEjercicioList.get(position);

        holder.txtEjercicioNombre.setText("Ejercicio ID: " + rutinaEjercicio.getIdEjercicio()); // Deberías obtener el nombre del ejercicio
        holder.txtRepeticiones.setText("Repeticiones: " + rutinaEjercicio.getRepeticiones());
        holder.txtSeries.setText("Series: " + rutinaEjercicio.getSeries());
        holder.txtDescanso.setText("Descanso: " + rutinaEjercicio.getDescanso() + " segundos");

        // Botón para eliminar ejercicio
        holder.btnDeleteEjercicio.setOnClickListener(v -> {
            listener.onEjercicioDeleteClick(rutinaEjercicio);
        });
    }

    @Override
    public int getItemCount() {
        return rutinaEjercicioList.size();
    }

    public static class RutinaEjercicioViewHolder extends RecyclerView.ViewHolder {
        TextView txtEjercicioNombre, txtRepeticiones, txtSeries, txtDescanso;
        Button btnDeleteEjercicio;

        public RutinaEjercicioViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEjercicioNombre = itemView.findViewById(R.id.txt_ejercicio_nombre);
            txtRepeticiones = itemView.findViewById(R.id.txt_repeticiones);
            txtSeries = itemView.findViewById(R.id.txt_series);
            txtDescanso = itemView.findViewById(R.id.txt_descanso);
            btnDeleteEjercicio = itemView.findViewById(R.id.btn_delete_ejercicio);
        }
    }

    // Método para eliminar el ejercicio de la lista
    public void eliminarEjercicio(RutinaEjercicio rutinaEjercicio) {
        rutinaEjercicioList.remove(rutinaEjercicio);
        notifyDataSetChanged();
    }

    public List<RutinaEjercicio> getEjerciciosParaEliminar() {
        return ejerciciosParaEliminar;
    }
}

