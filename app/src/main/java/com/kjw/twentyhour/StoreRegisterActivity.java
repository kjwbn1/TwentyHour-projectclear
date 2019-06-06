package com.kjw.twentyhour;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreRegisterActivity extends AppCompatActivity {

    @BindView(R.id.store_name)
    TextInputLayout storeName;
    @BindView(R.id.store_identity)
    TextInputLayout storeIdentity;
    @BindView(R.id.sotre_address)
    TextInputLayout sotreAddress;
    @BindView(R.id.sotre_coordinate)
    TextInputLayout sotreCoordinate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_register);
        ButterKnife.bind(this);
    }
}
