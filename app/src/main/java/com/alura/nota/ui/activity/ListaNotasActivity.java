package com.alura.nota.ui.activity;

import static com.alura.nota.constantes.ConstantesActivitys.CHAVE_NOTA;
import static com.alura.nota.constantes.ConstantesActivitys.CHAVE_POSICAO;
import static com.alura.nota.constantes.ConstantesActivitys.POSICAO_INVALIDA;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alura.nota.R;
import com.alura.nota.asynctask.BaseAsyncTask;
import com.alura.nota.dao.NotaDao;
import com.alura.nota.database.NotaDataBase;
import com.alura.nota.model.Nota;
import com.alura.nota.ui.adapter.recyclerview.ListaNotasAdapter;
import com.alura.nota.view.DialogPersonalizado;

import java.util.List;

public class ListaNotasActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Notas";

    private ActivityResultLauncher<Intent> activityResultLauncherInserirNotaSalvar;
    private ActivityResultLauncher<Intent> activityResultLauncherInserirNotaEditar;
    private ListaNotasAdapter adapter;
    private NotaDao notaDao;
    private List<Nota> listaNotas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        setTitle(TITULO_APPBAR);
        configurarInserirNota();

        NotaDataBase notaDataBase = NotaDataBase.getInstance(this);
        notaDao = notaDataBase.notaDao();
        configurarLista(notaDao);
        salvarNota();
        editarNota(notaDao);

    }

    private void editarNota(NotaDao notaDao) {

        activityResultLauncherInserirNotaEditar = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (verificandoSeNulo(result) && resultadoOk(result)) {

                    if (verificandoTemExtra(result) && verificarTemExtraPosicao(result)) {

                        editar(result, notaDao);

                    }
                }

            }
        });
    }

    private void editar(ActivityResult result, NotaDao notaDao) {

        Intent dadosNotaEditar = result.getData();

        assert dadosNotaEditar != null;
        Nota notaEditar = dadosNotaEditar.getParcelableExtra(CHAVE_NOTA);
        int posicaoValida = dadosNotaEditar.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);

        new BaseAsyncTask<Nota>(new BaseAsyncTask.ExecutaListener<Nota>() {
            @Override
            public Nota quandoExecuta() {

                notaDao.editar(notaEditar);

                return null;
            }
        }, resultado ->
                adapter.editar(notaEditar, posicaoValida)).execute();

    }

    private boolean verificarTemExtraPosicao(ActivityResult result) {
        assert result.getData() != null;
        return result.getData().hasExtra(CHAVE_POSICAO);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        DialogPersonalizado dialogPersonalizado = new DialogPersonalizado(ListaNotasActivity.this,
                adapter,
                listaNotas,
                notaDao,
                findViewById(R.id.layout_dialog_container));
        dialogPersonalizado.criarDialogPersonalizadoRemover();

        return super.onContextItemSelected(item);
    }

    private void salvarNota() {

        activityResultLauncherInserirNotaSalvar = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                //resultado salvar
                if (verificandoSeNulo(result) && resultadoOk(result)) {

                    if (verificandoTemExtra(result)) {

                        salvarNota(result);


                    }
                }
            }
        });
    }

    private void salvarNota(ActivityResult result) {

        Intent dadosNota = result.getData();
        assert result.getData() != null;
        Nota nota = dadosNota.getParcelableExtra(CHAVE_NOTA);

        //salvar
        new BaseAsyncTask<>(() -> {

            notaDao.salvar(nota);

            return notaDao.buscarTodasNotas();
        }, resultado ->
                adapter.atualizar(resultado)).execute();
    }

    private boolean verificandoTemExtra(ActivityResult result) {
        assert result.getData() != null;
        return result.getData().hasExtra(CHAVE_NOTA);
    }

    private boolean resultadoOk(ActivityResult result) {
        return result.getResultCode() == RESULT_OK;
    }

    private boolean verificandoSeNulo(ActivityResult result) {
        return result.getData() != null;
    }

    private void configurarLista(NotaDao notaDao) {

        configurarRecyclerView(notaDao);
    }

    private void configurarRecyclerView(NotaDao notaDao) {

        RecyclerView idRecyclerview = configurarLayoutManager();
        configurarAdapter(notaDao, idRecyclerview);
    }

    private void configurarAdapter(@NonNull NotaDao notaDao,
                                   @NonNull RecyclerView idRecyclerview) {

        new BaseAsyncTask<>(() -> {

            listaNotas = notaDao.buscarTodasNotas();

            return listaNotas;
        }, resultado -> {

            adapter = new ListaNotasAdapter(ListaNotasActivity.this, resultado);
            idRecyclerview.setAdapter(adapter);
            adapter.setOnItemClickListener((nota, posicao) -> {

                Intent vaiParaInserirNotaActivity = new Intent(ListaNotasActivity.this, InserirNotaActivity.class);

                vaiParaInserirNotaActivity.putExtra(CHAVE_NOTA, nota);
                vaiParaInserirNotaActivity.putExtra(CHAVE_POSICAO, posicao);
                activityResultLauncherInserirNotaEditar.launch(vaiParaInserirNotaActivity);


            });

        }).execute();
    }


    @NonNull
    private RecyclerView configurarLayoutManager() {
        RecyclerView idRecyclerview = findViewById(R.id.lista_notas_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        idRecyclerview.setLayoutManager(linearLayoutManager);
        return idRecyclerview;
    }

    private void configurarInserirNota() {

        TextView idInserirNota = findViewById(R.id.lista_notas_inserir_uma_nota);

        idInserirNota.setOnClickListener(view -> {
            Intent vaiParaInserirNotaActivity = new Intent(ListaNotasActivity.this, InserirNotaActivity.class);
            activityResultLauncherInserirNotaSalvar.launch(vaiParaInserirNotaActivity);

        });
    }

}