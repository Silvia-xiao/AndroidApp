package com.example.androidass.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidass.MovieViewActivity;
import com.example.androidass.PostToFbActivity;
import com.example.androidass.R;
import com.example.androidass.database.MovieDB;
import com.example.androidass.entity.Movie;
import com.example.androidass.model.Person;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final Context context;
    Person person;

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView movieNameRVTV;
        public TextView releaseDateRVTV;
        public TextView addDateRVTV;
        public Button deleteRVIV;
        public Button detailRVTV;
        public Button postRVBT;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            movieNameRVTV = itemView.findViewById(R.id.rv_movie_name_tv);
            releaseDateRVTV = itemView.findViewById(R.id.rv_release_date);
            addDateRVTV = itemView.findViewById(R.id.rv_add_date);
            deleteRVIV = itemView.findViewById(R.id.iv_item_delete);
            detailRVTV = itemView.findViewById(R.id.iv_view_detail);
            postRVBT = itemView.findViewById(R.id.iv_post_facebook_bt);
        }
    }

    private List<Movie> movies;

    // Pass in the contact array into the constructor
    public RecyclerViewAdapter(List<Movie> movies1,Context context, Person person) {
        movies = movies1;
        this.context = context;
        this.person = person;
    }

    public void addMovies(List<Movie> movies1) {
        movies = movies1;
        Log.e("MOVIES 2", String.valueOf(movies1.size()));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the view from an XML layout file
        View movieView = inflater.inflate(R.layout.rv_layout, parent, false); // construct the viewholder with the new view
        ViewHolder viewHolder = new ViewHolder(movieView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int position) {
        final Movie movie = movies.get(position);
        // viewHolder binding with its data at the specified position

        TextView tvMovieName= viewHolder.movieNameRVTV;
        tvMovieName.setText(movie.getMovieName());

        TextView tvReleaseDate = viewHolder.releaseDateRVTV;
        tvReleaseDate.setText(movie.getReleaseDate());

        TextView tvAddDate = viewHolder.addDateRVTV;
        tvAddDate.setText(movie.getTime());


        Button ivDelete = viewHolder.deleteRVIV;
        Button ivViewDetail = viewHolder.detailRVTV;
        final Button btPostFB = viewHolder.postRVBT;

        //click the button to view the details of the movie
        ivViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieViewActivity.class);
                Bundle bundleNext = new Bundle();
                bundleNext.putParcelable("personSearch",person);
                intent.putExtra("id", movie.getMovieId());
                intent.putExtra("verify","disable");
                intent.putExtras(bundleNext);
                context.startActivity(intent);
            }
        });

        //click the delete button to delete a movie from the watchlist
        ivDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                //this code to make confirmation if the user want to delete the movie or not.
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(movie.getMovieName());
                builder.setMessage("Are you sure to delete?");
                builder.setCancelable(true);
                builder.setPositiveButton( "Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        movies.remove(movie);
                        notifyDataSetChanged();
                        //MovieDB.getInstance(context).movieDao().delete(movie);
                        DeleteMovie deleteMovie = new DeleteMovie();
                        deleteMovie.execute(movie);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        //link to facebook sharing screen
        btPostFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostToFbActivity.class);
                intent.putExtra("movieName",movie.getMovieName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    class DeleteMovie extends AsyncTask<Movie,Void, Boolean>{

        @Override
        protected Boolean doInBackground(Movie... movies) {
            //delete the selected movie.
            MovieDB.getInstance(context).movieDao().delete(movies[0]);
            return null;
        }
    }
}
