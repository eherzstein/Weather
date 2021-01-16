package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    // Déclaration des champs
    TextView mDate, mCity, mTemp, mDescription,  mFeelsLike, mDaylightHours;
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
        mFeelsLike = findViewById(R.id.mFeelsLike);
        mDaylightHours = findViewById(R.id.mDaylightHours);
       // mCountry = findViewById(R.id.mCountry);
        afficher();
    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recherche, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
        searchView.setQueryHint("Écrire le nom de la ville");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                maVille = query;
                afficher();
                //gestion du clavier
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(getCurrentFocus()!=null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void afficher()
    {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + maVille + "&appid=7f9799eff79f0699f9b9a8f787d62739&units=metric";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                   JSONObject main_object = response.getJSONObject("main");
                   JSONObject sys_object = response.getJSONObject("sys");
                   JSONArray array = response.getJSONArray("weather");
                  //  Log.d("Tag","resultat =  " + array.toString());
                    JSONObject object = array.getJSONObject(0);
                  // température
                    int tempC = (int) Math.round(main_object.getDouble("temp"));
                    int feels_likeC = (int) Math.round(main_object.getDouble("feels_like"));

                    String temp = String.valueOf(tempC);
                    String feels_like = String.valueOf(feels_likeC);
                    String description = object.getString("description");
                    String city = response.getString("name");
                    String country = (String) sys_object.get("country");
                    String icon = object.getString("icon");
                    String pressure = String.valueOf(main_object.getDouble("pressure"));
                    String humidity = String.valueOf(main_object.getDouble("humidity"));

                    // mettre les valeurs dans les champs
                    mCity.setText(city+", "+country);
                    mTemp.setText(temp);
                    mDescription.setText(description);
                    mFeelsLike.setText("Feels like: " + feels_like  + "˚C");
                  //  mCountry.setText(country);

                    // formattage du temps
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM dd");
                    String formatted_date = simpleDateFormat.format(calendar.getTime());

                    // lever et coucher du soleil
                    long timeSs = sys_object.getLong("sunset");
                    long timeSr = sys_object.getLong("sunrise");
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    timeSs *= 1000;
                    timeSr *= 1000;
                    String formatted_sunrise = sdf.format(timeSr);
                    String formatted_sunset = sdf.format(timeSs);

                    mDate.setText(formatted_date);
                    mDaylightHours.setText("Daylight hours: " + formatted_sunrise + " - " + formatted_sunset + "\n" +
                            "Pressure: " + pressure + "\n" + "Humidity: " + humidity);
                    //gestion de l'image
                    String imageUri = "http://openweathermap.org/img/w/" + icon + ".png";
                    imgIcon = findViewById(R.id.imgIcon);
                    Uri myUri  = Uri.parse(imageUri);
                    Picasso.with(MainActivity.this).load(myUri).resize(200, 200).into(imgIcon);

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
       // ajouter tous les éléments à la queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }
}