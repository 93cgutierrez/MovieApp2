package cf.cgingenieria.movieapp;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cf.cgingenieria.movieapp.databinding.FragmentMovieListBinding;
import cf.cgingenieria.movieapp.model.adapter.MovieRecyclerAdapter;
import cf.cgingenieria.movieapp.model.data.Movie;
import cf.cgingenieria.movieapp.utils.SharedPreferencesHelper;
import cf.cgingenieria.movieapp.viewmodel.MovieListViewModel;

public class MovieListFragment extends Fragment {
    private static final String TAG = MovieListFragment.class.getCanonicalName();
    private FragmentMovieListBinding binding;
    private MovieListViewModel mViewModel;
    private View viewContext;
    private List<Movie> movieList = new ArrayList<>();
    private MovieRecyclerAdapter movieRecyclerAdapter;
    private boolean isScrolling = false;
    private GridLayoutManager gridLayoutManager;
    private int previousTotalCount = 0;
    private boolean isEnabledFilter = false;
    private Menu movieListMenu;
    private boolean isMovieSelected = false;


    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container,
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
                if (SharedPreferencesHelper.getPrefInt(SharedPreferencesHelper.KEY_CURRENT_PAGE, 0) > 0) {
                    setMovieListAllMoviesPopularity();
                } else
                    mViewModel.getMovieListApi();
                MainActivity.setIsFirstLaunch(false);
            }
            subscribeEvents(mViewModel);
        }
    }

    private void setMovieListAllMoviesPopularity() {
        isLoadingObserver(true);
        new Thread(() -> {
            List<Movie> moviesDB = MainActivity.getMovieDatabase().movieDao().getAllMoviesPopularity();
            if (moviesDB.size() > 0) {
                if (!movieList.isEmpty())
                    movieList.clear();
                viewContext.post(() -> {
                    isLoadingObserver(false);
                    getMovieList(moviesDB);
                });
            }
        }).start();
    }


    private void subscribeEvents(MovieListViewModel mViewModel) {
        mViewModel.isViewLoading().observe(getViewLifecycleOwner(), this::isLoadingObserver);
        mViewModel.isViewLoadingMoreMovies().observe(getViewLifecycleOwner(), this::isLoadingMoreMoviesObserver);
        mViewModel.getOnMessageError().observe(getViewLifecycleOwner(), this::getOnMessageErrorObserver);
        mViewModel.isEmptyMovieList().observe(getViewLifecycleOwner(), this::isEmptyMovieList);
        mViewModel.getMovieList().observe(getViewLifecycleOwner(), this::getMovieList);
    }

    private void getMovieList(List<Movie> movies) {
        Log.d(TAG, "getMovieList: movie: " + movieList.containsAll(movies));
        if (!movieList.containsAll(movies)) {
            // borrar la lista antigua
            if (movieList == null)
                movieList = new ArrayList<>();
/*        if (movieList.size() > 0)
            movieList.clear();*/

            int oldSizeMovieList = movieList.size();
            // agregar nueva lista
            movieList.addAll(movies);
/*        // notificar al adaptador
        movieRecyclerAdapter.notifyDataSetChanged();*/
            //añaden no borra anteriores
            movieRecyclerAdapter.notifyItemRangeInserted(oldSizeMovieList, movies.size());
        }
        showList();
    }

    private void showList() {
        //se muestra la lista
        binding.rvMovieList.setVisibility(View.VISIBLE);
        //se oculta lo demas
        isLoadingObserver(false);
        isEmptyMovieList(false);
        getOnMessageErrorObserver(null);
        binding.fabRefreshListMovie.setVisibility(View.GONE);
        binding.srlListMovie.setVisibility(View.VISIBLE);
    }

    private void setMovieListFilter(List<Movie> movies) {
        // borrar la lista antigua
        if (movieList == null)
            movieList = new ArrayList<>();
        if (movieList.size() > 0)
            movieList.clear();

        // agregar nueva lista
        movieList.addAll(movies);
        //notificar al adaptador
        movieRecyclerAdapter.notifyDataSetChanged();

        //se muestra la lista
        showList();
    }

    private void isEmptyMovieList(Boolean isEmpty) {
        showEmptyMessage(isEmpty);
        showFABRefresh();
    }

    private void showEmptyMessage(Boolean isEmpty) {
        if (isEmpty) {
            binding.ivNotification.setImageResource(R.drawable.empty_icon);
            binding.tvNotification.setText(R.string.msg_empty);
            binding.srlListMovie.setVisibility(View.GONE);
        }
        binding.svNotification.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }


    private void isEmptyMovieListFilter(Boolean isEmpty) {
        showEmptyMessage(isEmpty);
    }

    private void showFABRefresh() {
        binding.fabRefreshListMovie.setVisibility(View.VISIBLE);
        binding.fabRefreshListMovie.setEnabled(true);
    }

    private void getOnMessageErrorObserver(String error) {
        if (error != null) {
            binding.ivNotification.setImageResource(R.drawable.error_icon);
            binding.tvNotification.setText(String.format("%s %s", getString(R.string.msg_error), error));
            binding.svNotification.setVisibility(View.VISIBLE);
            showFABRefresh();
            binding.srlListMovie.setVisibility(View.GONE);
        } else {
            binding.svNotification.setVisibility(View.GONE);
            binding.fabRefreshListMovie.setEnabled(false);
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
        setHasOptionsMenu(true);
        //fab
        binding.fabRefreshListMovie.setOnClickListener(view -> {
            binding.fabRefreshListMovie.setEnabled(false);
            mViewModel.getMovieListApi();
        });

        //swipepullrefresh
        binding.srlListMovie.setOnRefreshListener(() -> {
            //termina el proceso false
            binding.srlListMovie.setRefreshing(false);
            //mViewModel.getMovieListApi();
        });

        //recyclerview
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        binding.rvMovieList.setHasFixedSize(true);
        binding.rvMovieList.setLayoutManager(gridLayoutManager);
               /* new LinearLayoutManager(getContext(),
                        LinearLayoutManager.VERTICAL, false));*/
        movieRecyclerAdapter = new MovieRecyclerAdapter(movieList, getContext(), this::onMovieListener);
        binding.rvMovieList.setAdapter(movieRecyclerAdapter);
        //detectar scroll
        binding.rvMovieList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    isScrolling = true;
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int scrolledOutItems = gridLayoutManager.findFirstVisibleItemPosition();
                Log.d(TAG, "onQueryTextChange: movie filter: " + isEnabledFilter);
                if (isScrolling && (visibleItemCount + scrolledOutItems == totalItemCount)
                        && (visibleItemCount + scrolledOutItems != previousTotalCount) && !isEnabledFilter) {
                    Log.d(TAG, "onScrolled: movie current: " + SharedPreferencesHelper.getPrefInt(SharedPreferencesHelper.KEY_CURRENT_PAGE, 0) + " total: " + SharedPreferencesHelper.getPrefInt(SharedPreferencesHelper.KEY_TOTAL_PAGES, 0));
                    if (SharedPreferencesHelper.getPrefInt(SharedPreferencesHelper.KEY_CURRENT_PAGE, 0) < 4) {
                        // < SharedPreferencesHelper.getPrefInt(SharedPreferencesHelper.KEY_TOTAL_PAGES, 2) - 497) {
                        previousTotalCount = totalItemCount;
                        isScrolling = false;
                        SharedPreferencesHelper.setPrefInt(SharedPreferencesHelper.KEY_CURRENT_PAGE,
                                SharedPreferencesHelper.getPrefInt(SharedPreferencesHelper.KEY_CURRENT_PAGE, 0) + 1);
                        mViewModel.getMovieListApi(SharedPreferencesHelper.getPrefInt(SharedPreferencesHelper.KEY_CURRENT_PAGE, 0));
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

    private boolean filterMovieList(String newText) {
        movieRecyclerAdapter.filter(newText);
        Log.d(TAG, "filterMovieList: onBackpress ");
        if (newText.length() == 0) {
            //original
            //se activa la carga temporal por scroll
            isEnabledFilter = false;
            Log.d(TAG, "onQueryTextChange: onBackpress" + MainActivity.isOnBackPress());
            if (!MainActivity.isOnBackPress() && getView() != null)
                setMovieListAllMoviesPopularity();
            else
                MainActivity.setOnBackPress(false);
            return false;
        }
        //se desactiva la carga temporal por scroll
        isEnabledFilter = true;
        setMovieListFilter(newText.toLowerCase(), viewContext);
        return false;
    }

    private void setMovieListFilter(String newText, View viewContext) {
        isLoadingObserver(true);
        new Thread(() -> {
            List<Movie> moviesDB = MainActivity.getMovieDatabase().movieDao().getListMovieTitle(newText);
            if (moviesDB.size() > 0) {
                Log.d(TAG, "onQueryTextChange: movie DATOS " + moviesDB.size());
                viewContext.post(() -> {
                        isLoadingObserver(false);
                        setMovieListFilter(moviesDB);
            });
            } else {
                viewContext.post(() -> {
                    isLoadingObserver(false);
                    isEmptyMovieListFilter(true);
                    Log.d(TAG, "onQueryTextChange: movie SIN RESULTADOS !!");
                });
            }
        }).start();
    }

    private void onMovieListener(View view) {
        if (view != null) {
            Log.d(TAG, "onMovieListener: selected movie title:: "
                    + movieList.get(binding.rvMovieList
                    .getChildAdapterPosition(view)).getMovieTitle());
            isMovieSelected = true;
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
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        movieListMenu = menu;
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.d(TAG, "onMenuItemActionCollapse " + item.getItemId());
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.d(TAG, "onMenuItemActionExpand " + item.getItemId());
                return true;
            }
        });

        searchView.setOnCloseListener(() -> {
            Log.d(TAG, "close:: ");
            return false;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: " + newText);
                filterMovieList(newText);
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}