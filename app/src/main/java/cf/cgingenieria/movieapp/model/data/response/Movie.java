package cf.cgingenieria.movieapp.model.data.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by CGIngenieria on 11/02/2021.
 */
public class Movie implements Parcelable {
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

    protected Movie(Parcel in) {
        MovieId = in.readByte() == 0x00 ? null : in.readInt();
        movieTitle = in.readString();
        movieOverview = in.readString();
        movieBackdropPath = in.readString();
        moviePosterPath = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (MovieId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(MovieId);
        }
        dest.writeString(movieTitle);
        dest.writeString(movieOverview);
        dest.writeString(movieBackdropPath);
        dest.writeString(moviePosterPath);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}