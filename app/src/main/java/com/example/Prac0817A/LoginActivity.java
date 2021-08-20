package com.example.Prac0817A;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.librarytest.LibraryTest;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseAuth mAuth;
    private String getuser;

    private static final String TAG = MainActivity.class.toString();

    /**
     * asset 경로가 아니라면,
     * AndroidManifest.xml 에 INTERNET Permission 이 필요할 수 있음.
     */
    private String startPage = "file:///android_asset/contents/LOG/html/LOG0100.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        WebView webView = findViewById(R.id.webView);
        webView.addJavascriptInterface(this, "JSBridge");       //Bridge 추가(script에서 "window.JSBridge" 로 사용하겠다는 의미)
        WebSettings settings = webView.getSettings();                       //Javascript 활성화
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new CustomWebViewClient());                //url catch 등(현재로선 크게 필요한 내용이 아님)
        webView.setWebContentsDebuggingEnabled(true);                       //크롬 인스펙트 활성화
        webView.loadUrl(startPage);//웹뷰에 페이지 로드

        LibraryTest libraryTest = new LibraryTest();
        libraryTest.showToast(LoginActivity.this);
    }

    /**
     * JSBridge 로 호출될 메서드는
     * JavascriptInterface annotation 이 추가되어야함.
     *
     * @param id
     * @param password
     */
    @JavascriptInterface
    public void login(String id, String password) {
        Log.e(TAG, "id : " + id + ", pw : " + password);

        firebaseAuth = FirebaseAuth.getInstance();

        if (id.equals("") || password.equals("")) {
            Toast.makeText(LoginActivity.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT);
        } else {
            firebaseAuth.signInWithEmailAndPassword(id, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                                    @Override
                                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        if (user != null) {
                                            //로그인
                                            getuser = user.getEmail();
                                        } else {
                                            //로그아웃 상태
                                        }
                                    }
                                };
                                // 로그인 성공
                                Toast.makeText(LoginActivity.this, getuser + "님 로그인 성공", Toast.LENGTH_SHORT).show();
                                firebaseAuth.addAuthStateListener(firebaseAuthListener);
                                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                startActivity(intent);
                            } else {
                                // 로그인 실패
                                Toast.makeText(LoginActivity.this, "잘못된 정보입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    }//

    /*//로그인 한 상태라면 리스너를 불러서
    //해당 리스터의 내용을 실행하는데
    //authStateListener 안에는 인텐트가 담겨있다.
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
        Log.w("","onstart");
    }
    @Override
    protected void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
        Log.w("","onstop");
    }*/

    class CustomWebViewClient extends WebViewClient {

        /**
         * url 이 변경될 때 호출
         *
         * @param view
         * @param url
         * @return
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        /**
         * 페이지 로드 시작 시 호출
         *
         * @param view
         * @param url
         * @param favicon
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        /**
         * 페이지 로드 완료 시 호출
         *
         * @param view
         * @param url
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }
}