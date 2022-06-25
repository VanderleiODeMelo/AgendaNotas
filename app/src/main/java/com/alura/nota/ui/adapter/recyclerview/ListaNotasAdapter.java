package com.alura.nota.ui.adapter.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alura.nota.R;
import com.alura.nota.model.Nota;
import com.alura.nota.util.DataUtil;

import java.util.Calendar;
import java.util.List;

public class ListaNotasAdapter extends RecyclerView.Adapter<ListaNotasAdapter.NotaViewHolder> {
    private final Context context;
    private final List<Nota> listaNotas;
    private static OnItemClickListener onItemClickListener;
    private  int  posicao = -1;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        ListaNotasAdapter.onItemClickListener = onItemClickListener;
    }

    public ListaNotasAdapter(Context context, List<Nota> listaNotas) {
        this.context = context;
        this.listaNotas = listaNotas;
    }


    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_nota, parent, false);


        return new NotaViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        Nota nota = listaNotas.get(position);
        holder.vincular(nota);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                setPosicao(holder.getAdapterPosition());


                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void atualizar(List<Nota> listaAtualizada) {

        this.listaNotas.clear();
        this.listaNotas.addAll(listaAtualizada);
        notifyDataSetChanged();
    }

    public void editar(Nota nota, int posicao) {

        listaNotas.set(posicao, nota);
        notifyItemChanged(posicao);
    }

    public void remover(int posicao) {
        listaNotas.remove(posicao);
        notifyItemRemoved(posicao);
    }

    static class NotaViewHolder extends RecyclerView.ViewHolder {

        private final TextView idItemNotaTitulo;
        private final TextView idItemNotaDescricao;
        private Nota nota;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);

            idItemNotaTitulo = itemView.findViewById(R.id.item_nota_titulo);
            idItemNotaDescricao = itemView.findViewById(R.id.item_nota_descricao);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onItemClickListener.onClick(nota, getAdapterPosition());

                }
            });
            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu contextMenu, View view,
                                                ContextMenu.ContextMenuInfo contextMenuInfo) {

                    contextMenu.add(contextMenu.NONE, R.id.menu_remover, contextMenu.NONE, "Remover");

                }
            });
        }

        public void vincular(Nota nota) {

            this.nota = nota;
            idItemNotaTitulo.setText(nota.getTitulo().concat(" - ").concat(DataUtil.formatadorData(Calendar.getInstance())));
            idItemNotaDescricao.setText(nota.getDescricao());


        }
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }
}
