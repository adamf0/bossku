package app.adam.bossku.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

import androidx.lifecycle.ViewModel;

import app.adam.bossku.view.ui.LoginActivity;
import app.adam.bossku.view.ui.RegisterActivity;

public class SelectRegisterViewModel extends ViewModel {
    @SuppressLint("StaticFieldLeak")
    public Activity act;

    public void register_user(){
        Intent i = new Intent(act, RegisterActivity.class);
        i.putExtra("type", 0);
        act.startActivity(i);
    }
    public void register_umkm(){
        Intent i = new Intent(act, RegisterActivity.class);
        i.putExtra("type", 1);
        act.startActivity(i);
    }
    public void back(){
        act.startActivity(new Intent(act, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
