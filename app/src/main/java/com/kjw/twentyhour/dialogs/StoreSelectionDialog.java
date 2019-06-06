package com.kjw.twentyhour.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kjw.twentyhour.R;
import com.kjw.twentyhour.fragment.MainFragment;
import com.kjw.twentyhour.listener.StoreBranch;
import com.kjw.twentyhour.listener.StoreData;

import java.util.zip.Inflater;

public class StoreSelectionDialog extends DialogFragment {



    ImageView imgStore;

    TextView storeBrand;

    TextView storeName;

    TextView storeDescription;

    LinearLayout dialogStoreSelection;

    Button select;

    Button later;

    String shortestStore;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        shortestStore = bundle.getString("shortestStore");




    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_store_selection,null);



        select = (Button)dialogView.findViewById(R.id.select);
        later = (Button)dialogView.findViewById(R.id.later);
        storeBrand = (TextView)dialogView.findViewById(R.id.store_brand);
        storeDescription = (TextView)dialogView.findViewById(R.id.store_description);
        storeName = (TextView)dialogView.findViewById(R.id.store_name);

        StoreData storeData = new StoreData();
        StoreBranch storeBranch = new StoreBranch(storeData, shortestStore);
        storeData.notifyObservers();

        storeDescription.setText(storeBranch.brandDescription);
        storeBrand.setText(storeBranch.brandName);
        storeName.setText(shortestStore);


        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(1000, 2000 );
//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams wlp = window.getAttributes();
//        wlp.gravity = Gravity.CENTER;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        window.setAttributes(wlp);
//        window
// .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        window.setGravity(Gravity.CENTER);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedPreferences =PreferenceManager.getDefaultSharedPreferences(getContext());
                editor = sharedPreferences.edit();
                editor.putBoolean("selectedStore", true);
                editor.commit();

                dismiss();
            }
        });

        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putBoolean("selectedStore", false);
                editor.commit();


                dismiss();
            }
        });



        return dialog;
    }

    @Override
    public void onResume() {

        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        getDialog().getWindow().setLayout(width,height);
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);


        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();



    }
}