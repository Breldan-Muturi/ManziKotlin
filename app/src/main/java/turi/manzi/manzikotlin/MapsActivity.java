package turi.manzi.manzikotlin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    LocationManager locationManager;

    LocationListener locationListener;

    private GoogleMap mMap;

    public void centerMapOnLocation(Location location, String title){

        if(location != null) {

            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

            mMap.clear();

            mMap.addMarker(new MarkerOptions().position(userLocation).title(title));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                centerMapOnLocation(lastKnownLocation,"Your Location");

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);

        Intent intent = getIntent();

        Toast.makeText(this, Integer.toString(intent.getIntExtra("placeNumber",0)), Toast.LENGTH_SHORT).show();

        if((intent.getIntExtra("placeNumber",0)) == 0 ){
//            Zoom in on user location

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            locationListener = new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {

                    centerMapOnLocation(location, "Your Location");

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,1, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                centerMapOnLocation(lastKnownLocation,"Your Location");

            }else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }

        } else {

            Location placeLocation = new Location(LocationManager.GPS_PROVIDER);

            placeLocation.setLatitude(PlacesActivity.locations.get(intent.getIntExtra("placeNumber",0)).latitude);

            placeLocation.setLongitude(PlacesActivity.locations.get(intent.getIntExtra("placeNumber",0)).longitude);

            centerMapOnLocation(placeLocation, PlacesActivity.places.get(intent.getIntExtra("placeNumber",0)));

        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        String address = "";

        try{

            List<Address> listAddresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);

            if(listAddresses != null && listAddresses.size() > 0){

                if(listAddresses.get(0).getThoroughfare() != null){

                    if(listAddresses.get(0).getSubThoroughfare() != null)

                        address += listAddresses.get(0).getSubThoroughfare() + " ";

                }

                address += listAddresses.get(0).getThoroughfare() + " ";

            }

        }catch(Exception e){
            e.printStackTrace();
        }

        if(address.equals("")){

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");

            address += sdf.format(new Date());

        }

        mMap.addMarker(new MarkerOptions().position(latLng).title(address));

        PlacesActivity.places.add(address);

        PlacesActivity.locations.add(latLng);

        PlacesActivity.arrayAdapter.notifyDataSetChanged();

        SharedPreferences sharedPreferences = this.getSharedPreferences("turi.mycompanyapp.memorableplaces",Context.MODE_PRIVATE);

        try{

            ArrayList<String> latitudes = new ArrayList<>();

            ArrayList<String> longitudes = new ArrayList<>();

            for(LatLng coord: PlacesActivity.locations){

                latitudes.add(Double.toString(coord.latitude));

                longitudes.add(Double.toString(coord.longitude));

            }

            sharedPreferences.edit().putString("places", ObjectSerializer.serialize(PlacesActivity.places)).apply();

            sharedPreferences.edit().putString("lats", ObjectSerializer.serialize(latitudes)).apply();

            sharedPreferences.edit().putString("longs", ObjectSerializer.serialize(longitudes)).apply();

        }catch (Exception e){

            e.printStackTrace();

        }

        Toast.makeText(this, "Location Saved!", Toast.LENGTH_SHORT).show();

    }
}
