package br.com.rafaeldcfarias.minhasdicas.model;

import java.io.Serializable;

/**
 * Created by rk on 24/06/2015.
 */
public class Dica implements Serializable {

    private long id;
    private String titulo;
    private String conteudo;

    public Dica() {
    }

    public Dica(long id, String titulo, String conteudo) {
        this.id = id;
        this.titulo = titulo;
        this.conteudo = conteudo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dica dica = (Dica) o;

        return id == dica.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
