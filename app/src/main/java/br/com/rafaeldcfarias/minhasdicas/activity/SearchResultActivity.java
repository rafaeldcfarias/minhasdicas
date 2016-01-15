package br.com.rafaeldcfarias.minhasdicas.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Collections;

import br.com.rafaeldcfarias.minhasdicas.R;
import br.com.rafaeldcfarias.minhasdicas.adapter.DicaAdapter;
import br.com.rafaeldcfarias.minhasdicas.infra.DBHelper;
import br.com.rafaeldcfarias.minhasdicas.model.Dica;
import br.com.rafaeldcfarias.minhasdicas.service.DicaService;

public class SearchResultActivity extends AppCompatActivity {

    private ListView dicasResultado;
    private DicaAdapter dicaAdapter;
    private DicaService dicaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DBHelper.setCONTEXT(getApplicationContext());
        dicaService = new DicaService();
        dicaAdapter = new DicaAdapter(getApplicationContext(), Collections.<Dica>emptyList(), dicaService);
        dicasResultado = (ListView) findViewById(R.id.listViewCartoes);
        dicasResultado.setAdapter(dicaAdapter);
        dicasResultado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(SearchResultActivity.this, CadastroActivity.class).putExtra("dica_id", id));
            }
        });
        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            dicaAdapter.setDicas(dicaService.buscarPorTituloOuConteudo(query));
            dicaAdapter.notifyDataSetChanged();
        }
    }

}
