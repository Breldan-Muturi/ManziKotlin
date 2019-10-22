package turi.manzi.manzikotlin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class PlacesActivity extends AppCompatActivity {

    static ArrayList<String> places = new ArrayList<String>();

    static ArrayList<LatLng> locations = new ArrayList<LatLng>();

    static  ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        SharedPreferences sharedPreferences = this.getSharedPreferences("turi.manzi.manzikotlin", Context.MODE_PRIVATE);

        ArrayList<String> latitudes = new ArrayList<>();

        ArrayList<String> longitudes = new ArrayList<>();

        places.clear();

        latitudes.clear();

        longitudes.clear();

        locations.clear();

        try{

            places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places", ObjectSerializer.serialize(new ArrayList<String>())));

            latitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lats", ObjectSerializer.serialize(new ArrayList<String>())));

            longitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("longs", ObjectSerializer.serialize(new ArrayList<String>())));

        }catch (Exception e){

            e.printStackTrace();

        }

        if(places.size() > 0 && latitudes.size() > 0 && longitudes.size() > 0){

            if(places.size() == latitudes.size() && places.size() == longitudes.size()){

                for(int i = 0 ; i < longitudes.size(); i++){

                    locations.add(new LatLng(Double.parseDouble(latitudes.get(i)),Double.parseDouble(longitudes.get(i))));

                }

            }

        } else {

            places.add("Add a new place ....");

            locations.add(new LatLng(0,0));
        }

        ListView listView = (ListView) findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, places);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(PlacesActivity.this, Integer.toString(i), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);

                intent.putExtra("placeNumber", i);

                startActivity(intent);

            }
        });
    }
}
