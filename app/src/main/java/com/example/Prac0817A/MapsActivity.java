package com.example.Prac0817A;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.Prac0817A.databinding.ActivityMapsBinding;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.io.InputStream;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    public double[] logtArray, latArray;
    public String[] nmArray, zipArray, roadnmArray;
    //public String[][] infoArray;

    private long backBtnTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String json = "";
        try {
            InputStream is = getAssets().open("data.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
            //Log.w("--- json --- ", json);

            JSONObject jsonObject = new JSONObject(json);
            JSONArray D = jsonObject.getJSONArray("Ducklbrd");
            JSONObject ob = D.getJSONObject(1);
            JSONArray arr = ob.getJSONArray("row");

            nmArray = new String[arr.length()];
            zipArray = new String[arr.length()];
            logtArray = new double[arr.length()];
            latArray = new double[arr.length()];
            roadnmArray = new String[arr.length()];
            //infoArray = new String[arr.length()][5];


            for (int i = 0; i < arr.length(); i++) {
                JSONObject ob2 = arr.getJSONObject(i);

                logtArray[i] = ob2.getDouble("REFINE_WGS84_LOGT");
                latArray[i] = ob2.getDouble("REFINE_WGS84_LAT");
                nmArray[i] = ob2.getString("BIZPLC_NM");
                zipArray[i] = ob2.getString("REFINE_ZIP_CD");
                roadnmArray[i] = ob2.getString("REFINE_ROADNM_ADDR");


            }


        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }


    }//oncreate


    void handleSendText(Intent intent) {
        String sharedText1 = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText1 != null) {
            Log.w("", sharedText1);
            String[] splitText1 = sharedText1.split("#");

            int getmarkerId = Integer.parseInt(splitText1[0]);
            Log.w("",String.valueOf(getmarkerId));
            latArray[getmarkerId] = Double.parseDouble(splitText1[1]);
            logtArray[getmarkerId] = Double.parseDouble(splitText1[2]);
        }
    }//

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        // 화면(인텐트)가 B앱에서 넘어온 여부를 확인해서
        // 넘어온 상태이면 handlesendtext() 를 통해
        // 수정된 값으로 핀을 뿌려주고 그게 아니라면 원래 json에서 읽어온 정보로 핀으로 뿌리라는 의미
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else {
            }
        }

        for (int i = 0; i < nmArray.length; i++) {
            LatLng korea = new LatLng(latArray[i], logtArray[i]);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(korea)
                    .title(nmArray[i])
                    .snippet("상세정보를 확인하려면 마커를 클릭해주세요!")

            );
            marker.showInfoWindow();

            mMap.moveCamera(CameraUpdateFactory.newLatLng(korea));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(korea, 13));
        }

        //클릭시 상세화면 이동
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                int markerId = Integer.parseInt((marker.getId().substring(1)));

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("markerId", markerId);
                intent.putExtra("logtArray", logtArray[markerId]);
                intent.putExtra("latArray", latArray[markerId]);
                intent.putExtra("nmArray", nmArray[markerId]);
                intent.putExtra("roadnmArray", roadnmArray[markerId]);
                intent.putExtra("zipArray", zipArray[markerId]);
                startActivity(intent);
                return false;
            }

        });//

    }//onmapready


    @Override
    public void onBackPressed() {

        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MapsActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
            finish();
//            Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent1);
//            super.onBackPressed();

        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 로그아웃됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}