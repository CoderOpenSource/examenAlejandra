package com.example.myapplication.rutina.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.rutina.data.models.RutinaEjercicio;

import java.util.List;

public class RutinaEjercicioAdapter extends RecyclerView.Adapter<RutinaEjercicioAdapter.RutinaEjercicioViewHolder> {

    private List<RutinaEjercicio> rutinaEjercicioList;

    public RutinaEjercicioAdapter(List<RutinaEjercicio> rutinaEjercicioList) {
        this.rutinaEjercicioList = rutinaEjercicioList;
    }

    @NonNull
    @Override
    public RutinaEjercicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rutina_ejercicio, parent, false);
        return new RutinaEjercicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RutinaEjercicioViewHolder holder, int position) {
        RutinaEjercicio rutinaEjercicio = rutinaEjercicioList.get(position);
        holder.txtEjercicioNombre.setText("Ejercicio ID: " + rutinaEjercicio.getIdEjercicio()); // Ideally, you would display the name of the exercise instead
        holder.txtRepeticiones.setText("Repeticiones: " + rutinaEjercicio.getRepeticiones());
        holder.txtSeries.setText("Series: " + rutinaEjercicio.getSeries());
        holder.txtDescanso.setText("Descanso: " + rutinaEjercicio.getDescanso() + " segundos");
    }

    @Override
    public int getItemCount() {
        return rutinaEjercicioList.size();
    }

    public static class RutinaEjercicioViewHolder extends RecyclerView.ViewHolder {

        TextView txtEjercicioNombre, txtRepeticiones, txtSeries, txtDescanso;

        public RutinaEjercicioViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEjercicioNombre = itemView.findViewById(R.id.txt_ejercicio_nombre);
            txtRepeticiones = itemView.findViewById(R.id.txt_repeticiones);
            txtSeries = itemView.findViewById(R.id.txt_series);
            txtDescanso = itemView.findViewById(R.id.txt_descanso);
        }
    }
}
