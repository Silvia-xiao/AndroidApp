package com.example.androidass.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.androidass.MovieViewActivity;
import com.example.androidass.R;
import com.example.androidass.model.Person;
import com.example.androidass.networkconnection.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class HomeFragment extends Fragment {
    private TextView userInformation;
    private LinearLayout linearLayout;
    private NetworkConnection networkConnection=null;


    // Required empty public constructor
    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        userInformation= view.findViewById(R.id.user_info__tv);
        TextView today = view.findViewById(R.id.date_tv);
        linearLayout = view.findViewById(R.id.card_container);
        networkConnection = new NetworkConnection();
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String todayDate = format.format(date);

        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        Person p = bundle.getParcelable("personHome");
        userInformation.setText("Hi " + p.getFirstname());
        today.setText(todayDate);

        GetTop5Movies getTop5Movies = new GetTop5Movies();
        getTop5Movies.execute(p.getPersonId());
        //String str = ((Person)getActivity().getIntent().getExtras().getParcelable("personHome")).getAddress();
        return view;


    }

    class GetTop5Movies extends AsyncTask<Integer, Void, String>
    {
        @Override
        protected String doInBackground(Integer... integers)
        {
            //get the top 5 movies watched by user in the recent year from server side database.
            String methodPath = "memoir/top5Movies/" + integers[0].toString();
            String result = networkConnection.getData(methodPath);
            return result;
        }

        @Override
        protected void onPostExecute(String s)
        {
            if( s == null )
                userInformation.setText("System Error");
            else{
                try {
                    //get the object and then set the information one by one
                    JSONArray movieTop5 = new JSONArray(s);
                    LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    for(int i = 0; i < movieTop5.length(); i++){
                       final View view = layoutInflater.inflate(R.layout.movie_top_5,null);
                        final JSONObject jsonObject = movieTop5.getJSONObject(i);

                        TextView movieName = view.findViewById(R.id.movie_name_tv);
                        TextView releaseDate = view.findViewById(R.id.release_date_tv);
                        TextView ratingScore = view.findViewById(R.id.rating_score_tv);
                        movieName.setText(jsonObject.getString("movieName"));
                        releaseDate.setText(jsonObject.getString("movieReleaseDate"));
                        ratingScore.setText(jsonObject.getString("score"));

                        linearLayout.addView(view);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("value", "fail");

                }
            }

        }

    }




}
