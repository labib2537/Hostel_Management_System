package com.example.hostelofdiu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class login_page extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout user_phone, password;
    TextView forgot_password_bnt, admin_login_btn;
    AppCompatButton login_btn, create_account_btn, back_btn;
    Intent intent;
    session ssn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_login_page);
        getSupportActionBar().hide();

        ssn = new session(this);
        if(ssn.checkLogin()){
            intent = new Intent(getApplicationContext(),home_page.class);
            startActivity(intent);
        }
        user_phone = findViewById(R.id.login_page_phone_number_id);
        password = findViewById(R.id.login_page_password_id);

        forgot_password_bnt = findViewById(R.id.login_page_forgot_password_id);
        admin_login_btn = findViewById(R.id.login_page_admin_login_id);
        login_btn = findViewById(R.id.login_page_login_btn_id);
        create_account_btn = findViewById(R.id.login_page_create_account_btn_id);
        back_btn = findViewById(R.id.login_page_back_btn_id);


        forgot_password_bnt.setOnClickListener(this);
        admin_login_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        create_account_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_page_back_btn_id) {
            finishAffinity();
        } else if (view.getId() == R.id.login_page_forgot_password_id) {
            intent = new Intent(view.getContext(), forgot_password_phone_page.class);
            view.getContext().startActivity(intent);
        } else if (view.getId() == R.id.login_page_admin_login_id) {
            intent = new Intent(view.getContext(), adminLogin_phone_page.class);
            view.getContext().startActivity(intent);
        } else if (view.getId() == R.id.login_page_login_btn_id) {
            if (!validate_phone_and_password()) {
                return;
            } else {
                String phonen = user_phone.getEditText().getText().toString().trim();
                String _phone = "+88" + phonen;
                String _password = password.getEditText().getText().toString().trim();

                db.collection("users")
                        .whereEqualTo("phone", _phone)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.isEmpty()) {
                                    showAccountDontexistDialogue();
                                } else {
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        user_phone.setError(null);
                                        user_phone.setErrorEnabled(false);
                                        String phon = document.getString("phone");
                                        String passwrd = document.getString("password");

                                        if (passwrd.equals(_password)) {
                                            password.setError(null);
                                            password.setErrorEnabled(false);

                                            String name = document.getString("name");

                                            session ssn = new session(login_page.this);
                                            ssn.createLoginSession(name, phon,"user");

                                            intent = new Intent(getApplicationContext(),home_page.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            showIncorrectPasswordDialogue();
                                        }
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
            }
        } else if (view.getId() == R.id.login_page_create_account_btn_id) {
            intent = new Intent(view.getContext(), create_account_page.class);
            view.getContext().startActivity(intent);
        }
    }


    private void showAccountDontexistDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("No account exist with that phone number !\nDo you want to create an account ?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                Html.fromHtml("<font color='#004383'>Yes</font>"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        intent = new Intent(getApplicationContext(), create_account_page.class);
                        startActivity(intent);
                    }
                });

        builder.setNegativeButton(
                Html.fromHtml("<font color='#004383'>No</font>"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }


    private void showIncorrectPasswordDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Password is Incorrect");
        builder.setCancelable(true);

        builder.setNegativeButton(
                Html.fromHtml("<font color='#004383'>Cancel</font>"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private Boolean validate_phone_and_password() {
        String val = user_phone.getEditText().getText().toString().trim();
        String val_2 = password.getEditText().getText().toString().trim();
        Pattern p = Pattern.compile("^\\d{11}$");
        Matcher m = p.matcher(val);
        if (val.isEmpty()) {
            user_phone.setError("Field can not be empty");
            return false;
        } else if (!m.matches()) {
            user_phone.setError("Must be 11 number");
            return false;
        } else if (val.charAt(0) != '0' || val.charAt(1) != '1') {
            user_phone.setError("Must be start with 01");
            return false;
        } else if (val_2.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        } else {
            user_phone.setError(null);
            password.setError(null);
            return true;
        }
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}