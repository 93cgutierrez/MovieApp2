package cf.cgingenieria.movieapp.model.repository;


import cf.cgingenieria.movieapp.model.adapter.MovieApiAdapter;
import cf.cgingenieria.movieapp.model.data.response.MovieDetailsResponse;
import cf.cgingenieria.movieapp.model.data.response.MovieListResponse;
import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by CGIngenieria on 11/02/2021.
 */
public class MovieRepositoryImpl implements MovieRepository{

    @Override
    public Observable<Response<MovieListResponse>> getMovieList(int currentPage) {
        return MovieApiAdapter.getApiService().getMovieList(currentPage);
    }

    @Override
    public Observable<Response<MovieDetailsResponse>> getMovieDetail(int movieId) {
        return MovieApiAdapter.getApiService().getMovieDetail(movieId);
    }
}
