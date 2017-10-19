package io.hasura.orange;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import static com.facebook.Profile.getCurrentProfile;

public class MainActivity extends BaseActivity {
    LoginButton loginButton;
    CallbackManager callbackManager;

    public static void startActivity(Activity startingActivity){
        Intent intent = new Intent(startingActivity, MainActivity.class);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Profile.getCurrentProfile()!=null) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        callbackManager = CallbackManager.Factory.create();
        loginButton= (LoginButton) findViewById(R.id.login_button);

        setLoginPermissions();


    }

    void setLoginPermissions(){
        loginButton.setReadPermissions("email","public_profile","user_friends");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AD.dismiss();
                        HomeActivity.startActivity(MainActivity.this, true,"");
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this, "Request Cancelled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}


