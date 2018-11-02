package com.kjw.twentyhour.fragment;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kjw.twentyhour.LoginActivity;
import com.kjw.twentyhour.R;
import com.kjw.twentyhour.model.User;
import com.kjw.twentyhour.network.NetworkUtil;
import com.kjw.twentyhour.utils.Constants;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.kjw.twentyhour.utils.Validation.validateFields;


public class ProfileFragment extends Fragment implements ChangePasswordDialog.Listener{

    public static final String TAG = ProfileFragment.class.getSimpleName();


    private TextView mTvName;
    private TextView mTvEmail;
    private TextView mTvDate;
    public Button mBtChangePassword;
    public Button mBtLogout;

    private ProgressBar mProgressbar;

    private String mToken;
    private String mEmail;


    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        mSubscriptions = new CompositeSubscription();
        initViews(view);
        initSharedPreferences();
        loadProfile();
        return view;


    }



    private void initViews(View v) {
        mTvName = (TextView) v.findViewById(R.id.tv_name);
        mTvEmail = (TextView) v.findViewById(R.id.tv_email);
        mTvDate = (TextView) v.findViewById(R.id.tv_date);
        mBtChangePassword = (Button) v.findViewById(R.id.btn_change_password);
        mBtLogout = (Button) v.findViewById(R.id.btn_logout);
        mProgressbar = (ProgressBar) v.findViewById(R.id.progress);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mToken = mSharedPreferences.getString(Constants.TOKEN,"");

        if(!validateFields(mToken)){


        } else{

            mBtLogout.setVisibility(View.VISIBLE);
            mBtChangePassword.setVisibility(View.VISIBLE);
        }



        mBtChangePassword.setOnClickListener(view -> showDialog());
        mBtLogout.setOnClickListener(view -> logout());





    }
    private void initSharedPreferences() {


        mToken = mSharedPreferences.getString(Constants.TOKEN,"");
        mEmail = mSharedPreferences.getString(Constants.EMAIL,"");
    }

    private void logout() {


        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.EMAIL,"");
        editor.putString(Constants.TOKEN,"");
        editor.apply();
//        Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
//        startActivity(intent);

        mProgressbar.setVisibility(View.VISIBLE);
        mTvEmail.setText("");
        mTvDate.setText("");
        mTvName.setText("로그인 하세요.");

        mBtLogout.setVisibility(View.INVISIBLE);
        mBtChangePassword.setVisibility(View.INVISIBLE);


        mProgressbar.setVisibility(View.GONE);

        mTvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra(Constants.TOKEN, mToken);
                startActivity(intent);


            }
        });






    }

    private void showDialog(){

        ChangePasswordDialog fragment = new ChangePasswordDialog();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.EMAIL, mEmail);
        bundle.putString(Constants.TOKEN,mToken);
        fragment.setArguments(bundle);

        fragment.show(getFragmentManager(), ChangePasswordDialog.TAG);
    }

    private void loadProfile() {

        mSubscriptions.add(NetworkUtil.getRetrofit(mToken).getProfile(mEmail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void handleResponse(User user) {

        mProgressbar.setVisibility(View.GONE);
        mTvName.setText(user.getName());
        mTvEmail.setText(user.getEmail());
        mTvDate.setText(user.getCreated_at());
    }

    private void handleError(Throwable error ) {

        mProgressbar.setVisibility(View.GONE);
        mBtLogout.setVisibility(View.INVISIBLE);
        mBtChangePassword.setVisibility(View.INVISIBLE);
        mTvName.setText("로그인 하세요.");

        mTvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);

                startActivity(intent);

            }

        });

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
//                Response response = gson.fromJson(errorBody,Response.class);
//                showSnackBarMessage(response.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

//            showSnackBarMessage("Network Error !");
        }
    }

    private void showSnackBarMessage(String message) {

//        Snackbar.make(findViewById(R.id.activity_profile),message,Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();

    }

    @Override
    public void onPasswordChanged() {

        showSnackBarMessage("Password Changed Successfully !");

    }
}


