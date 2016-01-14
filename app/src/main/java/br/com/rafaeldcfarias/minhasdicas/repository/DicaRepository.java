package br.com.rafaeldcfarias.minhasdicas.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.rafaeldcfarias.minhasdicas.infra.DBHelper;
import br.com.rafaeldcfarias.minhasdicas.model.Dica;

/**
 * Created by rk on 25/06/2015.
 */
public class DicaRepository {

    private SQLiteDatabase db;
    public static String TB_NAME = "dica";
    public static String TB_CREATE = "CREATE TABLE " + TB_NAME + " (_id integer primary key ,titulo text not null unique, conteudo text not null);";
    public static String[] TB_COLUMNS = {"_id", "titulo", "conteudo"};

    public DicaRepository() {
        this.db = DBHelper.getInstance().getDb();
    }

    public long inserir(Dica dica) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("titulo", dica.getTitulo());
        contentValues.put("conteudo", dica.getConteudo());
        if (dica.getId() != 0) {
            return db.update(TB_NAME, contentValues, "_id = ?", new String[]{String.valueOf(dica.getId())});
        }
        return db.insert(TB_NAME, null, contentValues);
    }

    public int apagar(long id) {
        return this.db.delete(TB_NAME, "_id = ?", new String[]{String.valueOf(id)});
    }

    public Dica buscar(long id) {
        Dica dica = null;
        Cursor cursor = null;
        cursor = db.query(TB_NAME, TB_COLUMNS, " _id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            dica = new Dica();
            dica.setId(cursor.getLong(0));
            dica.setTitulo(cursor.getString(1));
            dica.setConteudo(cursor.getString(2));
        }

        fecharCursor(cursor);
        return dica;
    }

    public Dica buscarPorTitulo(String titulo) {
        Dica dica = null;
        Cursor cursor = null;
        cursor = db.query(TB_NAME, TB_COLUMNS, " titulo like ?", new String[]{titulo}, null, null, null);
        if (cursor.moveToFirst()) {
            dica = new Dica();
            dica.setId(cursor.getLong(0));
            dica.setTitulo(cursor.getString(1));
            dica.setConteudo(cursor.getString(2));
        }

        fecharCursor(cursor);
        return dica;
    }

    public List<Dica> buscarPorTituloOuConteudo(String chave) {
        List<Dica> dicas = new ArrayList();
        Cursor cursor = null;
        cursor = db.query(TB_NAME, TB_COLUMNS, " titulo like ?||'%' or conteudo like ?||'%'", new String[]{chave, chave}, null, null, "titulo");
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Dica dica = new Dica();
                dica.setId(cursor.getLong(0));
                dica.setTitulo(cursor.getString(1));
                dica.setConteudo(cursor.getString(2));
                dicas.add(dica);
                cursor.moveToNext();
            }
        }
        fecharCursor(cursor);
        return dicas;
    }

    public List<Dica> buscarTodos() {
        List<Dica> dicas = new ArrayList();
        Cursor cursor = null;
        cursor = db.query(TB_NAME, TB_COLUMNS, null, null, null, null, "titulo");
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Dica dica = new Dica();
                dica.setId(cursor.getLong(0));
                dica.setTitulo(cursor.getString(1));
                dica.setConteudo(cursor.getString(2));
                dicas.add(dica);
                cursor.moveToNext();
            }
        }
        fecharCursor(cursor);
        return dicas;
    }

    public Long count() {
        Cursor cursor = null;
        cursor = db.rawQuery("select count(_id) from dica;", null);
        long count = -1;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            count = cursor.getLong(0);
        }
        fecharCursor(cursor);

        return count;
    }

    private void fecharCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

}
