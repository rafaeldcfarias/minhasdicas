package br.com.rafaeldcfarias.minhasdicas.infra;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import br.com.rafaeldcfarias.minhasdicas.repository.DicaRepository;

public class DBHelper {
    private static final String DB_NAME = "minhasdicas";
    private static final int DB_VERSION = 1;
    private static Context CONTEXT;

    private static DBHelper instance;

    private DBOpenHelper dbOpenHelper;

    private SQLiteDatabase db;

    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    public DBHelper(Context context) {
        dbOpenHelper = new DBOpenHelper(context, DB_NAME, DB_VERSION);
        stablishDb();
    }

    private void stablishDb() {
        if (db == null) {
            db = dbOpenHelper.getWritableDatabase();
        }
    }

    private void cleanUp() {
        if (db != null) {
            db.close();
            db = null;
        }
    }

    public static DBHelper getInstance() {
        if (instance == null) {
            instance = new DBHelper(CONTEXT);
        }
        return instance;
    }

    public static void setCONTEXT(Context context) {
        CONTEXT = context;
    }

    public boolean resetDB() {
        File file = new File(db.getPath());
        boolean deleted = false;
        deleted |= file.delete();
        deleted |= new File(file.getPath() + "-journal").delete();
        deleted |= new File(file.getPath() + "-shm").delete();
        deleted |= new File(file.getPath() + "-wal").delete();

        File dir = file.getParentFile();
        if (dir != null) {
            final String prefix = file.getName() + "-mj";
            final FileFilter filter = new FileFilter() {
                @Override
                public boolean accept(File candidate) {
                    return candidate.getName().startsWith(prefix);
                }
            };
            for (File masterJournal : dir.listFiles(filter)) {
                deleted |= masterJournal.delete();
            }
        }
        cleanUp();
        instance = null;
        return deleted;
    }

    private static class DBOpenHelper extends SQLiteOpenHelper {

        public static final Set<Class<?>> repositorios = new HashSet();

        public DBOpenHelper(Context context, String dbName, int version) {
            super(context, dbName, null, version);
            repositorios.add(DicaRepository.class);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                for (Class<?> repositorio : repositorios) {
                    String createTabela = null;
                    Field field = null;
                    try {
                        field = repositorio.getDeclaredField("TB_CREATE");
                        createTabela = (String) field.get(null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    if (createTabela != null) {
                        db.execSQL(createTabela);
                    }
                }
            } catch (SQLException e) {
                Log.e("ProvidersWidgets", DBHelper.class.getSimpleName(), e);
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            for (Class<?> classe : repositorios) {
                String tableName = null;
                Field field = null;
                try {
                    field = classe.getDeclaredField("TB_NAME");
                    tableName = (String) field.get(null);
                } catch (Exception e) {
                    Log.e("ProvidersWidgets", DBHelper.class.getSimpleName(), e);
                }
                if (tableName != null) {
                    db.execSQL("DROP TABLE IF EXISTS " + tableName + " ;");
                }
            }
            onCreate(db);
        }

    }

}
