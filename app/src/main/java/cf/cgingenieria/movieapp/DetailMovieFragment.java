package cf.cgingenieria.movieapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import cf.cgingenieria.movieapp.databinding.FragmentDetailMovieBinding;
import cf.cgingenieria.movieapp.model.data.response.Movie;

public class DetailMovieFragment extends Fragment {
    private static final String TAG = DetailMovieFragment.class.getCanonicalName();
    private FragmentDetailMovieBinding binding;
    private Movie movieReceived;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentDetailMovieBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null){
            if(DetailMovieFragmentArgs.fromBundle(getArguments()).getMovieSelected().getMovieId() > 0
            && DetailMovieFragmentArgs.fromBundle(getArguments()).getMovieSelected().getMovieTitle() != null
            && DetailMovieFragmentArgs.fromBundle(getArguments()).getMovieSelected().getMovieBackdropPath() != null
            && DetailMovieFragmentArgs.fromBundle(getArguments()).getMovieSelected().getMovieOverview() != null
            && DetailMovieFragmentArgs.fromBundle(getArguments()).getMovieSelected().getMoviePosterPath() != null){
                movieReceived = DetailMovieFragmentArgs.fromBundle(getArguments()).getMovieSelected();
                binding.textviewSecond.setText(movieReceived.getMovieTitle());
                Log.d(TAG, "onViewCreated: Movie: " + movieReceived.getMovieTitle());
            }
        }

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(DetailMovieFragment.this)
                        .popBackStack();
/*                NavHostFragment.findNavController(DetailMovieFragment.this)
                        .navigate(R.id.action_detailMovieFragment_to_movieListFragment);*/
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}