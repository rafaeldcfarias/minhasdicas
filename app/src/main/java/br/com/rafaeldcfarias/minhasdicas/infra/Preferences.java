package br.com.rafaeldcfarias.minhasdicas.infra;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rk on 26/06/2015.
 */
public class Preferences {

    private final static String sessionName = "minhasdicas";
    private static SharedPreferences sharedPreferences;
    private static boolean init;

    public static void initialize(Context context) {
        sharedPreferences = context.getSharedPreferences(sessionName,Context.MODE_PRIVATE);
        init = true;
    }

    public static SharedPreferences getInstance() {
        if (sharedPreferences == null)
            throw new RuntimeException("Shared Preferences is not initialized");

        return sharedPreferences;
    }

    public static boolean isInitializated() {
        return init;
    }

}


