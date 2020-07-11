package com.example.androidass.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.androidass.MovieViewActivity;
import com.example.androidass.R;
import com.example.androidass.Utility.GetMovieImage;
import com.example.androidass.model.Person;
import com.example.androidass.networkconnection.NetworkConnection;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchFragment extends Fragment {
    private EditText movieNameET;
    private Button searchMovieBT;
    private LinearLayout linearLayout;
    private NetworkConnection networkConnection=null;

    public SearchFragment() {
// Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        movieNameET = view.findViewById(R.id.movieName_et);
        searchMovieBT = view.findViewById(R.id.search_bt);
        networkConnection = new NetworkConnection();
        linearLayout = view.findViewById(R.id.movie_list_l);

        //click the button to get search by movie name using omdb api
        searchMovieBT.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = layoutInflater.inflate(R.layout.movie_list,null);
                linearLayout.removeAllViewsInLayout();
                String message = movieNameET.getText().toString().trim();
                GetMovieInfoOMDB getMovieInfoOMDB = new GetMovieInfoOMDB();
                getMovieInfoOMDB.execute(message);

            }
        });
        return view;
    }

    class GetMovieInfoOMDB extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(@NotNull String... strings) {
            String result = networkConnection.getMovieInfoFromOmdb(strings[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String str)
        {
            super.onPostExecute(str);
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Bundle bundle = getActivity().getIntent().getExtras();
            final Person person = bundle.getParcelable("personHome");
                try
                {
                    JSONObject jsonObjectOverall = new JSONObject(str);
                    Log.e("value", str);
                    if (jsonObjectOverall.getString("Response").equals("False"))
                        Toast.makeText(getContext(), "Invalid movie name!", Toast.LENGTH_LONG).show();
                    else
                        {
                        //get the object and then set the information one by one
                        JSONArray movieFoundArray = jsonObjectOverall.getJSONArray("Search");
                        for(int i = 0; i < movieFoundArray.length(); i++)
                        {
                            final View view = layoutInflater.inflate(R.layout.movie_list,null);
                            final JSONObject movieFoundObject = movieFoundArray.getJSONObject(i);
                            ImageView findMoviePictureIV = view.findViewById(R.id.movie_Image_iv);
                            TextView findMovieNameTV = view.findViewById(R.id.find_movie_name_tv);
                            TextView findReleaseDateTV = view.findViewById(R.id.find_release_date_tv);
                            findMovieNameTV.setText(movieFoundObject.getString("Title"));
                            findReleaseDateTV.setText(movieFoundObject.getString("Year"));

                            //download the image from omdb api
                            GetMovieImage getMovieImage = new GetMovieImage(findMoviePictureIV);
                            String url = movieFoundObject.getString("Poster");
                            getMovieImage.execute(url);

                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try{
                                        Intent intent = new Intent(getActivity(), MovieViewActivity.class);
                                        intent.putExtra("id", movieFoundObject.getString("imdbID"));
                                        intent.putExtra("verify","able");
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable("personSearch",person);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            linearLayout.addView(view);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("value", "fail");

                }
        }
    }
}
