package cf.cgingenieria.movieapp.model.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cf.cgingenieria.movieapp.R;
import cf.cgingenieria.movieapp.model.data.Movie;
import cf.cgingenieria.movieapp.utils.Parameters;


/**
 * Created by CGIngenieria on 11/02/2021.
 */
public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.ViewHolderMovie> {
    private List<Movie> movieList;
    private List<Movie> originList;
    private Context context;
    private OnMovieListener onMovieListener;

    public MovieRecyclerAdapter(List<Movie> movieList, Context context, OnMovieListener onMovieListener) {
        this.movieList = movieList;
        this.context = context;
        this.onMovieListener = onMovieListener;
        this.originList = new ArrayList<>();
        originList.addAll(movieList);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderMovie onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_movie_list_item, null, false);
        return new ViewHolderMovie(view, onMovieListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderMovie holder, int position) {
        holder.setDataMovie(context, movieList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void filter(String filter) {
        if (filter.length() == 0) {
            movieList.clear();
            movieList.addAll(originList);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                List<Movie> movieListFilter = originList.stream().filter(movie -> movie.getMovieTitle()
                        .toLowerCase().contains(filter))
                        .collect(Collectors.toList());
                movieList.clear();
                movieList.addAll(movieListFilter);
            } else {
                movieList.clear();
                for (Movie movie : originList) {
                    if (movie.getMovieTitle().toLowerCase().contains(filter)) {
                        movieList.add(movie);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolderMovie extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        OnMovieListener onMovieListener;
        private ImageView iv_movie_poster;
        private TextView tv_movie_title;

        public ViewHolderMovie(@NonNull @NotNull View itemView, OnMovieListener onMovieListener) {
            super(itemView);
            iv_movie_poster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            tv_movie_title = (TextView) itemView.findViewById(R.id.tv_movie_title);
            this.onMovieListener = onMovieListener;
            itemView.setOnClickListener(this);
        }

        public void setDataMovie(Context context, Movie movie) {
            tv_movie_title.setText(movie.getMovieTitle());
            //GLIDE set image
            try {
                Glide.with(context).load(Parameters.API_SERVER_BASE_URL_POSTER + movie.getMoviePosterPath())
                        .centerInside()
                        .into(iv_movie_poster);
            } catch (Exception e) {
                Glide.with(context).load(R.drawable.ic_launcher_foreground)
                        .into(iv_movie_poster);
            }
        }

        @Override
        public void onClick(View view) {
            //onMovieListener.onMovieListener(getAdapterPosition());
            //onMovieListener.onMovieListener(getLayoutPosition());
            onMovieListener.onMovieListener(view);
        }
    }

    public interface OnMovieListener {
        void onMovieListener(View view);
    }
}
