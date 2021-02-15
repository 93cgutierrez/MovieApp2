package cf.cgingenieria.movieapp.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by CGIngenieria on 11/02/2021.
 */
@Entity
public class Movie implements Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    @SerializedName("id")
    @Expose
    Integer movieId;
    @ColumnInfo(name = "movie_title")
    @SerializedName("title")
    @Expose
    String movieTitle;
    @ColumnInfo(name = "movie_overview")
    @SerializedName("overview")
    @Expose
    String movieOverview;
    @ColumnInfo(name = "movie_popularity")
    @SerializedName("popularity")
    @Expose
    Double moviePopularity;
    @ColumnInfo(name = "movie_backdrop_path")
    @SerializedName("backdrop_path")
    @Expose
    String movieBackdropPath;
    @ColumnInfo(name = "movie_poster_path")
    @SerializedName("poster_path")
    @Expose
    String moviePosterPath;
    @ColumnInfo(name = "movie_release_date")
    @SerializedName("release_date")
    @Expose
    String movieReleaseDate;
    //"2020-12-16"


    public Movie(Integer movieId, String movieTitle, String movieOverview, Double moviePopularity, String movieBackdropPath, String moviePosterPath, String movieReleaseDate) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieOverview = movieOverview;
        this.moviePopularity = moviePopularity;
        this.movieBackdropPath = movieBackdropPath;
        this.moviePosterPath = moviePosterPath;
        this.movieReleaseDate = movieReleaseDate;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public Double getMoviePopularity() {
        return moviePopularity;
    }

    public String getMovieBackdropPath() {
        return movieBackdropPath;
    }

    public String getMoviePosterPath() {
        return moviePosterPath;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }


    protected Movie(Parcel in) {
        movieId = in.readByte() == 0x00 ? null : in.readInt();
        movieTitle = in.readString();
        movieOverview = in.readString();
        moviePopularity = in.readByte() == 0x00 ? null : in.readDouble();
        movieBackdropPath = in.readString();
        moviePosterPath = in.readString();
        movieReleaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (movieId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(movieId);
        }
        dest.writeString(movieTitle);
        dest.writeString(movieOverview);
        if (moviePopularity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(moviePopularity);
        }
        dest.writeString(movieBackdropPath);
        dest.writeString(moviePosterPath);
        dest.writeString(movieReleaseDate);
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