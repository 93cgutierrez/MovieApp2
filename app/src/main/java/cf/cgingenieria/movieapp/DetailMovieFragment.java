package cf.cgingenieria.movieapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;

import cf.cgingenieria.movieapp.databinding.FragmentDetailMovieBinding;
import cf.cgingenieria.movieapp.databinding.FragmentMovieListBinding;
import cf.cgingenieria.movieapp.model.data.response.Movie;
import cf.cgingenieria.movieapp.utils.Parameters;
import cf.cgingenieria.movieapp.utils.UIHelper;
import cf.cgingenieria.movieapp.viewmodel.MovieListViewModel;

public class DetailMovieFragment extends Fragment {
    private static final String TAG = DetailMovieFragment.class.getCanonicalName();
    private FragmentDetailMovieBinding binding;
    private Movie movieReceived;
    private View viewContext;

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
        if (getArguments() != null) {
            if (DetailMovieFragmentArgs.fromBundle(getArguments()).getMovieSelected().getMovieId() > 0
                    && DetailMovieFragmentArgs.fromBundle(getArguments()).getMovieSelected().getMovieTitle() != null
                    && DetailMovieFragmentArgs.fromBundle(getArguments()).getMovieSelected().getMovieBackdropPath() != null
                    && DetailMovieFragmentArgs.fromBundle(getArguments()).getMovieSelected().getMovieOverview() != null
                    && DetailMovieFragmentArgs.fromBundle(getArguments()).getMovieSelected().getMoviePosterPath() != null) {
                movieReceived = DetailMovieFragmentArgs.fromBundle(getArguments()).getMovieSelected();
                Log.d(TAG, "onViewCreated: Movie: " + movieReceived.getMovieTitle());
            }
            viewContext = view;
        }
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (viewContext != null) {
            initUI(viewContext);
        }
    }

    private void initUI(View viewContext) {
        //hide toolbar
        UIHelper.hideToolbar(getActivity(), false, movieReceived.getMovieTitle());
        setHasOptionsMenu(true);
        setImage(viewContext, Parameters.API_SERVER_BASE_URL_POSTER + movieReceived.getMovieBackdropPath(), binding.ivMoviePosterDetail);
        binding.tvMovieOverview.setText(movieReceived.getMovieOverview());
        binding.tvMovieReleaseDate.setText(movieReceived.getMovieReleaseDate());
 /*       NavHostFragment.findNavController(DetailMovieFragment.this)
                .popBackStack();
                NavHostFragment.findNavController(DetailMovieFragment.this)
                        .navigate(R.id.action_detailMovieFragment_to_movieListFragment);*/
    }

    private void setImage(View viewContext, String url, ImageView imageView) {
        //GLIDE set image
        try {
            Glide.with(viewContext).load(url)
                    .into(imageView);
        } catch (Exception e) {
            Glide.with(viewContext).load(R.drawable.ic_launcher_foreground)
                    .into(imageView);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}