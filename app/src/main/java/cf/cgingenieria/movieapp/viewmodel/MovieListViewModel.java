package cf.cgingenieria.movieapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import java.util.List;

import cf.cgingenieria.movieapp.view.MainActivity;
import cf.cgingenieria.movieapp.model.data.Movie;
import cf.cgingenieria.movieapp.model.repository.MovieRepositoryImpl;
import cf.cgingenieria.movieapp.utils.SharedPreferencesHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MovieListViewModel extends ViewModel {
    private static final String TAG = MovieListViewModel.class.getCanonicalName();
    private final MovieRepositoryImpl movieRepository = new MovieRepositoryImpl();
    private final CompositeDisposable disposables = new CompositeDisposable();

    //loading
    private MutableLiveData<Boolean> isViewLoading = new MutableLiveData<>();

    public LiveData<Boolean> isViewLoading() {
        if (isViewLoading == null) {
            isViewLoading = new MutableLiveData<>();
        }
        return isViewLoading;
    }

    //loading More Movies
    private MutableLiveData<Boolean> isViewLoadingMoreMovies = new MutableLiveData<>();

    public LiveData<Boolean> isViewLoadingMoreMovies() {
        if (isViewLoadingMoreMovies == null) {
            isViewLoadingMoreMovies = new MutableLiveData<>();
        }
        return isViewLoadingMoreMovies;
    }

    //data list<Movie>
    private MutableLiveData<List<Movie>> movieList = new MutableLiveData<>();

    public LiveData<List<Movie>> getMovieList() {
        if (movieList == null) {
            movieList = new MutableLiveData<>();
        }
        return movieList;
    }

    //empty
    private MutableLiveData<Boolean> isEmptyMovieList = new MutableLiveData<>();

    public LiveData<Boolean> isEmptyMovieList() {
        if (isEmptyMovieList == null) {
            isEmptyMovieList = new MutableLiveData<>();
        }
        return isEmptyMovieList;
    }

    //Error
    private MutableLiveData<String> onMessageError = new MutableLiveData<>();

    public LiveData<String> getOnMessageError() {
        if (onMessageError == null) {
            onMessageError = new MutableLiveData<>();
        }
        return onMessageError;
    }

    public void getMovieListApi() {
        getMovieListApi(Math.max(SharedPreferencesHelper.getPrefInt(SharedPreferencesHelper.KEY_CURRENT_PAGE,0), 1));
    }

    public void getMovieListApi(int currentPage) {
        changeViewLoading(true);
        disposables.add(
                movieRepository.getMovieList(currentPage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result -> {
                                    changeViewLoading(false);
                                    if (result.code() == 200 && result.body() != null && result.body().getMovieList() != null && result.body().getMovieList().size() > 0) {
                                        SharedPreferencesHelper.setPrefInt(SharedPreferencesHelper.KEY_TOTAL_PAGES, result.body().getTotalPagesMovieList());
                                        saveMoviesDB(result.body().getMovieList(),result.body().getCurrentPageMovieList());
                                    } else {
                                        isEmptyMovieList.postValue(true);
                                    }
                                },
                                throwable -> {
                                    changeViewLoading(false);
                                    onMessageError.postValue(throwable.getMessage());
                                }
                        )
        );
    }

    private void changeViewLoading(boolean isLoading) {
        if (SharedPreferencesHelper.getPrefInt(SharedPreferencesHelper.KEY_CURRENT_PAGE,0) == 1) {
            isViewLoading.postValue(isLoading);
        } else {
            isViewLoadingMoreMovies.postValue(isLoading);
        }
    }

    private void saveMoviesDB(List<Movie> movieList, int currentPage) {
        new Thread(() -> {
            //insert
            List<Long> idList = MainActivity.getMovieDatabase()
                    .movieDao()
                    .createdMovie(movieList);
            Log.d(TAG, "onClick: movieListCreated ids:: " + idList.size());
            SharedPreferencesHelper.setPrefInt(SharedPreferencesHelper.KEY_CURRENT_PAGE, currentPage);
            getMoviesDB();
        }).start();
    }

    private void getMoviesDB() {
        int offset = 0;
        //private int currentPageVM = 1;
        //private int totalAvailablePagesVM = 2;
        int resultPages = 20;
        if (SharedPreferencesHelper.getPrefInt(SharedPreferencesHelper.KEY_CURRENT_PAGE, 0) > 1)
            offset = (SharedPreferencesHelper.getPrefInt(SharedPreferencesHelper.KEY_CURRENT_PAGE, 0) * resultPages) - resultPages;
        //getAll
        List<Movie> movieListDB = MainActivity.getMovieDatabase().movieDao()
                .getListMoviesPopularityRange(offset);
        //.getAllMoviesPopularity();
        Log.d(TAG, "onActivityCreated: movie listSize: " + movieListDB.size());
        movieList.postValue(movieListDB);
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }


}