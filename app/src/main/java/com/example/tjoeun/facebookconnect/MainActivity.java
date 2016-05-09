package com.example.tjoeun.facebookconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    String TAG ="facebook";

    private  CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext()); // SDK를 사용할 때 기본적으로 호출
        // setContentView 이전에 호출 한다.
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();
        final LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager,new FacebookCallback<LoginResult>(){
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG,"페이스북 토큰->" + loginResult.getAccessToken().getToken());
                Log.d(TAG,"페이스북 UserID->" + loginResult.getAccessToken().getUserId());
                Log.d(TAG,"페이스북 UserID->" + loginResult.getAccessToken().getApplicationId());
                Toast.makeText(getApplicationContext(),"로그인 성공",Toast.LENGTH_LONG);
            }

            @Override
            public void onCancel() {
                Log.d(TAG,"cancel");
                Toast.makeText(getApplicationContext(),"로그인 취소",Toast.LENGTH_LONG);
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void loginFacebook(){
            //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(TAG,"페이스북 토큰->" + loginResult.getAccessToken().getToken());
                    Log.d(TAG,"페이스북 UserID->" + loginResult.getAccessToken().getUserId());

                    Toast.makeText(getApplicationContext(),"로그인 성공",Toast.LENGTH_LONG);

                    GraphRequest graphRequest;

                    // 아래 코드를 작성하지 않으면 JSONOBject나 Array가 이상한 값을 로드해옴.
                    GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(AccessToken.getCurrentAccessToken(),"/v2.6/me/feed", null, HttpMethod.GET, new GraphRequest.Callback(){
                        @Override
                        public void onCompleted(GraphResponse response) {

                        }
                    }).executeAsync();

                  /*  graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.d(TAG, "페이스북 로그인 결과 "+response.toString());

                            try{
                                String email = object.getString("email");
                                String name = object.getString("name");
                                String gender = object.getString("gender");

                                Log.d(TAG, "페이스북 이메일 : "+email);
                                Log.d(TAG, "페이스북 아이디 : "+name);
                                Log.d(TAG, "페이스북 성별 : "+gender);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields","id,name,email,gender,birthday");
                    graphRequest.setParameters(parameters);
                    graphRequest.executeAsync();*/
                }
                @Override
                public void onCancel() {
                    Log.d(TAG,"cancel");
                    Toast.makeText(getApplicationContext(),"로그인 취소",Toast.LENGTH_LONG);
                }
                @Override
                public void onError(FacebookException error) {
                    error.printStackTrace();
                }
            });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
