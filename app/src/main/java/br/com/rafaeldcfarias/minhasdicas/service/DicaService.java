package br.com.rafaeldcfarias.minhasdicas.service;

import java.util.List;
import java.util.Random;

import br.com.rafaeldcfarias.minhasdicas.model.Dica;
import br.com.rafaeldcfarias.minhasdicas.repository.DicaRepository;

/**
 * Created by rk on 25/06/2015.
 */
public class DicaService {
    private DicaRepository dicaRepository;

    public DicaService() {
        dicaRepository = new DicaRepository();
    }

    public long salvar(Dica dica) {
        return dicaRepository.inserir(dica);
    }

    public Dica buscar(long id) {
        return dicaRepository.buscar(id);
    }

    public List<Dica> buscarTodos() {
        return dicaRepository.buscarTodos();
    }

    public int apagar(long id) {
        return dicaRepository.apagar(id);
    }

    public long count() {
        return dicaRepository.count();
    }

    public Dica buscarPorTitulo(String titulo) {
        return dicaRepository.buscarPorTitulo(titulo);
    }

    public List<Dica> buscarPorTituloOuConteudo(String chave) {
        return dicaRepository.buscarPorTituloOuConteudo(chave);
    }

    public Dica sortearDica() {
        Random random = new Random();
        Dica sorteado = null;
        long count = count();
        if (count > 0) {
            sorteado = buscar(random.nextInt((int) count) + 1);
        }
        return sorteado;
    }
}
