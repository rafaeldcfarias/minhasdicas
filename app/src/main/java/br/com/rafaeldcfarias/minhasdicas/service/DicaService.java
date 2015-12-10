package br.com.rafaeldcfarias.minhasdicas.service;

import java.util.List;

import br.com.devlooper.study.model.Dica;
import br.com.devlooper.study.repository.DicaRepository;
import br.com.rafaeldcfarias.minhasdicas.model.Dica;
import br.com.rafaeldcfarias.minhasdicas.repository.DicaRepository;

/**
 * Created by rk on 25/06/2015.
 */
public class DicaService {
    private DicaRepository cartaoRepository;

    public DicaService() {
        cartaoRepository = new DicaRepository();
    }

    public long salvar(Dica dica) {
        return cartaoRepository.inserir(dica);
    }

    public Dica buscar(Dica dica) {
        return cartaoRepository.buscar(dica);
    }

    public List<Dica> buscarTodos() {
        return cartaoRepository.buscarTodos();
    }

    public int apagar(Dica dica) {
        return cartaoRepository.apagar(dica);
    }

    public long count() {
        return cartaoRepository.count();
    }
}
