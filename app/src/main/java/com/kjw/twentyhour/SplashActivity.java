package com.kjw.twentyhour;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.PresetReverb;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.kjw.twentyhour.utils.Constants;

import static com.kjw.twentyhour.utils.Validation.validateFields;


public class SplashActivity extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String mToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        mToken = preferences.getString(Constants.TOKEN , "");

        if(!validateFields(mToken)) {


            Intent intent = new Intent(this, CustomerClassificationActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this, StateActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
