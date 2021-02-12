package cf.cgingenieria.movieapp;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import cf.cgingenieria.movieapp.databinding.FragmentMovieListBinding;
import cf.cgingenieria.movieapp.model.adapter.MovieRecyclerAdapter;
import cf.cgingenieria.movieapp.model.data.response.Movie;
import cf.cgingenieria.movieapp.viewmodel.MovieListViewModel;

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
            mViewModel.getMovieListApi();
            subscribeEvents(mViewModel);
        }

    }


    private void subscribeEvents(MovieListViewModel mViewModel) {
        mViewModel.isViewLoading().observe(getViewLifecycleOwner(), this::isLoadingObserver);
        mViewModel.getOnMessageError().observe(getViewLifecycleOwner(), this::getOnMessageErrorObserver);
        mViewModel.isEmptyMovieList().observe(getViewLifecycleOwner(), this::isEmptyMovieList);
        mViewModel.getMovieList().observe(getViewLifecycleOwner(), this::getMovieList);
    }

    private void getMovieList(List<Movie> movies) {
        // borrar la lista antigua
        if (movieList == null)
            movieList = new ArrayList<Movie>();
        if (movieList.size() > 0)
            movieList.clear();

        // agregar nueva lista
        movieList.addAll(movies);
        // notificar al adaptador
        movieRecyclerAdapter.notifyDataSetChanged();
        //se muestra la lista
        binding.rvMovieList.setVisibility(View.VISIBLE);
        //se oculta lo demas
        isLoadingObserver(false);
        isEmptyMovieList(false);
        getOnMessageErrorObserver(null);
    }

    private void isEmptyMovieList(Boolean isEmpty) {
        //TODO: CAMBIAR TEXTOS E IMAGEN
        binding.svNotification.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    private void getOnMessageErrorObserver(String error) {
        //TODO: CAMBIAR TEXTOS E IMAGEN
        if(error != null){
            binding.svNotification.setVisibility(View.VISIBLE);
        } else {
            binding.svNotification.setVisibility(View.GONE);
        }
    }

    private void isLoadingObserver(Boolean isLoading) {
        binding.cpiLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void initUI(View viewContext) {
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MovieListFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.rvMovieList.setHasFixedSize(true);
        binding.rvMovieList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
               /* new LinearLayoutManager(getContext(),
                        LinearLayoutManager.VERTICAL, false));*/
        movieRecyclerAdapter = new MovieRecyclerAdapter(movieList, getContext(), this::onMovieListener);
        binding.rvMovieList.setAdapter(movieRecyclerAdapter);
    }

    private void onMovieListener(int position) {
        if(movieList != null && movieList.get(position) != null){
            Log.d(TAG, "onMovieListener: selectedMovieTitle:: " + movieList.get(position).getMovieTitle());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}