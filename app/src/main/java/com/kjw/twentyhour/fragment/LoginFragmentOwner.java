package com.kjw.twentyhour.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kjw.twentyhour.OwnerConsoleActivity;
import com.kjw.twentyhour.R;
import com.kjw.twentyhour.databinding.FragmentLoginFragmentOwnerBinding;
import com.kjw.twentyhour.model.Response;
import com.kjw.twentyhour.network.NetworkUtil;
import com.kjw.twentyhour.utils.Constants;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import java.io.IOException;

import static com.kjw.twentyhour.utils.Validation.validateEmail;
import static com.kjw.twentyhour.utils.Validation.validateFields;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragmentOwner extends Fragment {

    final static public String TAG = LoginFragmentOwner.class.getSimpleName();


    FragmentLoginFragmentOwnerBinding binding;
    CompositeSubscription mSubscriptions;
    SharedPreferences mSharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mSubscriptions = new CompositeSubscription();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_fragment_owner, container, false);
        binding.setFragment(this);
        View view = binding.getRoot();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.btnLogi.setOnClickListener(v -> login());
        binding.gotoregister.setOnClickListener(v -> goToRegister());
        binding.tvFindPassword.setOnClickListener(v -> showDialog());


        return view;
    }

    private void login() {

        setError();

        String email = binding.inputEmail.getText().toString();
        String password = binding.inputPassword.getText().toString();


        int err = 0;

        if (!validateEmail(email)) {

            err++;
            binding.inputEmail.setError("유효하지 않는 이메일");
        }

        if (!validateFields(password)) {

            err++;
            binding.inputPassword.setError("비밀번호가 비어 있습니다.");
        }

        if (err == 0) {

            loginProcess(email, password);
            binding.progressBar.setVisibility(View.VISIBLE);

        } else {

            showSnackBarMessage("Enter Valid Details !");
        }


    }

    private void setError() {

        binding.inputEmail.setError(null);
        binding.inputPassword.setError(null);

    }

    private void loginProcess(String email, String password) {

        mSubscriptions.add(NetworkUtil.getRetrofit(email, password).login()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Response response) {

        binding.progressBar.setVisibility(View.GONE);

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.TOKEN, response.getToken());
        editor.putString(Constants.EMAIL, response.getMessage());
        editor.apply();

        binding.inputEmail.setText(null);
        binding.inputPassword.setText(null);


        Intent intent = new Intent(getActivity(), OwnerConsoleActivity.class);
        startActivity(intent);


    }

    private void handleError(Throwable error) {

        binding.progressBar.setVisibility(View.GONE);

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody, Response.class);
                showSnackBarMessage(response.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            showSnackBarMessage("Network Error !");
        }
    }

    private void showSnackBarMessage(String message) {

        if (getView() != null) {

            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void goToRegister() {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        RegisterOwnerFragment fragment = new RegisterOwnerFragment();
        ft.replace(R.id.fragmentFrame, fragment, RegisterOwnerFragment.TAG).addToBackStack(LoginFragment.TAG);
        ft.commit();
    }

    private void showDialog() {

        ResetPasswordDialog fragment = new ResetPasswordDialog();

        fragment.show(getFragmentManager(), ResetPasswordDialog.TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }


}

