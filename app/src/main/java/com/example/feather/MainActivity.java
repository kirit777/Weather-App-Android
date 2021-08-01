package com.example.feather;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.feather.adapter.ForcastAdapter;
import com.example.feather.model.ForcastClass;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

public class MainActivity extends AppCompatActivity {

    TextView city, county, min, max, day, date, time, temp, detail, hum;
    ImageView icon;
    EditText searchEdt;
    Button btn;
    Criteria criteria;
    LinearLayout disL;
    ProgressBar pbar;
    RecyclerView forcast;
    LinearLayout empty;
    private static final int REQUEST_LOCATION = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = (TextView) findViewById(R.id.city);
        county = (TextView) findViewById(R.id.county);
        min = (TextView) findViewById(R.id.min);
        max = (TextView) findViewById(R.id.max);
        date = (TextView) findViewById(R.id.date);
        day = (TextView) findViewById(R.id.day);
        time = (TextView) findViewById(R.id.timeh);
        temp = (TextView) findViewById(R.id.temp);
        detail = (TextView) findViewById(R.id.detail);
        hum = (TextView) findViewById(R.id.hum);
        pbar = (ProgressBar) findViewById(R.id.pbar);
        disL = (LinearLayout) findViewById(R.id.disL);
        forcast = (RecyclerView) findViewById(R.id.forcast);

        btn = (Button) findViewById(R.id.btn);
        searchEdt = (EditText) findViewById(R.id.searchEdt);

        icon = (ImageView) findViewById(R.id.iconh);
        empty = (LinearLayout)findViewById(R.id.empty);


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchEdt.getText().toString().isEmpty()) {
                    searchEdt.setError("Plz Enter City Name");
                } else {
                    searchData(searchEdt.getText().toString());
                    pbar.setVisibility(View.VISIBLE);
                    disL.setVisibility(View.GONE);
                }
            }
        });

        searchData("keshod");

    }



    private void searchData(String city1) {

        String url = "https://api.openweathermap.org/data/2.5/weather?q="+ city1 +"&appid=d9306eb6dd930650aede48577f488c27";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    pbar.setVisibility(View.GONE);
                    disL.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
//                    Log.d("nooon",jsonObject.toString());

                    String url = "https://openweathermap.org/img/wn/"+ jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon") +"@4x.png";
                    Glide.with(getApplicationContext()).load(url).into(icon);
//                    Log.d("hoooh",jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon"));
                    city.setText(jsonObject.getString("name")+",");
                    county.setText(jsonObject.getJSONObject("sys").getString("country"));

                    hum.setText(jsonObject.getJSONObject("main").getString("humidity"));
                    String det = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main")+" , " +jsonObject.getJSONArray("weather").getJSONObject(0).getString("description")+" , Wind Speed "+jsonObject.getJSONObject("wind").getString("speed");
                    detail.setText(det);

                    Calendar calendar = Calendar.getInstance();
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    String dayw = "";
                    switch (dayOfWeek){
                        case 1:
                            dayw = "Sunday";
                            break;
                        case 2:
                            dayw = "Monday";
                            break;
                        case 3:
                            dayw = "Tuesday";
                            break;
                        case 4:
                            dayw = "Wednesday";
                            break;
                        case 5:
                            dayw = "Thursday";
                            break;
                        case 6:
                            dayw = "Friday";
                            break;
                        case 7:
                            dayw = "Saturday";
                            break;
                    }
                    day.setText(dayw);


                    DateTimeFormatter dt = DateTimeFormatter.ofPattern("dd, MMM, yyyy");
                    LocalDateTime now = LocalDateTime.now();
                    date.setText(dt.format(now));

                    DateTimeFormatter tm = DateTimeFormatter.ofPattern("HH");
                    LocalDateTime nowt = LocalDateTime.now();

                    int h = Integer.parseInt(tm.format(nowt));
                    DateTimeFormatter tmm = DateTimeFormatter.ofPattern("mm");
                    String m = tmm.format(nowt);

                    if(h > 12 ){
                        h = h - 12;
                    }


                    time.setText(String.valueOf(h)+":"+m);

                    String tem = jsonObject.getJSONObject("main").getString("temp");
                    temp.setText( String.valueOf((Float.parseFloat(tem) - 273.15)).substring(0,5) + "°" );

                    String temin = jsonObject.getJSONObject("main").getString("temp_min");
                    min.setText( String.valueOf((Float.parseFloat(temin) - 273.15)).substring(0,5)+ "°" );

                    String temax = jsonObject.getJSONObject("main").getString("temp_max");
                    max.setText( String.valueOf((Float.parseFloat(temax) - 273.15)).substring(0,5)+ "°" );

                    getForcast(dayOfWeek,city.getText().toString());



                } catch (JSONException e) {
                    e.printStackTrace();
                    pbar.setVisibility(View.GONE);
                    disL.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbar.setVisibility(View.GONE);
                disL.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }

    private void getForcast(int day1, String city1) {

        String url = "https://api.openweathermap.org/data/2.5/forecast?q="+ city1 +"&ctn=7&appid=d9306eb6dd930650aede48577f488c27";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    List<ForcastClass> forcastClasses = new ArrayList<>();
                    for(int i=0; i<14;i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        int d  = day1 + i;
                        if(d > 7){
                            d= d - 7;
                        }
                        String dayf = "";
                        switch (d){
                            case 1:
                                dayf = "Sunday";
                                break;
                            case 2:
                                dayf = "Monday";
                                break;
                            case 3:
                                dayf = "Tuesday";
                                break;
                            case 4:
                                dayf = "Wednesday";
                                break;
                            case 5:
                                dayf = "Thursday";
                                break;
                            case 6:
                                dayf = "Friday";
                                break;
                            case 7:
                                dayf = "Saturday";
                                break;
                        }

                        String tem = jsonObject1.getJSONObject("main").getString("temp");
                        String temin = jsonObject1.getJSONObject("main").getString("temp_min");
                        String temax = jsonObject1.getJSONObject("main").getString("temp_max");


                        String tempf = String.valueOf((Float.parseFloat(tem) - 273.15)).substring(0,5) + "°" ;
                        String minf = String.valueOf((Float.parseFloat(temin) - 273.15)).substring(0,5)+ "°" ;
                        String maxf = String.valueOf((Float.parseFloat(temax) - 273.15)).substring(0,5)+ "°" ;
                        String windf = jsonObject1.getJSONObject("wind").getString("speed");
                        String url = "https://openweathermap.org/img/wn/"+ jsonObject1.getJSONArray("weather").getJSONObject(0).getString("icon") +"@4x.png";

                        ForcastClass forcastClass = new ForcastClass();
                        forcastClass.setDay(dayf);
                        forcastClass.setTemp(tempf);
                        forcastClass.setMin(minf);
                        forcastClass.setMax(maxf);
                        forcastClass.setWind(windf);
                        forcastClass.setIcon(url);
                        forcastClasses.add(forcastClass);
                    }


                    forcast.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                    ForcastAdapter adapter = new ForcastAdapter(forcastClasses,getApplicationContext());
                    forcast.setAdapter(adapter);



                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }



    private void showData(String latitude, String longitude) {

        String url = "https://api.openweathermap.org/data/2.5/weather?lat="+ latitude +"&lon="+ longitude +"&appid=d9306eb6dd930650aede48577f488c27";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    Log.d("nooon",jsonObject.toString());

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }


}