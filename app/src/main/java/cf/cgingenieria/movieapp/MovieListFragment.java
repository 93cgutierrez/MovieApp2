package cf.cgingenieria.movieapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.todkars.shimmer.ShimmerRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cf.cgingenieria.movieapp.databinding.FragmentMovieListBinding;
import cf.cgingenieria.movieapp.model.adapter.MovieRecyclerAdapter;
import cf.cgingenieria.movieapp.model.data.response.Movie;
import cf.cgingenieria.movieapp.viewmodel.MovieListViewModel;
import retrofit2.http.Body;

public class MovieListFragment extends Fragment {
    private static final String TAG = MovieListFragment.class.getCanonicalName();
    private FragmentMovieListBinding binding;
    private MovieListViewModel mViewModel;
    private View viewContext;
    private List<Movie> movieList = new ArrayList<Movie>();
    private MovieRecyclerAdapter movieRecyclerAdapter;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentMovieListBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewContext = view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);
        if (viewContext != null) {
            initUI(viewContext);
            if (MainActivity.isFirstLaunch()) {
                mViewModel.getMovieListApi();
                MainActivity.setIsFirstLaunch(false);
            }
            subscribeEvents(mViewModel);
        }
    }


    private void subscribeEvents(MovieListViewModel mViewModel) {
        mViewModel.isViewLoading().observe(getViewLifecycleOwner(), this::isLoadingObserver);
        mViewModel.isViewLoadingMoreMovies().observe(getViewLifecycleOwner(), this::isLoadingMoreMoviesObserver);
        mViewModel.getOnMessageError().observe(getViewLifecycleOwner(), this::getOnMessageErrorObserver);
        mViewModel.isEmptyMovieList().observe(getViewLifecycleOwner(), this::isEmptyMovieList);
        mViewModel.getMovieList().observe(getViewLifecycleOwner(), this::getMovieList);
    }

    private void getMovieList(List<Movie> movies) {
        // borrar la lista antigua
        if (movieList == null)
            movieList = new ArrayList<Movie>();
/*        if (movieList.size() > 0)
            movieList.clear();*/

        int oldSizeMovieList = movieList.size();
        // agregar nueva lista
        movieList.addAll(movies);
/*        // notificar al adaptador
        movieRecyclerAdapter.notifyDataSetChanged();*/
        //añaden no borra anteriores
        movieRecyclerAdapter.notifyItemRangeInserted(oldSizeMovieList, movies.size());
        //se muestra la lista
        binding.rvMovieList.setVisibility(View.VISIBLE);
        //se oculta lo demas
        isLoadingObserver(false);
        isEmptyMovieList(false);
        getOnMessageErrorObserver(null);
        binding.fabRefreshListMovie.setVisibility(View.GONE);
        binding.srlListMovie.setVisibility(View.VISIBLE);
    }

    private void isEmptyMovieList(Boolean isEmpty) {
        if (isEmpty) {
            binding.ivNotification.setImageResource(R.drawable.empty_icon);
            binding.tvNotification.setText(R.string.msg_empty);
            binding.fabRefreshListMovie.setVisibility(View.VISIBLE);
            binding.srlListMovie.setVisibility(View.GONE);
        }
        binding.svNotification.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    private void getOnMessageErrorObserver(String error) {
        if (error != null) {
            binding.ivNotification.setImageResource(R.drawable.error_icon);
            binding.tvNotification.setText(String.format("%s %s", getString(R.string.msg_error), error));
            binding.svNotification.setVisibility(View.VISIBLE);
            binding.fabRefreshListMovie.setVisibility(View.VISIBLE);
            binding.srlListMovie.setVisibility(View.GONE);
        } else {
            binding.svNotification.setVisibility(View.GONE);
        }
    }

    private void isLoadingObserver(Boolean isLoading) {
        if (binding == null)
            return;
        if (isLoading) {
            binding.rvMovieList.setVisibility(View.GONE);
            getOnMessageErrorObserver(null);
            binding.srvLoading.showShimmer();
        } else {
            binding.srvLoading.hideShimmer(); // to hide shimmer
            binding.fabRefreshListMovie.setEnabled(true);
        }
        binding.srvLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        //termina el proceso false
        binding.srlListMovie.setRefreshing(isLoading);
    }

    private void isLoadingMoreMoviesObserver(Boolean isLoading) {
        if (binding == null)
            return;
        //loading more movies
        binding.cpiLoadingMoreMovies.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.srlListMovie.setRefreshing(isLoading);
    }

    private void initUI(View viewContext) {
        //fab
        binding.fabRefreshListMovie.setOnClickListener(view -> {
            binding.fabRefreshListMovie.setEnabled(false);
            mViewModel.getMovieListApi();
        });

        //swipepullrefresh
        binding.srlListMovie.setOnRefreshListener(() -> {
            //termina el proceso false
            binding.srlListMovie.setRefreshing(false);
            mViewModel.getMovieListApi();
        });

        //recyclerview
        binding.rvMovieList.setHasFixedSize(true);
        binding.rvMovieList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
               /* new LinearLayoutManager(getContext(),
                        LinearLayoutManager.VERTICAL, false));*/
        movieRecyclerAdapter = new MovieRecyclerAdapter(movieList, getContext(), this::onMovieListener);
        binding.rvMovieList.setAdapter(movieRecyclerAdapter);
        //detectar scroll
        binding.rvMovieList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "movie onScrolled: ");
                if(binding.rvMovieList.canScrollVertically(1)) {
                    Log.d(TAG, "movie onScrolled: " + mViewModel.getCurrentPageVM() + " - " + mViewModel.getTotalAvailablePagesVM());
                    if(mViewModel.getCurrentPageVM() <= mViewModel.getTotalAvailablePagesVM()){
                        mViewModel.setCurrentPageVM(mViewModel.getCurrentPageVM() + 1);
                        mViewModel.getMovieListApi(mViewModel.getCurrentPageVM());
                    }
                }
            }
        });

        //loading
        binding.srvLoading.setLayoutManager(new GridLayoutManager(getActivity(), 3));
               /* new LinearLayoutManager(getContext(),
                        LinearLayoutManager.VERTICAL, false));*/
        binding.srvLoading.setAdapter(movieRecyclerAdapter);
        /* Shimmer layout view type depending on List / Gird */

        // This is optional, use if no attributes are mentioned in layout xml resource.
        // WARNING: Setting Shimmer programmatically will obsolete all shimmer attributes.
        /* mShimmerRecyclerView.setShimmer(mShimmer); */
        binding.srvLoading.setItemViewType((type, position) -> R.layout.fragment_movie_list_item);
    }

    private void onMovieListener(View view) {
        if (view != null) {
            Log.d(TAG, "onMovieListener: tedMovieTitle:: "
                    + movieList.get(binding.rvMovieList
                    .getChildAdapterPosition(view)).getMovieTitle());
            Movie movieSelected = movieList.get(binding.rvMovieList.getChildAdapterPosition(view));
            MovieListFragmentDirections
                    .ActionMovieListFragmentToDetailMovieFragment action
                    = MovieListFragmentDirections.actionMovieListFragmentToDetailMovieFragment(movieSelected);
            NavHostFragment.findNavController(MovieListFragment.this)
                    .navigate(action);
        }
    }

/*    private void onMovieListener(int position) {
        if(movieList != null && movieList.get(position) != null){
            Log.d(TAG, "onMovieListener: selectedMovieTitle:: " + movieList.get(position).getMovieTitle());
        }
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}