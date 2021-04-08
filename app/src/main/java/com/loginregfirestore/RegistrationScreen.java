package com.loginregfirestore;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegistrationScreen extends Fragment {
    EditText frgname, frgemail, frgcity, frgpassword, frgdob;
    Button btnRegister;
    TextView tvLogin;
    FirebaseFirestore db;
    ProgressDialog loadingBar;
    FirebaseAuth fAuth;
    RadioGroup radioGroup;
    RadioButton radioMale, radioFemale;

    int mYear,mMonth,mDay;
    String DAY,MONTH,YEAR;
    View view;

    public RegistrationScreen() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.screen_register, container, false);


        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        radioMale = (RadioButton) view.findViewById(R.id.radioMale);
        radioFemale = (RadioButton) view.findViewById(R.id.radioFemale);

        frgname = (EditText) view.findViewById(R.id.frgname);
        frgpassword = (EditText) view.findViewById(R.id.frgpassword);
        frgemail = (EditText) view.findViewById(R.id.frgemail);
        frgdob = (EditText) view.findViewById(R.id.frgdob);
        frgcity = (EditText) view.findViewById(R.id.frgcity);
        frgdob.setFocusable(false);

        frgdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker();
            }
        });

        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        loadingBar = new ProgressDialog(getContext());
        selectGender();
        btnRegister=(Button)view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Createrecipeprocess();
            }
        });
    return view;
    }

    private void Createrecipeprocess() {

        String name = frgname.getText().toString().trim();
        String email = frgemail.getText().toString().trim();
        String password = frgpassword.getText().toString().trim();
        String city = frgcity.getText().toString().trim();
        String dob = frgdob.getText().toString().trim();
        int selectedId = radioGroup.getCheckedRadioButtonId();


        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "Name should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }if (selectedId == -1) {
            Toast.makeText(getContext(), "Please Choose Gender", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Email should not be empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Password should not be empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(city)) {
            Toast.makeText(getContext(), "City name should not be empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(dob)) {
            Toast.makeText(getContext(), "Date of Birth should not be empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() < 6) {
            Toast.makeText(getContext(), "Password Must be >= 6 Characters", Toast.LENGTH_SHORT).show();
            return;
        } else {
            loadingBar.setTitle("Please Wait");
            loadingBar.setMessage("Please wait,  while we are adding details.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            addingNewUser(name, email, password,city,dob);
        }

    }

    String UserID;

    private void addingNewUser(final String name, final String email, final String password, final String city, final String dob) {

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("Registered_users").document(UserID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("name_field", name);
                    user.put("email_field", email);
                    user.put("password_field", password);
                    user.put("city_field", city);
                    user.put("birthdate_field", dob);
                    user.put("gender_field", gender);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "User Register Succussfully.", Toast.LENGTH_SHORT).show();
                            resetFields();
                            loadingBar.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingBar.dismiss();
                            Toast.makeText(getContext(), "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    String gender;
    private void selectGender() {
        if (radioFemale.isChecked()) {
            gender = "Female";

        } else {
            gender = "Male";


        }
    }

    public void datepicker() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        DAY = dayOfMonth + "";
                        MONTH = monthOfYear + 1 + "";
                        YEAR = year + "";

                        frgdob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void resetFields(){

        frgname.getText().clear();
        frgemail.getText().clear();
        frgpassword.getText().clear();
        frgdob.getText().clear();
        frgcity.getText().clear();
        radioMale.setChecked(false);
        radioFemale.setChecked(false);


    }

}