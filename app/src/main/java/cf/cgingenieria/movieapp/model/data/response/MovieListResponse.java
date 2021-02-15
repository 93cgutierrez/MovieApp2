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
    private final List<Movie> movieList = new ArrayList<>();
    @SerializedName("page")
    @Expose
    private final int currentPageMovieList;
    @SerializedName("total_pages")
    @Expose
    private final int totalPagesMovieList;
    @SerializedName("total_results")
    @Expose
    private int totalResultsMovieList;

    public MovieListResponse(int currentPageMovieList, int totalPagesMovieList, int totalResultsMovieList) {
        this.currentPageMovieList = currentPageMovieList;
        this.totalPagesMovieList = totalPagesMovieList;
        this.totalResultsMovieList = totalResultsMovieList;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public int getCurrentPageMovieList() {
        return currentPageMovieList;
    }

    public int getTotalPagesMovieList() {
        return totalPagesMovieList;
    }

    public void setTotalResultsMovieList(int totalResultsMovieList) {
        this.totalResultsMovieList = totalResultsMovieList;
    }
}
