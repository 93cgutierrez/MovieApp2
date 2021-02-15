package cf.cgingenieria.movieapp.view;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import cf.cgingenieria.movieapp.BuildConfig;
import cf.cgingenieria.movieapp.R;
import cf.cgingenieria.movieapp.databinding.ActivityMainBinding;
import cf.cgingenieria.movieapp.io.MovieDatabase;
import cf.cgingenieria.movieapp.utils.SharedPreferencesHelper;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private static boolean isFirstLaunch = false;
    private static boolean onBackPress = false;
    private static MovieDatabase movieDatabase;

    public static MovieDatabase getMovieDatabase() {
        return movieDatabase;
    }

    public static void setMovieDatabase(Context context) {
        MainActivity.movieDatabase = Room.databaseBuilder(
                context,
                MovieDatabase.class, "movie-database")
                .build();
    }

    public static void setIsFirstLaunch(boolean isFirstLaunch) {
        MainActivity.isFirstLaunch = isFirstLaunch;
    }

    public static boolean isFirstLaunch() {
        return isFirstLaunch;
    }

    public static boolean isOnBackPress() {
        return onBackPress;
    }

    public static void setOnBackPress(boolean onBackPress) {
        MainActivity.onBackPress = onBackPress;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cf.cgingenieria.movieapp.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        isFirstLaunch = true;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        setMovieDatabase(this);
        SharedPreferencesHelper.SharedPreferencesHelperInit(this);

        new Thread(() -> {
            if(getMovieDatabase().movieDao().getAllMovies().size() > 0 && SharedPreferencesHelper.getPrefInt(SharedPreferencesHelper.KEY_CURRENT_PAGE, 0) == 0) {
                SharedPreferencesHelper.setPrefInt(SharedPreferencesHelper.KEY_CURRENT_PAGE, 1);
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}