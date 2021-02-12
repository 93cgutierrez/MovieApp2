package cf.cgingenieria.movieapp.model.repository;

import cf.cgingenieria.movieapp.model.data.response.MovieDetailsResponse;
import cf.cgingenieria.movieapp.model.data.response.MovieListResponse;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Path;

/**
 * Created by CGIngenieria on 11/02/2021.
 */
public interface MovieRepository {
    //getAllMovies
    Observable<Response<MovieListResponse>> getMovieList();
    //getMovieDetail(id)
    Observable<Response<MovieDetailsResponse>> getMovieDetail(@Path("id") int movieId);

}
