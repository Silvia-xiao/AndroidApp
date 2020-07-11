package com.example.androidass.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.androidass.R;
import com.example.androidass.SignUpActivity;
import com.example.androidass.model.Person;
import com.example.androidass.networkconnection.NetworkConnection;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ReportFragment extends Fragment {
    private PieChart pieChart;
    private BarChart barChart;
    private ArrayList<PieEntry> pieEntries = new ArrayList();
    private ArrayList<BarEntry> barEntries = new ArrayList<>();
    private Person person;
    private Date start;
    private Date end;
    private Timestamp startStamp;
    private Timestamp endStamp;
    private NetworkConnection networkConnection = null;
    private Spinner spinner;
    public ArrayList<String> barLabels = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        pieChart = view.findViewById(R.id.pie_chart);
        barChart = view.findViewById(R.id.bar_chart);
        spinner = view.findViewById(R.id.report_bar_select_sp);
        final Button pieStartBT = view.findViewById(R.id.pie_time_start_bt);
        final Button pieEndBT = view.findViewById(R.id.pie_time_end_bt);
        Button pieSelectBT = view.findViewById(R.id.report_pie_select_bt);
        //Button barSelectBT = view.findViewById(R.id.report_bar_select_bt);
        person = getActivity().getIntent().getExtras().getParcelable("personHome");
        networkConnection = new NetworkConnection();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.select_year_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GetYear getYear = new GetYear();
                getYear.execute(Integer.parseInt(parent.getItemAtPosition(position).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //use date time picker to let user choose start date.
        pieStartBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                DatePickerDialog dd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        c.set(year,(month+1),dayOfMonth);
                        start = c.getTime();
                        startStamp = new Timestamp(start.getTime());
                        pieStartBT.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dd.getDatePicker().setMaxDate(c.getTimeInMillis());
                dd.show();
            }
        });

        //use date time picker to let user choose end date.
        pieEndBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                DatePickerDialog dd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        c.set(year,(month+1),dayOfMonth);
                        end = c.getTime();
                        endStamp = new Timestamp(end.getTime());
                        pieEndBT.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dd.getDatePicker().setMaxDate(c.getTimeInMillis());
                dd.show();
            }
        });

        pieSelectBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMemoir getMemoir = new GetMemoir();
                getMemoir.execute();
            }
        });

        drawBarChart();
        drawPieChart();

        return view;
    }

    void drawPieChart(){
        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);
        pieChart.setUsePercentValues(true);//show percentage value
        Description description = new Description();
        description.setText("Movies watched in each cinema");
        pieChart.setDescription(description);

        PieDataSet dataSet = new PieDataSet(pieEntries, "location");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);

        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);

        pieChart.animateXY(3000,3000);
        pieChart.setData(data);

    }
    void drawBarChart() {
//        Description description;
//        Legend legend;
        Description description = new Description();
        description.setText("Movies watched in each month");
        barChart.setDescription(description);
        BarDataSet barDataSet = new BarDataSet(barEntries, "month");
        BarData data = new BarData(barDataSet);
        barChart.animate();
        barChart.setData(data);
    }

    class GetMemoir extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss.SSS");
            String str ="memoir/movieWatchedInPeriod/" + person.getPersonId()+ "/" + format.format(startStamp) + "/"+ format.format(endStamp);
            String result = networkConnection.getData(str);
            Log.e("str", result);
            Log.e("date",format.format(startStamp));
            return result;
        }

        @Override
        protected void onPostExecute(String str){
            super.onPostExecute(str);
            pieEntries.clear();
            try{
                JSONArray jsonArray = new JSONArray(str);
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    pieEntries.add(new PieEntry((float)jsonObject.getInt("count"),jsonObject.getString("location")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            drawPieChart();
        }
    }

    class GetYear extends AsyncTask<Integer, Void, String>{

        @Override
        protected String doInBackground(Integer... integers) {
            String str ="memoir/MovieWatchedPerMonth/" + person.getPersonId()+ "/" + integers[0];
            String result = networkConnection.getData(str);
            Log.e("year",result);
            return result;
        }

        @Override
        protected void onPostExecute(String str){
            super.onPostExecute(str);
            barEntries.clear();
            try{
                JSONArray jsonArray = new JSONArray(str);
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    barEntries.add(new BarEntry(jsonObject.getInt("Month Name"),jsonObject.getInt("count")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            drawBarChart();
        }


    }
}
