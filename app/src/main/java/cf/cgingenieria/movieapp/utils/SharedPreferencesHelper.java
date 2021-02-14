package cf.cgingenieria.movieapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by CGIngenieria on 14/02/2021.
 */
public class SharedPreferencesHelper {
    private static SharedPreferences prefs;
    public static String KEY_CURRENT_PAGE = "current_page";
    public static String KEY_TOTAL_PAGES = "total_pages";


    public static void SharedPreferencesHelperInit(Context context) {
        prefs = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
    }

    public static int getPrefInt(String key, int defValue) {
        return prefs.getInt(key, defValue);
    }

    public static void setPrefInt(String key, int intValue) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, intValue);
        editor.apply();
    }
}
