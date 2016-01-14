package br.com.rafaeldcfarias.minhasdicas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.rafaeldcfarias.minhasdicas.R;
import br.com.rafaeldcfarias.minhasdicas.infra.DBHelper;
import br.com.rafaeldcfarias.minhasdicas.infra.Preferences;
import br.com.rafaeldcfarias.minhasdicas.model.Dica;
import br.com.rafaeldcfarias.minhasdicas.service.DicaService;

public class CadastroActivity extends AppCompatActivity {

    private DicaService dicaService;
    private Dica dica;
    private EditText editTextConteudo;
    private EditText editTextTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DBHelper.setCONTEXT(getApplicationContext());
        Preferences.initialize(getApplicationContext());
        dicaService = new DicaService();
        editTextConteudo = (EditText) findViewById(R.id.editTextConteudo);
        editTextTitulo = (EditText) findViewById(R.id.editTextTitulo);
        dica = new Dica();
        if (getIntent().hasExtra("dica_id")) {
            dica.setId(getIntent().getLongExtra("dica_id", 0L));
            dica = dicaService.buscar(dica.getId());
            editTextConteudo.setText(dica.getConteudo());
            editTextTitulo.setText(dica.getTitulo());
        }
    }

    public String validarDica(long id) {
        String mensagens = null;

        if (dica.getTitulo().isEmpty() && dica.getConteudo().isEmpty()) {
            return "";
        }
        if (dica.getTitulo().isEmpty() && !dica.getConteudo().isEmpty()) {
            mensagens = getString(R.string.msg_dica_sem_titulo);
        } else if (!dica.getTitulo().isEmpty() && dica.getConteudo().isEmpty()) {
            mensagens = getString(R.string.msg_dica_sem_conteudo);
        } else if (dicaService.buscarPorTitulo(dica.getTitulo()) != null && id == 0) {
            mensagens = getString(R.string.msg_dica_ja_existe);
        }
        return mensagens;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            dica.setTitulo(editTextTitulo.getText().toString());
            dica.setConteudo(editTextConteudo.getText().toString());
            String mensagem = null;
            if ((mensagem = validarDica(dica.getId())) == null) {
                long result = dicaService.salvar(dica);
                if (result == -1) {
                    mensagem = getString(R.string.msg_problema_salvar_dica);
                } else {
                    mensagem = getString(R.string.msg_dica_salva);
                    if (dica.getId() != 0) {
                        mensagem = getString(R.string.msg_dica_atualizada);
                    }
                }
            }
            if (!mensagem.isEmpty()) {
                Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
