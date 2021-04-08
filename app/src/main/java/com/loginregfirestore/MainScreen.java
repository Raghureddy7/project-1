package com.loginregfirestore;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainScreen extends AppCompatActivity {

    Button loginFragmentbtn, regFragmentbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View view) {

                Fragment fragment = null;
                if (view == findViewById(R.id.loginFragmentbtn)) {
                    fragment = new LoginScreen();
                } else {
                    fragment = new RegistrationScreen();
                }
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frameLayout, fragment);
                transaction.commit();
            }
        };


        loginFragmentbtn = (Button) findViewById(R.id.loginFragmentbtn);
        loginFragmentbtn.setSelected(true);
        loginFragmentbtn.setOnClickListener(listener);

        regFragmentbtn = (Button) findViewById(R.id.regFragmentbtn);
        regFragmentbtn.setOnClickListener(listener);
    }
}