package app.adam.bossku.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import app.adam.basiclibrary.BasicLib;
import app.adam.basiclibrary.TextListener;
import app.adam.bossku.R;
import app.adam.bossku.databinding.ActivityLoginBinding;
import app.adam.bossku.databinding.ActivitySelectRegisterBinding;
import app.adam.bossku.viewmodel.LoginViewModel;
import app.adam.bossku.viewmodel.SelectRegisterViewModel;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity implements LoginViewModel.Loginlistener {
    LoginViewModel model;
    ActivityLoginBinding binding;
    SweetAlertDialog dialog_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        model = new ViewModelProvider(this).get(LoginViewModel.class);
        model.setLoginlistener(this);
        LoginViewModel.lib = new BasicLib(this);
        binding.setModel(model.component);
        binding.setAction(this);
    }

    public void doLogin(){
        if(model.component.isValidation()){
            dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            dialog_loading.setTitleText("Loading");
            dialog_loading.setCancelable(false);
            dialog_loading.show();

            model.doLogin();
        }
        else{
            model.getLoginlistener().onErrorValidation(model.component.getMessage());
        }
    }
    @Override
    public void onErrorValidation(String message) {
        Snackbar.make(binding.layoutLogin,message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onFailAuth(String message) {
        dialog_loading.dismissWithAnimation();
        Snackbar.make(binding.layoutLogin,message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessAuth(String message) {
        dialog_loading.dismissWithAnimation();
        Snackbar.make(binding.layoutLogin,message,Snackbar.LENGTH_LONG).show();
        startActivity(new Intent(this, MainActivity.class));
    }
}