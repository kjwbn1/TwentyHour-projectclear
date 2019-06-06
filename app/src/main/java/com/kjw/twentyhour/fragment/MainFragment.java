package com.kjw.twentyhour.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.widget.Toolbar;
import com.kjw.twentyhour.R;
import com.kjw.twentyhour.dialogs.StoreSelectionDialog;
import com.kjw.twentyhour.view.MenuSelectionView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    private CardView sirenOrder;
    private CardView membership;
    private CardView creditCard;
    private TextView selectedStoreName;
    boolean selectedStore;
    Toolbar toolbar;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
        preferenceInit();



        String shortestStore = preferences.getString("shortestStore","매장선택안됨");
        selectedStoreName.setText(shortestStore);

        Bundle bundle = new Bundle();
        bundle.putString("shortestStore", shortestStore);

//        editor.clear();




        if (preferences.getBoolean("selectedStore",false)) {


        } else {

            StoreSelectionDialog storeSelectionDialog = new StoreSelectionDialog();
            storeSelectionDialog.setArguments(bundle);
            storeSelectionDialog.show(getFragmentManager(), "aa");
        }




        setClickListener();
        return view;
    }

    private void preferenceInit() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preferences.edit();


        selectedStoreName.setText(preferences.getString("shortestStore", "매장을 선택하세요."));
    }

    private void initView(View view) {
        sirenOrder = (CardView) view.findViewById(R.id.siren_order);
        membership = (CardView) view.findViewById(R.id.membership);
        creditCard = (CardView) view.findViewById(R.id.credit_card);
        selectedStoreName = (TextView) view.findViewById(R.id.selected_store_name);


    }

    private void setClickListener() {
        sirenOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MenuSelectionView.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
