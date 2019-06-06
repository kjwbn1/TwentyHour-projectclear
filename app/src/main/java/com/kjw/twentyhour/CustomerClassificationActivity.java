package com.kjw.twentyhour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CustomerClassificationActivity extends AppCompatActivity {

    private Button btCustomerSelect;
    private Button btOwnerSelect;
    private Boolean isSelectedC = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_classification);


        btOwnerSelect = (Button)findViewById(R.id.btn_owner_select);
        btCustomerSelect = (Button)findViewById(R.id.btn_customer_select);



        btCustomerSelect.setOnClickListener(v -> {



            Intent intent = new Intent(this,LoginActivity.class);
            intent.putExtra("isSelectedC" , isSelectedC);
            startActivity(intent);



        });

        btOwnerSelect.setOnClickListener(v -> {

            Intent intent = new Intent(this,LoginActivity.class);
            intent.putExtra("isSelectedC" , !isSelectedC);
            startActivity(intent);
        });



    }
}
