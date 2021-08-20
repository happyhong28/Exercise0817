package com.example.Prac0817A;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private Button btn_signin,btn_tosignup;
    private EditText et_signinemail;
    private EditText et_signinpw;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private String getuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();


        //로그인 버튼
        btn_signin = (Button) findViewById(R.id.btn_Signin);
        et_signinemail = (EditText) findViewById(R.id.et_SigninEmail);
        et_signinpw = (EditText) findViewById(R.id.et_SigninPw);


        //로그인버튼을 눌렀을때
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = et_signinemail.getText().toString();
                String pw = et_signinpw.getText().toString();

                // 관리자 로그인인
               if(id.equals("admin@ttt.com") && pw.equals("123456") ) {
                    Toast.makeText(MainActivity.this, "관리자 로그인", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(intent);
                }else if(id.equals("") || pw.equals("")){
                   Toast.makeText(MainActivity.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT);
               }else{
                   loginUser(id, pw);
               }
            }
        });


        btn_tosignup = (Button) findViewById(R.id.btn_ToSignup);

        //회원가입 버튼을 눌렀을 때
        btn_tosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);

            }
        });
    }


    public void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                                @Override
                                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if (user != null) {
                                        //로그인
                                        getuser = user.getEmail();
                                    } else {
                                        //로그아웃 상태
                                    }
                                }
                            };
                            // 로그인 성공
                            Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            firebaseAuth.addAuthStateListener(firebaseAuthListener);
                            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                            startActivity(intent);
                        } else {
                            // 로그인 실패
                            Toast.makeText(MainActivity.this, "잘못된 정보입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}