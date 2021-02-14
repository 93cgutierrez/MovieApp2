package cf.cgingenieria.movieapp.model.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cf.cgingenieria.movieapp.model.data.Movie;


/**
 * Created by CGIngenieria on 11/02/2021.
 */
public class MovieListResponse implements Serializable {
    @SerializedName("results")
    @Expose
    private List<Movie> movieList = new ArrayList<>();
    @SerializedName("page")
    @Expose
    private int currentPageMovieList;
    @SerializedName("total_pages")
    @Expose
    private int totalPagesMovieList;
    @SerializedName("total_results")
    @Expose
    private int totalResultsMovieList;

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public int getCurrentPageMovieList() {
        return currentPageMovieList;
    }

    public void setCurrentPageMovieList(int currentPageMovieList) {
        this.currentPageMovieList = currentPageMovieList;
    }

    public int getTotalPagesMovieList() {
        return totalPagesMovieList;
    }

    public void setTotalPagesMovieList(int totalPagesMovieList) {
        this.totalPagesMovieList = totalPagesMovieList;
    }

    public int getTotalResultsMovieList() {
        return totalResultsMovieList;
    }

    public void setTotalResultsMovieList(int totalResultsMovieList) {
        this.totalResultsMovieList = totalResultsMovieList;
    }
}
