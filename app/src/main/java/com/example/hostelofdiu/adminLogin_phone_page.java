package com.example.hostelofdiu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class adminLogin_phone_page extends AppCompatActivity implements View.OnClickListener {
    Button back_btn, next_btn;
    TextInputLayout phone_edttxt, password_edttxt;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_admin_login_phone_page);
        getSupportActionBar().hide();

        back_btn = findViewById(R.id.adminlogin_back_bnt_id);
        next_btn = findViewById(R.id.adminlogin_next_btn_id);
        phone_edttxt = findViewById(R.id.adminlogin_phone_edtxt_id);
        password_edttxt = findViewById(R.id.admin_login_page_password_id);

        back_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.adminlogin_back_bnt_id) {
            finish();
        } else if (view.getId() == R.id.adminlogin_next_btn_id) {
            if (!validate_phone_and_password()) {
                return;
            } else {
                String phonen = phone_edttxt.getEditText().getText().toString().trim();
                String _phone = "+88" + phonen;
                String _password = password_edttxt.getEditText().getText().toString().trim();

                db.collection("admin")
                        .whereEqualTo("phone", _phone)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.isEmpty()) {
                                    showAccountDontexistDialogue();
                                } else {
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        phone_edttxt.setError(null);
                                        phone_edttxt.setErrorEnabled(false);
                                        String phon = documentSnapshot.getString("phone");
                                        String passwrd = documentSnapshot.getString("password");

                                        if (passwrd.equals(_password)) {
                                            phone_edttxt.setError(null);
                                            phone_edttxt.setErrorEnabled(false);

                                            String name = documentSnapshot.getString("name");

                                            session ssn = new session(adminLogin_phone_page.this);
                                            ssn.createLoginSession(name, phon, "admin");

                                            intent = new Intent(getApplicationContext(), home_page.class);
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
        String val = phone_edttxt.getEditText().getText().toString().trim();
        String val_2 = password_edttxt.getEditText().getText().toString().trim();
        Pattern p = Pattern.compile("^\\d{11}$");
        Matcher m = p.matcher(val);
        if (val.isEmpty()) {
            phone_edttxt.setError("Field can not be empty");
            return false;
        } else if (!m.matches()) {
            phone_edttxt.setError("Must be 11 number");
            return false;
        } else if (val.charAt(0) != '0' || val.charAt(1) != '1') {
            phone_edttxt.setError("Must be start with 01");
            return false;
        } else if (val_2.isEmpty()) {
            password_edttxt.setError("Field can not be empty");
            return false;
        } else {
            phone_edttxt.setError(null);
            password_edttxt.setError(null);
            return true;
        }
    }
}