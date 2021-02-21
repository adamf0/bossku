package app.adam.bossku.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import androidx.lifecycle.ViewModel;

import app.adam.bossku.helper.HttpsTrustManager;
import app.adam.bossku.view.ui.LoginActivity;

public class SplashViewModel extends ViewModel {
    @SuppressLint("StaticFieldLeak")
    public Activity act;

    public void loading(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //HttpsTrustManager.allowAllSSL();
                act.startActivity(new Intent(act, LoginActivity.class));
                act.finish();
            }
        }, 6000L);
    }
}
