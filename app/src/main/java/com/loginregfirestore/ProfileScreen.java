package com.loginregfirestore;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class ProfileScreen extends Fragment {
    EditText frgname, frgemail, frgcity, frgpassword, frgdob;
    ProgressDialog loadingBar;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    String userId;
    RadioGroup radioGroup;
    RadioButton radioMale, radioFemale;


    public ProfileScreen() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_profile_fragment, viewGroup, false);
        frgemail = (EditText) view.findViewById(R.id.frgemail);
        frgpassword = (EditText) view.findViewById(R.id.frgpassword);
        frgname = (EditText) view.findViewById(R.id.frgname);
        frgdob = (EditText) view.findViewById(R.id.frgdob);
        frgcity = (EditText) view.findViewById(R.id.frgcity);


        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        radioMale = (RadioButton)view. findViewById(R.id.radioMale);
        radioFemale = (RadioButton)view. findViewById(R.id.radioFemale);
        getUserData();
        return view;
    }

    public void getUserData() {
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Registered_users").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    frgemail.setText(documentSnapshot.getString("password_field"));
                    frgname.setText(documentSnapshot.getString("name_field"));
                    frgpassword.setText(documentSnapshot.getString("email_field"));
                    frgcity.setText(documentSnapshot.getString("city_field"));
                    frgdob.setText(documentSnapshot.getString("birthdate_field"));
                    if(documentSnapshot.getString("gender_field").equals("Male")){
                        radioMale.setChecked(true);
                    }
                    else {
                        radioFemale.setChecked(true);
                    }


                } else {
                    Toast.makeText(getContext(), "Somethig went wrong please try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}