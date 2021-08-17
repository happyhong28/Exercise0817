package com.example.exercise0817;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.exercise0817.databinding.ActivityMapsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.io.InputStream;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String jsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        String json = "";
        try {
            InputStream is = getAssets().open("data.json"); // json파일 이름
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            //json파일명을 가져와서 String 변수에 담음
            json = new String(buffer, "UTF-8");
            //Log.d("--  json = ", json);


            JSONObject jsonObject = new JSONObject(json);

            //배열로된 자료를 가져올때
            JSONArray Array = jsonObject.getJSONArray("Ducklbrd");//배열의 이름
            JSONObject Object = Array.getJSONObject(1);

            for(int i=0; i<Array.length(); i++)
            {

          /*      JSONObject Object = Array.getJSONObject(i);*/
                Log.d("-- 위도 ", Object.getString("REFINE_WGS84_LOGT"));
                Log.d("-- 경도 ", Object.getString("REFINE_WGS84_LAT"));


                String structured_formatting = Object.getString("row");
                Log.d("--  json = ", structured_formatting);
                JSONObject Object2 = Object.getJSONObject(structured_formatting);
                String logt = Object2.getString("REFINE_WGS84_LOGT");
                String lat = Object2.getString("REFINE_WGS84_LAT");

                Log.d("-- 위도 ",logt);
                Log.d("-- 위도 ",lat);
                // 위도 Latitude, 경도 longitude
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }





}