package br.com.rafaeldcfarias.minhasdicas.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import br.com.rafaeldcfarias.minhasdicas.R;
import br.com.rafaeldcfarias.minhasdicas.activity.DicasActivity;
import br.com.rafaeldcfarias.minhasdicas.activity.CadastroActivity;
import br.com.rafaeldcfarias.minhasdicas.activity.SettingsActivity;
import br.com.rafaeldcfarias.minhasdicas.infra.DBHelper;
import br.com.rafaeldcfarias.minhasdicas.model.Dica;
import br.com.rafaeldcfarias.minhasdicas.service.DicaService;

/**
 * Created by rk on 26/06/2015.
 */
public class DicaReceiver extends BroadcastReceiver {

    private static Queue<Dica> dicas = new LinkedList();
    private DicaService dicaService;
    private static final int MAXDICAS = 6;

    @Override
    public void onReceive(Context context, Intent intent) {
        DBHelper.setCONTEXT(context);
        dicaService = new DicaService();
        Random random = new Random();
        Dica sorteado = new Dica();
        sorteado.setId((random.nextInt(((int) dicaService.count())) + 1));
        Log.d("sorteado", sorteado.toString());
        // TODO: 02/01/2016 testar se existe pelo menos uma dica cadastrada
        sorteado = dicaService.buscar(sorteado.getId());
        dicas.add(sorteado);
        if (dicas.size() == MAXDICAS) {
            dicas.remove();
        }

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(context.getString(R.string.label_dicas));
        for (Dica dica : dicas) {
            inboxStyle.addLine(dica.getTitulo() + " - " + dica.getConteudo());
        }
        Intent resultIntent = new Intent(context, DicasActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
        Intent resultIntentSettings = new Intent(context, SettingsActivity.class);
        PendingIntent pendingIntentSettings = PendingIntent.getActivity(context, 0, resultIntentSettings, 0);
        Intent resultIntentCadastro = new Intent(context, CadastroActivity.class);
        PendingIntent pendingIntentCadastro = PendingIntent.getActivity(context, 0, resultIntentCadastro, 0);


        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setSmallIcon(R.drawable.ic_stat_bright4);
        builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        builder.setContentTitle(sorteado.getTitulo());
        builder.setContentText(sorteado.getConteudo());
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.addAction(R.drawable.ic_action_settings61, context.getString(R.string.title_activity_settings), pendingIntentSettings);
        builder.addAction(R.drawable.ic_content_add, context.getString(R.string.action_adicionar_dica), pendingIntentCadastro);
        builder.setStyle(inboxStyle);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        builder.setTicker(context.getString(R.string.label_dica) + sorteado.getId());
        manager.notify(1002, builder.build());
    }
}
