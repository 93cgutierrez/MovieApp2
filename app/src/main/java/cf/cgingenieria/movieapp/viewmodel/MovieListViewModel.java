package cf.cgingenieria.movieapp.viewmodel;

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

    //loading
    private MutableLiveData<Boolean> isViewLoading = new MutableLiveData<>();

    public LiveData<Boolean> isViewLoading() {
        if (isViewLoading == null) {
            isViewLoading = new MutableLiveData<Boolean>();
        }
        return isViewLoading;
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
        isViewLoading.postValue(true);
        disposables.add(
                movieRepository.getMovieList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result -> {
                                    isViewLoading.postValue(false);
                                    if (result.code() == 200 && result.body() != null && result.body().getProductList() != null && result.body().getProductList().size() > 0) {
                                        movieList.postValue(result.body().getProductList());
                                    } else {
                                        isEmptyMovieList.postValue(true);
                                    }
                                },
                                throwable -> {
                                    isViewLoading.postValue(false);
                                    onMessageError.postValue(throwable.getMessage());
                                }
                        )
        );
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }


}