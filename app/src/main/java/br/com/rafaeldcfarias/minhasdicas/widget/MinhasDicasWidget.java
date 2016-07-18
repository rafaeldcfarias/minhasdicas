package br.com.rafaeldcfarias.minhasdicas.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import br.com.rafaeldcfarias.minhasdicas.R;
import br.com.rafaeldcfarias.minhasdicas.model.Dica;
import br.com.rafaeldcfarias.minhasdicas.service.DicaService;

/**
 * Implementation of App Widget functionality.
 */
public class MinhasDicasWidget extends AppWidgetProvider {

    public static final String ONCLICKSORTEIO = "onClickSorteio";

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.minhas_dicas_widget);
        DicaService dicaService = new DicaService();
        Dica dica = dicaService.sortearDica();
        if (dica != null) {
            views.setTextViewText(R.id.textViewTituloWidget, dica.getTitulo());
            views.setTextViewText(R.id.textViewConteudoWidget, dica.getConteudo());
        }

        Intent refresh = new Intent(context, getClass());
        PendingIntent refreshIntent = PendingIntent.getBroadcast(context, 0, refresh, 0);
        views.setOnClickPendingIntent(R.id.buttonSortearWidget, refreshIntent);
        appWidgetManager.updateAppWidget(new ComponentName(context, getClass()), views);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

