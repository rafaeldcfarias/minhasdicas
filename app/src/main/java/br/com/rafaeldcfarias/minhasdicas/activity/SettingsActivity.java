package br.com.rafaeldcfarias.minhasdicas.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import br.com.rafaeldcfarias.minhasdicas.R;
import br.com.rafaeldcfarias.minhasdicas.infra.Preferences;
import br.com.rafaeldcfarias.minhasdicas.receiver.DicaReceiver;

public class SettingsActivity extends AppCompatActivity {

    private EditText editText;
    private ToggleButton toggleButtonAtivado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText = (EditText) findViewById(R.id.editTextFrequencia);
        toggleButtonAtivado = (ToggleButton) findViewById(R.id.toggleButtonAtivado);
        Preferences.initialize(getApplicationContext());
        if (Preferences.isInitializated()) {
            editText.setText(String.valueOf(Preferences.getInstance().getInt("frequencia", 30)));
            toggleButtonAtivado.setChecked(Preferences.getInstance().getBoolean("ativado", false));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            int frequencia = Integer.valueOf(editText.getText().toString());
            SharedPreferences.Editor editor = Preferences.getInstance().edit();
            editor.putInt("frequencia", frequencia);
            Intent intent = new Intent(getApplicationContext(), DicaReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
            AlarmManager manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            manager.cancel(pendingIntent);
            if (toggleButtonAtivado.isChecked()) {
                long fator = (frequencia * 60 * 1000);
                long triggerAlarm = System.currentTimeMillis() + fator;
                manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAlarm, fator, pendingIntent);
            }
            editor.putBoolean("ativado", toggleButtonAtivado.isChecked());
            editor.commit();

            Toast.makeText(getApplicationContext(), R.string.msg_configuracoes_aplicadas, Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
