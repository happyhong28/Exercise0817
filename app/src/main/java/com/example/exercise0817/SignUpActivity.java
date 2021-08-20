package com.example.exercise0817;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText et_signupemail;
    private EditText et_signuppw;
    private EditText et_pw_check;
    private Button btn_signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_signupemail =  findViewById(R.id.et_SignupEmail);
        et_signuppw = findViewById(R.id.et_SignupPw);
        btn_signup = findViewById(R.id.btn_Signup);
        et_pw_check = findViewById(R.id.et_PwCheck);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!et_signupemail.getText().toString().equals("") && !et_signuppw.getText().toString().equals("") && et_pw_check.getText().toString().equals(et_signuppw.getText().toString()) ) {
                    // 이메일과 비밀번호가 공백이 아닌 경우 & 비밀번호와 비밀번호확인이 일치한 경우
                    createUser(et_signupemail.getText().toString(), et_signuppw.getText().toString());

                    Log.e("id",et_signupemail.getText().toString());
                    Log.e("pw",et_signuppw.getText().toString());
                } else {
                    // 이메일과 비밀번호가 공백인 경우
                    Toast.makeText(SignUpActivity.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });//

    }
    private void createUser(String email, String password) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공시
                            Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            //getActivity().finish();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        } else {
                            // 계정이 중복된 경우
                            Toast.makeText(SignUpActivity.this, "이미 존재하는 계정입니다. ", Toast.LENGTH_SHORT).show();
                            Log.e("id",et_signupemail.getText().toString());
                            Log.e("pw",et_signuppw.getText().toString());
                        }
                    }
                });
    }

}
