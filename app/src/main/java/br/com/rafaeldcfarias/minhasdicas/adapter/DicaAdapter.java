package br.com.rafaeldcfarias.minhasdicas.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.rafaeldcfarias.minhasdicas.R;
import br.com.rafaeldcfarias.minhasdicas.model.Dica;
import br.com.rafaeldcfarias.minhasdicas.service.DicaService;

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
        } else {
            setDicas(dicas);
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
                final Dica aRemover = dicas.get(position);
                if (dicaService.apagar(aRemover.getId()) == 1) {
                    dicas.remove(position);
                } else {
                    mensagem = context.getString(R.string.msg_problema_excluir_dica);
                }
                notifyDataSetChanged();
                Snackbar.make(parent, mensagem, Snackbar.LENGTH_LONG)
                        .setAction(context.getString(R.string.label_desfazer), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                aRemover.setId(0);
                                dicaService.salvar(aRemover);
                                dicas.add(position, aRemover);
                                notifyDataSetChanged();
                            }
                        }).show();
            }
        });
        return convertView;
    }

    public List<Dica> getDicas() {
        return dicas;
    }

    public void setDicas(List<Dica> dicas) {
        this.dicas = dicas;
    }
}
