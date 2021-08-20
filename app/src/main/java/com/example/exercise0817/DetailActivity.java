package com.example.exercise0817;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    private TextView tv_detail;
    private Button btn_send;
    String nmArray,roadnmArray,zipArray;
    int markerId;
    double logtArray,latArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tv_detail = findViewById(R.id.tv_detail);
        btn_send = findViewById(R.id.btn_send);

        Bundle extras = getIntent().getExtras();
        markerId = extras.getInt("markerId");
        logtArray = extras.getDouble("logtArray");
        latArray = extras.getDouble("latArray");
        nmArray = extras.getString("nmArray");
        roadnmArray = extras.getString("roadnmArray");
        zipArray = extras.getString("zipArray");


        tv_detail.setText("- 위도 : "+latArray+"\n"+"- 경도 : "+logtArray+"\n"+"- 상호명 : "+nmArray+"\n"+"- 도로명주소 : "+roadnmArray+"\n"+"- 우편번호 : "+zipArray+"\n");
        String sendToB = markerId+"#"+String.valueOf(latArray)+"#"+String.valueOf(logtArray)+"#"+nmArray+"#"+roadnmArray+"#"+zipArray;


        //버튼 클릭시 데이터 전송
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, sendToB);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

            }
        });

    }
}
