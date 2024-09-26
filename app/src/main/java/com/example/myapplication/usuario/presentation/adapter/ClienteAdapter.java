package com.example.myapplication.usuario.presentation.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.usuario.data.models.Cliente;

import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    private List<Cliente> clientes;
    private Context context;
    private OnItemClickListener onItemClickListener;

    // Interface for click handling
    public interface OnItemClickListener {
        void onEditClick(Cliente cliente);
        void onDeleteClick(Cliente cliente);
    }


    public ClienteAdapter(Context context, List<Cliente> clientes, OnItemClickListener listener) {
        this.context = context;
        this.clientes = clientes;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cliente, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente cliente = clientes.get(position);
        holder.txtNombre.setText(cliente.getNombre() + " " + cliente.getApellidoPaterno() + " " + cliente.getApellidoMaterno());
        holder.txtCelular.setText(cliente.getCelular());

        // Set click listeners for edit and delete buttons
        holder.btnEdit.setOnClickListener(v -> onItemClickListener.onEditClick(cliente));
        holder.btnDelete.setOnClickListener(v -> onItemClickListener.onDeleteClick(cliente));
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public static class ClienteViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtCelular;
        ImageButton btnEdit, btnDelete;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txt_nombre);
            txtCelular = itemView.findViewById(R.id.txt_celular);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public void updateData(List<Cliente> newClientes) {
        this.clientes = newClientes;
        notifyDataSetChanged();
    }
}
