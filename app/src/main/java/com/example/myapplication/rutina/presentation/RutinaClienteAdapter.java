package com.example.myapplication.rutina.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.rutina.data.models.Rutina;
import java.util.List;

public class RutinaClienteAdapter extends RecyclerView.Adapter<RutinaClienteAdapter.RutinaViewHolder> {

    private List<Rutina> rutinas;
    private OnItemClickListener listener;

    public RutinaClienteAdapter(List<Rutina> rutinas, OnItemClickListener listener) {
        this.rutinas = rutinas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RutinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rutina_cliente, parent, false);
        return new RutinaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RutinaViewHolder holder, int position) {
        Rutina rutina = rutinas.get(position);
        holder.tvDescripcion.setText(rutina.getDescripcion());
        holder.tvFechaInicio.setText("Inicio: " + rutina.getFechaInicio());
        holder.tvFechaFinal.setText("Final: " + (rutina.getFechaFinal() != null ? rutina.getFechaFinal() : "N/A"));

        // Button to generate PDF
        holder.btnPdf.setOnClickListener(v -> listener.onGeneratePdfClick(rutina));

        // Delete button
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(rutina));
    }

    @Override
    public int getItemCount() {
        return rutinas.size();
    }

    public void updateData(List<Rutina> nuevasRutinas) {
        this.rutinas = nuevasRutinas;
        notifyDataSetChanged();
    }

    public static class RutinaViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescripcion, tvFechaInicio, tvFechaFinal;
        ImageButton btnPdf, btnDelete;

        public RutinaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescripcion = itemView.findViewById(R.id.tv_descripcion_rutina);
            tvFechaInicio = itemView.findViewById(R.id.tv_fecha_inicio_rutina);
            tvFechaFinal = itemView.findViewById(R.id.tv_fecha_final_rutina);
            btnPdf = itemView.findViewById(R.id.btn_pdf);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public interface OnItemClickListener {
        void onGeneratePdfClick(Rutina rutina);
        void onDeleteClick(Rutina rutina);
    }
}
