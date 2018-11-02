package com.kjw.twentyhour;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.kjw.twentyhour.utils.Constants;

import static com.kjw.twentyhour.utils.Validation.validateFields;


public class SplashActivity extends AppCompatActivity {


    private SharedPreferences mSharedPreferences;

    String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mToken = mSharedPreferences.getString(Constants.TOKEN,"");





        if(!validateFields(mToken)){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();

        } else {
            Intent intents = new Intent(this, StateActivity.class);
            startActivity(intents);
            finish();
        }







    }
}
