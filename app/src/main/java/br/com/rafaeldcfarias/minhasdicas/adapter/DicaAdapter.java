package br.com.rafaeldcfarias.minhasdicas.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.devlooper.study.R;
import br.com.devlooper.study.model.Dica;
import br.com.devlooper.study.service.DicaService;

/**
 * Created by rk on 29/06/2015.
 */
public class DicaAdapter extends BaseAdapter {

    private List<Dica> dicas;
    private LayoutInflater layoutInflater;
    private DicaService dicaService;

    public DicaAdapter(Context context, @Nullable List<Dica> dicas, DicaService dicaService) {
        this.dicaService = dicaService;
        this.layoutInflater = LayoutInflater.from(context);
        if (dicas == null) {
            this.dicas = this.dicaService.buscarTodos();
        }

    }

    @Override
    public int getCount() {
        return dicas.size();
    }

    @Override
    public Object getItem(int position) {
        return dicas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dicas.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Context context = layoutInflater.getContext();
        convertView = layoutInflater.inflate(R.layout.layout_dica_adapter, null);
        ((TextView) convertView.findViewById(R.id.tvCartaoTituloLista)).setText(dicas.get(position).getTitulo());
        ((TextView) convertView.findViewById(R.id.tvCartaoConteudoLista)).setText(dicas.get(position).getConteudo());
        convertView.findViewById(R.id.ibApagarCartao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensagem = context.getString(R.string.msg_dica_excluida);
                if (dicaService.apagar(dicas.get(position)) == 1) {
                    dicas.remove(position);
                } else {
                    mensagem = context.getString(R.string.msg_problema_excluir_dica);
                }
                notifyDataSetChanged();
                Toast.makeText(context, mensagem, Toast.LENGTH_LONG).show();
            }
        });
        return convertView;
    }
}
