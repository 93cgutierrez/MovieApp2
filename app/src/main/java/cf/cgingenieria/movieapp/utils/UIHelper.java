package cf.cgingenieria.movieapp.utils;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import java.util.Objects;

import cf.cgingenieria.movieapp.R;

/**
 * Created by CGIngenieria on 12/02/2021.
 */
public class UIHelper {
    private static final String TAG = UIHelper.class.getCanonicalName();

    public static void hideToolbar(FragmentActivity activity, boolean hide, String title) {
        Log.d(TAG, "hideToolbar: " + hide);
        if (activity != null && ((AppCompatActivity) activity).getSupportActionBar() != null) {
            if (hide) {
                Objects.requireNonNull(((AppCompatActivity) activity).getSupportActionBar()).hide();
            } else {
                if (title != null && !title.isEmpty())
                    Objects.requireNonNull(((AppCompatActivity) activity).getSupportActionBar()).setTitle(title);
                Objects.requireNonNull(((AppCompatActivity) activity).getSupportActionBar()).show();
            }
        }

    }
}
