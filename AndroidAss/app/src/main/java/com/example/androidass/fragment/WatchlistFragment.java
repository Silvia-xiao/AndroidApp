package com.example.androidass.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidass.R;
import com.example.androidass.adapter.RecyclerViewAdapter;
import com.example.androidass.database.MovieDB;
import com.example.androidass.entity.Movie;
import com.example.androidass.model.Person;
import com.example.androidass.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class WatchlistFragment extends Fragment {

    MovieDB db = null;
    private MovieViewModel movieViewModel;
    private RecyclerView recyclerView;
    private List<Movie> movies;
    private RecyclerViewAdapter adapter;


    public WatchlistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);

        Bundle bundle = getActivity().getIntent().getExtras();
        final Person person = bundle.getParcelable("personHome");
        recyclerView = view.findViewById(R.id.watchlist_recycler_view);
        movies = new ArrayList<>();
        //movies = Movie.createContactsList();
        adapter = new RecyclerViewAdapter(movies, getContext(),person);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.initializeVars(getActivity().getApplication());
        movieViewModel.getAllMovies().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                for (Movie temp : movies) {
                    String name = temp.getMovieName();
                    String releaseD = temp.getReleaseDate();
                    String addDate = temp.getTime();
                    String movieId = temp.getMovieId();
                    saveData(movieId, name, releaseD, addDate);
                }
            }
        });


        return view;
    }


    private void saveData(String movieId, String name, String release, String add) {
        Log.e("MOVIE", movieId  + " "+ movies.size());
        final Movie movie = new Movie(movieId, name, release, add);
        movies.add(movie);
        adapter.notifyDataSetChanged();
        //adapter.addMovies(movies);

    }
}
