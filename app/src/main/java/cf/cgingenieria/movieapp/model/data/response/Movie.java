package cf.cgingenieria.movieapp.model.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Created by CGIngenieria on 11/02/2021.
 */
public class Movie implements Serializable {
    @SerializedName("id")
    @Expose
    Integer MovieId;
    @SerializedName("title")
    @Expose
    String movieTitle;
    @SerializedName("overview")
    @Expose
    String movieOverview;
    @SerializedName("backdrop_path")
    @Expose
    String movieBackdropPath;
    @SerializedName("poster_path")
    @Expose
    String moviePosterPath;

    public Integer getMovieId() {
        return MovieId;
    }

    public void setMovieId(Integer movieId) {
        MovieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public String getMovieBackdropPath() {
        return movieBackdropPath;
    }

    public void setMovieBackdropPath(String movieBackdropPath) {
        this.movieBackdropPath = movieBackdropPath;
    }

    public String getMoviePosterPath() {
        return moviePosterPath;
    }

    public void setMoviePosterPath(String moviePosterPath) {
        this.moviePosterPath = moviePosterPath;
    }
}
