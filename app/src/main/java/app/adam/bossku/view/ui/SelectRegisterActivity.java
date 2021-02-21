package app.adam.bossku.view.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import app.adam.bossku.R;
import app.adam.bossku.databinding.ActivitySelectRegisterBinding;
import app.adam.bossku.viewmodel.SelectRegisterViewModel;

public class SelectRegisterActivity extends AppCompatActivity {
    SelectRegisterViewModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySelectRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_select_register);
        model = new ViewModelProvider(this).get(SelectRegisterViewModel.class);
        model.act = this;
        binding.setViewModel(model);
    }
}