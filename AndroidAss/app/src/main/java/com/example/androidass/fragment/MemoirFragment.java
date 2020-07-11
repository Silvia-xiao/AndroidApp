package com.example.androidass.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.androidass.MainActivity;
import com.example.androidass.MovieViewActivity;
import com.example.androidass.R;
import com.example.androidass.Utility.GetMovieImage;
import com.example.androidass.model.Memoir;
import com.example.androidass.model.Person;
import com.example.androidass.networkconnection.NetworkConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MemoirFragment extends Fragment {

    private LinearLayout memoirContainer;
    private NetworkConnection networkConnection=null;
    private Person person;
    private List<Memoir> memoirs = new ArrayList<>();;
    private String selected;

    public MemoirFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_memoir, container, false);

        memoirContainer = view.findViewById(R.id.mem_container_l);
        Spinner sortSpinner = view.findViewById(R.id.mem_sort_spinner);
        networkConnection = new NetworkConnection();
        Bundle bundle = getActivity().getIntent().getExtras();
        person = bundle.getParcelable("personHome");
        String url = "memoir/findByPersonId/" + person.getPersonId();
        GetMemoirs getMemoirs = new GetMemoirs();
        getMemoirs.execute(url);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.sort_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);
        selected = "None";
        //set the spinner sorting function.
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = parent.getItemAtPosition(position).toString();
                if(selected.equals("None")){
                    return;
                }else if (selected.equals("WatchDate")){
                    Collections.sort(memoirs, new Comparator<Memoir>() {
                        @Override
                        public int compare(Memoir o1, Memoir o2) {
                            return o2.getUserWatchDateTime().compareTo(o1.getUserWatchDateTime());
                        }
                    });
                }else if (selected.equals("Your rating")){
                    Collections.sort(memoirs, new Comparator<Memoir>() {
                        @Override
                        public int compare(Memoir o1, Memoir o2) {
                            return (int)((o2.getScore()-o1.getScore())*10);
                        }
                    });
                }else if (selected.equals("Public Ratings")){
                    Collections.sort(memoirs, new Comparator<Memoir>() {
                        @Override
                        public int compare(Memoir o1, Memoir o2) {
                            return (int)((o2.getPublicRating()-o1.getPublicRating())*10);
                        }
                    });
                }
            addView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    class GetMemoirs extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try{
                //get the memoirs from server side database
                String result = networkConnection.getData(strings[0]);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String str){
            super.onPostExecute(str);

            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(str);
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
                    final Memoir memoir = gson.fromJson(jsonArray.getJSONObject(i).toString(),Memoir.class);
                    if(memoir!=null)
                        memoirs.add(memoir);
            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            addView();
        }
    }

    /**
     * Get each movie memoir view and then add to the layout one by one.
     */
    private void addView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        memoirContainer.removeAllViews();
        for (final Memoir memoir : memoirs) {
            View view = layoutInflater.inflate(R.layout.movie_memoir, null);
            TextView movieNameTV = view.findViewById(R.id.mem_movie_name_tv);
            TextView releaseTV = view.findViewById(R.id.mem_release_date_tv);
            TextView watchDateTV = view.findViewById(R.id.mem_watch_date_tv);
            TextView cinemaLocation = view.findViewById(R.id.mem_cin_position_tv);
            TextView commentTV = view.findViewById(R.id.mem_comment_tv);
            RatingBar ratingBar = view.findViewById(R.id.mem_rating_bar);
            ImageView image = view.findViewById(R.id.mem_image_iv);

            movieNameTV.setText(memoir.getMovieName());
            releaseTV.setText(memoir.getMovieReleaseDate().toString());
            watchDateTV.setText(memoir.getUserWatchDateTime().toString());
            cinemaLocation.setText(memoir.getCinemaId().getLocation());
            //change the star rating according to user's choice.
            if(selected.equals(("None")))
                ratingBar.setRating((float) memoir.getScore());
            if(selected.equals("WatchDate"))
                ratingBar.setRating((float) memoir.getScore());
            if (selected.equals("Your rating")){
                ratingBar.setRating((float) memoir.getScore());}
            if (selected.equals("Public Ratings")){
                ratingBar.setRating((float)memoir.getPublicRating()); }
            commentTV.setText(memoir.getUserComment());
            new GetMovieImage(image).execute(memoir.getImageurl());

            //if click on each view, user will be led to movie detail screen but the add to watchlist button will be disabled.
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        //let the user see the details of a movie and disable the add to watchlist button.
                        Intent intent = new Intent(getActivity(), MovieViewActivity.class);
                        intent.putExtra("id", memoir.getOmdb());
                        intent.putExtra("verify","disable");
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("personSearch", person);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            memoirContainer.addView(view);
        }
    }


}
