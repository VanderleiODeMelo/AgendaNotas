package com.alura.nota.ui.activity;

import static com.alura.nota.constantes.ConstantesActivitys.CHAVE_NOTA;
import static com.alura.nota.constantes.ConstantesActivitys.CHAVE_POSICAO;
import static com.alura.nota.constantes.ConstantesActivitys.POSICAO_INVALIDA;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.alura.nota.R;
import com.alura.nota.model.Nota;

public class InserirNotaActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Cadastrar nota";
    public static final String BOTAO_VOLTAR = "Voltar";
    private EditText idCampoTitulo;
    private EditText idCampoDescricao;
    private int posicaoValida = POSICAO_INVALIDA;
    private Nota nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserir_nota);
        setTitle(TITULO_APPBAR);

        botaoVoltar();
        inicializarCampos();
        carregandoInformacoes();
    }

    private void carregandoInformacoes() {

        Intent dadosNotaEditar = getIntent();
        if (dadosNotaEditar != null) {

            if (dadosNotaEditar.hasExtra(CHAVE_NOTA) && dadosNotaEditar.hasExtra(CHAVE_POSICAO)) {

                nota = dadosNotaEditar.getParcelableExtra(CHAVE_NOTA);
                posicaoValida = dadosNotaEditar.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
                idCampoTitulo.setText(nota.getTitulo());
                idCampoDescricao.setText(nota.getDescricao());

            } else {

                nota = new Nota();
            }

        }
    }

    private void botaoVoltar() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeActionContentDescription(BOTAO_VOLTAR);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.inserir_nota_menu_salvar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        int itemIdMenuSalvar = item.getItemId();
        if (itemIdMenuSalvar == R.id.menu_salvar) {

            criarNota();
            if (nota.temIdValido()) {

                Intent vaiParaListaNotasActivity = new Intent(InserirNotaActivity.this, ListaNotasActivity.class);
                vaiParaListaNotasActivity.putExtra(CHAVE_NOTA, nota);
                vaiParaListaNotasActivity.putExtra(CHAVE_POSICAO, posicaoValida);
                setResult(RESULT_OK, vaiParaListaNotasActivity);
                finish();

            } else {

                Intent vaiParaListaNotasActivity = new Intent(InserirNotaActivity.this, ListaNotasActivity.class);
                vaiParaListaNotasActivity.putExtra(CHAVE_NOTA, nota);
                setResult(RESULT_OK, vaiParaListaNotasActivity);

                finish();

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    private void criarNota() {

        String titulo = idCampoTitulo.getText().toString();
        String descricao = idCampoDescricao.getText().toString();
        nota.setTitulo(titulo);
        nota.setDescricao(descricao);
    }

    private void inicializarCampos() {
        idCampoTitulo = findViewById(R.id.inserir_nota_campo_titulo);
        idCampoDescricao = findViewById(R.id.inserir_nota_campo_descricao);
    }
}