package app.adam.bossku.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import app.adam.bossku.R;

public class AkunActivity extends AppCompatActivity {
    TextInputEditText edt_email,edt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);

        edt_email = findViewById(R.id.edt_email_akun);
        edt_password = findViewById(R.id.edt_password_akun);
        Toolbar mtoolbar = findViewById(R.id.toolbar_akun);

        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AkunActivity.super.onBackPressed();
            }
        });

    }
}