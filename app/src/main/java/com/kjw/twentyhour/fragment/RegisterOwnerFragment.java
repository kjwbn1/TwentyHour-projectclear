package com.kjw.twentyhour.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kjw.twentyhour.R;
import com.kjw.twentyhour.databinding.FragmentRegisterOwnerBinding;
import com.kjw.twentyhour.model.Owner;
import com.kjw.twentyhour.model.Response;
import com.kjw.twentyhour.model.User;
import com.kjw.twentyhour.network.NetworkUtil;
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
public class RegisterOwnerFragment extends Fragment {


    public static final String TAG = RegisterOwnerFragment.class.getSimpleName();
    FragmentRegisterOwnerBinding binding;
    CompositeSubscription mSubscriptions;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSubscriptions = new CompositeSubscription();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_owner, container,false);
        View view = binding.getRoot();
        binding.btnRegister.setOnClickListener(v -> register());
        binding.gotologin.setOnClickListener(v -> goToLogin());

        // Inflate the layout for this fragment
        return view;
    }

    private void register() {

        setError();

        String name = binding.etStoreName.getText().toString();
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();
        String businessCode = binding.etBusinessCode.getText().toString();

        int err = 0;

        if (!validateFields(name)) {

            err++;
            binding.tlStoreName.setError("Name should not be empty !");
        }

        if (!validateEmail(email)) {

            err++;
            binding.tlEmail.setError("Email should be valid !");
        }

        if (!validateFields(password)) {

            err++;
            binding.tlPwd.setError("Password should not be empty !");
        }

        if(!validateFields(businessCode)) {

            err++;
            binding.tlBusinessCode.setError("사업자번호가 비었습니다.");
        }


        if (err == 0) {

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);


            Owner owner = new Owner();
            owner.storeName = name;
            owner.email = email;
            owner.password = password;
            owner.businessCode = businessCode;

//            binding.progressBar2.setVisibility(View.VISIBLE);

            registerProcess(owner);

        } else {

            showSnackBarMessage("Enter Valid Details !");
        }
    }

    private void setError() {

        binding.tlStoreName.setError(null);
        binding.tlEmail.setError(null);
        binding.tlPwd.setError(null);
        binding.tlBusinessCode.setError(null);
    }

    private void registerProcess(Owner owner) {

        mSubscriptions.add(NetworkUtil.getRetrofit().registerOwner(owner)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Response response) {

        binding.progressBar2.setVisibility(View.GONE);
        showSnackBarMessage(response.getMessage());
    }

    private void handleError(Throwable error) {

        binding.progressBar2.setVisibility(View.GONE);

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

    private void goToLogin() {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        LoginFragment fragment = new LoginFragment();
        ft.replace(R.id.fragmentFrame, fragment, LoginFragment.TAG);
        ft.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

}
