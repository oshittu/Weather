package com.tomi.weather;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    TextView mDate, mCountry, mCity, mTemp, mMain, mDesc, mName, mFeels, mMax, mMin, mPressure, mHumid;
    Button btn;
    ImageView imgIcon;
    String myCity="Toronto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // mDate=findViewById(R.id.mDate); // je n'arrive pas a faire la date locale de chaque ville, donc je l'ai omit
        mCountry=findViewById(R.id.mCountry);
        mCity=findViewById(R.id.mCity);
        mTemp=findViewById(R.id.mTemp);
        mMain=findViewById(R.id.mMain);
        mDesc=findViewById(R.id.mDesc);
        mName=findViewById(R.id.mName);
        btn=findViewById(R.id.btn);
        imgIcon=findViewById(R.id.imgIcon);

        mFeels=findViewById(R.id.mFeels);
        mMax=findViewById(R.id.mMax);
        mMin=findViewById(R.id.mMin);
        mPressure=findViewById(R.id.mPressure);
        mHumid=findViewById(R.id.mHumid);

        show();
    }
    String CityName = "Toronto";

    public void show()
    {
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+CityName+"&appid=72c3eebb9570346bcf8ab71a6488e1b2&units=metric";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url , null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject system = response.getJSONObject("sys");


                    String theTemp = main_object.getString("temp");
                    mTemp.setText(theTemp);

                    String theCon = system.getString("country");
                    mCountry.setText(theCon);

                    int arraySize = array.length();
                    String[] desc1 = new String[arraySize];
                    for (int i = 0; i < arraySize; i++) {
                        JSONObject jsonobj = (JSONObject) array.get(i);
                        desc1[i] = jsonobj.get("main").toString();
                        mMain.setText(desc1[i]);
                    }

                    String[] desc2 = new String[arraySize];
                    for (int i = 0; i < arraySize; i++) {
                        JSONObject jsonobj = (JSONObject) array.get(i);
                        desc2[i] = jsonobj.get("description").toString();
                        mDesc.setText(desc2[i]);
                    }

                    String[] d = new String[arraySize];
                    for (int i = 0; i < arraySize; i++) {
                        JSONObject jsonobj = (JSONObject) array.get(i);
                        d[i] = jsonobj.get("icon").toString();
                        String iconCode = d[i];
                        String iconUrl = "http://openweathermap.org/img/w/" + iconCode + ".png";

                        Picasso.with(getApplicationContext()).load(iconUrl).into(imgIcon);
                    }

                    mCity.setText(CityName);

                    String theFeel = main_object.getString("feels_like");
                    mFeels.setText("The temperature feels like "+theFeel);

                    String theMax = main_object.getString("temp_max");
                    mMax.setText("Temperature Maximum: "+theMax);

                    String theMin = main_object.getString("temp_min");
                    mMin.setText("Temperature Minimum: "+theMin);

                    String theP = main_object.getString("pressure");
                    mPressure.setText("Pressure: " +theP);

                    String theHum = main_object.getString("humidity");
                    mHumid.setText("Humidity: "+theHum);

                    Log.d("Tag", "resultat = "+array.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }

        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

        btn.setOnClickListener(new View.OnClickListener() // do country
        {
            @Override
            public void onClick(View v)
            {
                CityName = mName.getText().toString();
                show();
            }
        });
    }

}