package cf.cgingenieria.movieapp.model.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cf.cgingenieria.movieapp.model.data.Movie;

/**
 * Created by CGIngenieria on 13/02/2021.
 */
@Dao
public interface MovieDao {
    //getAll
    @Query("SELECT * FROM Movie")
    List<Movie> getAllMovies();

    //getAll order popularity
    @Query("SELECT * FROM Movie ORDER BY movie_popularity DESC")
    List<Movie> getAllMoviesPopularity();

    //getListMoviePopularity (20Movie) offset page
    @Query("SELECT * FROM Movie ORDER BY movie_popularity DESC LIMIT 20 OFFSET :offset")
    List<Movie> getListMoviesPopularityRange(int offset);


    //getById
    @Query("SELECT * FROM Movie WHERE movie_id = :movieId")
    Movie getMovieById(int movieId);

    //getByTitle filter %:movieTitle%
    @Query("SELECT * FROM Movie WHERE movie_title LIKE '%' || :movieTitle || '%'")
    Movie getMovieByTitle(String movieTitle);

    //getByTitle filter %:movieTitle%
    @Query("SELECT * FROM Movie WHERE movie_title LIKE '%' || :movieTitle || '%'")
    List<Movie> getListMovieTitle(String movieTitle);
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long createdMovie(Movie movie);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> createdMovie(List<Movie> movieList);

    //Update devuelve cantidad de filas actualizadas
    @Update
    int updateMovie(Movie movie);

    //Delete devuelve cantidad de filas eliminadas
    @Delete
    int deleteMovie(Movie movie);

    //tener mucho cuidado solo para pruebas
    @Query("DELETE FROM Movie")
    void clearDBMovie();
}
