package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    // Déclaration des champs
    TextView mDate, mCity, mTemp, mDescription;
    ImageView imgIcon;
    String maVille="Toronto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDate = findViewById(R.id.mDate);
        mCity = findViewById(R.id.mCity);
        mTemp = findViewById(R.id.mTemp);
        imgIcon = findViewById(R.id.imgIcon);
        mDescription = findViewById(R.id.mDescription);
        afficher();
    }

    public void afficher()
    {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=Toronto&appid=7f9799eff79f0699f9b9a8f787d62739&units=metric";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                   JSONObject main_object = response.getJSONObject("main");
                  //  JSONArray array = response.getJSONArray("weather");
                    Log.d("Tag","resultat =  " + main_object.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Tag","resultat =  "+error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}