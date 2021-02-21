package app.adam.bossku.view.ui;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import app.adam.bossku.R;
import app.adam.bossku.databinding.ActivitySplashBinding;
import app.adam.bossku.viewmodel.SplashViewModel;

public class SplashActivity extends AppCompatActivity {
    SplashViewModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
        ActivitySplashBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        model = new ViewModelProvider(this).get(SplashViewModel.class);
        model.act = this;
        binding.setViewModel(model);

        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        binding.imgLogoSplash.startAnimation(animFadeIn);
        binding.txtTitleSplash.startAnimation(animFadeIn);
        model.loading();
    }

}