package br.com.rafaeldcfarias.minhasdicas.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import br.com.rafaeldcfarias.minhasdicas.R;
import br.com.rafaeldcfarias.minhasdicas.adapter.DicaAdapter;
import br.com.rafaeldcfarias.minhasdicas.infra.DBHelper;
import br.com.rafaeldcfarias.minhasdicas.infra.Preferences;
import br.com.rafaeldcfarias.minhasdicas.service.DicaService;

public class DicasActivity extends AppCompatActivity {

    private ListView listViewDicas;
    private DicaAdapter dicaAdapter;
    private DicaService dicaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dicas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.buttonAdicionar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DicasActivity.this, CadastroActivity.class));
            }
        });
        DBHelper.setCONTEXT(getApplicationContext());
        Preferences.initialize(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        dicaService = new DicaService();
        listViewDicas = (ListView) findViewById(R.id.listViewCartoes);
        dicaAdapter = new DicaAdapter(DicasActivity.this, null, dicaService);
        TextView tvEmpty = new TextView(getApplicationContext());
        tvEmpty.setText(getString(R.string.zero_dica_cadastrado));
        listViewDicas.setEmptyView(tvEmpty);
        listViewDicas.setAdapter(dicaAdapter);
        listViewDicas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(DicasActivity.this, CadastroActivity.class).putExtra("dica_id", id));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dicas, menu);
        // Associate searchable configuration with the SearchView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager searchManager =
                    (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView =
                    (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(DicasActivity.this, SettingsActivity.class));
        }
        if (id == R.id.action_creditos) {
            startActivity(new Intent(DicasActivity.this, CreditosActivity.class));
        }
        if (id == R.id.search)
            onSearchRequested();

        return super.onOptionsItemSelected(item);
    }
}
