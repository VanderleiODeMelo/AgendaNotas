package com.alura.nota.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.alura.nota.R;
import com.alura.nota.asynctask.BaseAsyncTask;
import com.alura.nota.dao.NotaDao;
import com.alura.nota.model.Nota;
import com.alura.nota.ui.adapter.recyclerview.ListaNotasAdapter;

import java.util.List;

public class DialogPersonalizado {


    private final Context context;
    private final ListaNotasAdapter adapter;
    private final List<Nota> listaNotas;
    private final NotaDao notaDao;
    private final ViewGroup layout_dialog_container;

    public DialogPersonalizado(Context context,
                               ListaNotasAdapter adapter,
                               List<Nota> listaNotas,
                               NotaDao notaDao,
                               ViewGroup layout_dialog_container) {
        this.context = context;
        this.adapter = adapter;
        this.listaNotas = listaNotas;
        this.notaDao = notaDao;
        this.layout_dialog_container = layout_dialog_container;
    }

    public void criarDialogPersonalizadoRemover() {

        //1° - utilizar estilo personalizado que criamos lá no themes.xml
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogPersonalizado);
        //2° - inflar o dialog personalizado
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.layout_remover_dialog, layout_dialog_container);
        //3° - introduzindo o style(estilo) na view
        builder.setView(viewCriada);

        colocarTextosNoDialogPersonalizado(builder, viewCriada);
    }


    private void colocarTextosNoDialogPersonalizado(AlertDialog.Builder builder, View viewCriada) {


        colocarTituloRemover(viewCriada);
        colocarMensagemRemover(viewCriada);
        colocarPalavraSimBotao(viewCriada);
        colocarPalavraNaoBotao(viewCriada);
        colocarIconeLixeira(viewCriada);

        criarAlertDialogPersonalizado(builder, viewCriada);
    }

    private void criarAlertDialogPersonalizado(AlertDialog.Builder builder, View viewCriada) {

        //4° criar esse alertDialog que foi feito com todos os argumentos
        AlertDialog alertDialog = builder.create();

        criarListenersBotoes(viewCriada, alertDialog);
    }

    private void criarListenersBotoes(View viewCriada, AlertDialog alertDialog) {

        criarListenerBotaoSim(viewCriada, alertDialog);
        criarListenerBotaoNao(viewCriada, alertDialog);
        recuperarJanelaParaAtividade(alertDialog);
    }

    private void recuperarJanelaParaAtividade(AlertDialog alertDialog) {

        if (alertDialog.getWindow() != null) {

            //aqui vamos ter que setar a cor do drawable de fundo
            //vamos criar um novo colorDrawable com a cor específica
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        }
        //aqui estou dando um start para o meu drawable aparecer
        alertDialog.show();
    }

    private void criarListenerBotaoNao(View viewCriada, AlertDialog alertDialog) {

        viewCriada.findViewById(R.id.dialog_botao_nao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });
    }

    private void criarListenerBotaoSim(@NonNull View viewCriada, AlertDialog alertDialog) {

        viewCriada.findViewById(R.id.dialog_botao_sim).
                setOnClickListener(view -> {

                    alertDialog.dismiss();
                    int posicao = adapter.getPosicao();
                    Nota nota = listaNotas.get(posicao);
                    removerNota(posicao, nota);
                });
    }

    private void removerNota(int posicao, Nota nota) {

        new BaseAsyncTask<Nota>(() -> {

            notaDao.remover(nota);

            return null;
        }, resultado ->
                adapter.remover(posicao)).execute();

    }

    private void colocarIconeLixeira(View viewCriada) {

        ((ImageView) viewCriada.findViewById(R.id.dialog_imagem_icone_remover))
                .setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_action_deletar));
    }

    private void colocarPalavraNaoBotao(View viewCriada) {
        ((Button) viewCriada.findViewById(R.id.dialog_botao_nao))
                .setText(context.getResources().getString(R.string.botao_não));
    }

    private void colocarPalavraSimBotao(View viewCriada) {
        ((Button) viewCriada.findViewById(R.id.dialog_botao_sim))
                .setText(R.string.botao_sim);
    }

    private void colocarMensagemRemover(View viewCriada) {
        ((TextView) viewCriada.findViewById(R.id.dialog_texto_mensagem))
                .setText(context.getResources().getString(R.string.mensagem_remover));
    }

    private void colocarTituloRemover(View viewCriada) {

        ((TextView) viewCriada.findViewById(R.id.dialog_texto_titulo))
                .setText(context.getResources().getString(R.string.palavra_remover_dialog));
    }
}
