package cf.cgingenieria.movieapp.viewmodel;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import java.util.List;

import cf.cgingenieria.movieapp.model.data.response.Movie;
import cf.cgingenieria.movieapp.model.repository.MovieRepositoryImpl;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MovieListViewModel extends ViewModel {
    private static final String TAG = MovieListViewModel.class.getCanonicalName();
    private MovieRepositoryImpl movieRepository = new MovieRepositoryImpl();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private int currentPageVM = 1;
    private int totalAvailablePagesVM = 2;

    public int getCurrentPageVM() {
        return currentPageVM;
    }

    public void setCurrentPageVM(int currentPageVM) {
        this.currentPageVM = currentPageVM;
    }

    public int getTotalAvailablePagesVM() {
        return totalAvailablePagesVM;
    }

    public void setTotalAvailablePagesVM(int totalAvailablePagesVM) {
        this.totalAvailablePagesVM = totalAvailablePagesVM;
    }

    //loading
    private MutableLiveData<Boolean> isViewLoading = new MutableLiveData<>();

    public LiveData<Boolean> isViewLoading() {
        if (isViewLoading == null) {
            isViewLoading = new MutableLiveData<Boolean>();
        }
        return isViewLoading;
    }

    //loading More Movies
    private MutableLiveData<Boolean> isViewLoadingMoreMovies = new MutableLiveData<>();

    public LiveData<Boolean> isViewLoadingMoreMovies() {
        if (isViewLoadingMoreMovies == null) {
            isViewLoadingMoreMovies = new MutableLiveData<Boolean>();
        }
        return isViewLoadingMoreMovies;
    }

    //data list<Movie>
    private MutableLiveData<List<Movie>> movieList = new MutableLiveData<>();

    public LiveData<List<Movie>> getMovieList() {
        if (movieList == null) {
            movieList = new MutableLiveData<List<Movie>>();
        }
        return movieList;
    }

    //empty
    private MutableLiveData<Boolean> isEmptyMovieList = new MutableLiveData<>();

    public LiveData<Boolean> isEmptyMovieList() {
        if (isEmptyMovieList == null) {
            isEmptyMovieList = new MutableLiveData<Boolean>();
        }
        return isEmptyMovieList;
    }

    //Error
    private MutableLiveData<String> onMessageError = new MutableLiveData<>();

    public LiveData<String> getOnMessageError() {
        if (onMessageError == null) {
            onMessageError = new MutableLiveData<String>();
        }
        return onMessageError;
    }

    public void getMovieListApi() {
        getMovieListApi(1);
    }

    public void getMovieListApi(int currentPage) {
        currentPageVM = currentPage;
        changeViewLoading(true);
        disposables.add(
                movieRepository.getMovieList(currentPage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result -> {
                                    changeViewLoading(false);
                                    if (result.code() == 200 && result.body() != null && result.body().getProductList() != null && result.body().getProductList().size() > 0) {
                                        currentPageVM = result.body().getCurrentPageMovieList();
                                        totalAvailablePagesVM = result.body().getTotalPagesMovieList();
                                        movieList.postValue(result.body().getProductList());
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
        if (currentPageVM == 1) {
            isViewLoading.postValue(isLoading);
        } else {
            isViewLoadingMoreMovies.postValue(isLoading);
        }
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }


}