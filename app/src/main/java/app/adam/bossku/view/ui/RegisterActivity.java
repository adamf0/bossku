package app.adam.bossku.view.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import app.adam.basiclibrary.BasicLib;
import app.adam.bossku.R;
import app.adam.bossku.databinding.ActivityRegisterBinding;
import app.adam.bossku.viewmodel.RegisterViewModel;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity implements RegisterViewModel.RegisterListener, AdapterView.OnItemClickListener {
    RegisterViewModel model;
    ActivityRegisterBinding binding;
    SweetAlertDialog dialog_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        model = new ViewModelProvider(this).get(RegisterViewModel.class);

        RegisterViewModel.lib = new BasicLib(this);
        model.setRegisterListener(this);
        binding.setModel(model.component);
        binding.setViewModel(model);
        binding.setAction(this);

        Bundle b = getIntent().getExtras();
        if(b != null){
            model.isUMKM =  b.getInt("type");
            if(model.isUMKM==1)
                binding.tilTipeUmkmRegister.setVisibility(View.VISIBLE);
        }
        binding.edtTipeUmkmRegister.setOnItemClickListener(this);

        model.agree.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean val) {
                binding.btnDaftarRegister.setAlpha(val ? 1.0f:0.5f);
                binding.btnDaftarRegister.setEnabled(val);
            }
        });

    }

    public void register(){
        if(model.component.isValidation()) {
            dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            dialog_loading.setTitleText("Loading");
            dialog_loading.setCancelable(false);
            dialog_loading.show();

            model.register();
        }
        else{
            model.getRegisterListener().onErrorValidation(model.component.getMessage());
        }
    }
    public void back(){
        startActivity(new Intent(this, SelectRegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @Override
    public void onErrorValidation(String message) {
        Snackbar.make(binding.layoutRegister,message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessRegister(String message) {
        dialog_loading.dismissWithAnimation();
        Snackbar.make(binding.layoutRegister,message,Snackbar.LENGTH_LONG).show();

        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void onFailRegister(String message) {
        dialog_loading.dismissWithAnimation();
        Snackbar.make(binding.layoutRegister,message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        String selected = (String) arg0.getAdapter().getItem(arg2);
        if(!selected.equals("Pilih Kategori"))
            model.component.setKategori(selected);
    }
}