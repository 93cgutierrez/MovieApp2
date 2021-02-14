package cf.cgingenieria.movieapp.io;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import cf.cgingenieria.movieapp.model.data.Movie;
import cf.cgingenieria.movieapp.model.data.dao.MovieDao;

/**
 * Created by CGIngenieria on 13/02/2021.
 */
@Database(entities = {Movie.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();
}
