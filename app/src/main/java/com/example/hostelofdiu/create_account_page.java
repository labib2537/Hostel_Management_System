package com.example.hostelofdiu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class create_account_page extends AppCompatActivity implements View.OnClickListener {

    Button back_btn, register_btn;
    TextInputLayout full_name,user_phone, password1, password2;
    Intent intent;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_create_account_page);
        getSupportActionBar().hide();

        back_btn = findViewById(R.id.createaccount_back_bnt_id);
        register_btn = findViewById(R.id.createaccount_register_btn_id);
        full_name = findViewById(R.id.createaccount_username_edtxt_id);
        user_phone = findViewById(R.id.createaccount_phone_edtxt_id);
        password1 = findViewById(R.id.createaccount_password_1_edtxt_id);
        password2 = findViewById(R.id.createaccount_password_2_edtxt_id);

        back_btn.setOnClickListener(this);
        register_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.createaccount_back_bnt_id) {
            finish();
        } else if (view.getId() == R.id.createaccount_register_btn_id) {
            if (!validate_name() | !validate_phone() | !validate_password_1() | !validate_password_2()) {
                return;
            }

            String _user_name = full_name.getEditText().getText().toString().trim();
            String _user_phone = user_phone.getEditText().getText().toString().trim();
            String _phoneN0 = "+88" + _user_phone;
            String _user_password = password2.getEditText().getText().toString().trim();

            db.collection("users")
                    .whereEqualTo("phone", _phoneN0)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                showHaveAccountDialogue();

                            } else {
                                intent = new Intent(getApplicationContext(), create_account_otp.class);
                                intent.putExtra("phn", _phoneN0);
                                intent.putExtra("name", _user_name);
                                intent.putExtra("pswrd", _user_password);
                                startActivity(intent);
                            }
                        }
                    });
        }
    }

    private Boolean validate_name() {
        String val = full_name.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            full_name.setError("Field can not be empty");
            return false;
        } else if (val.length() < 4) {
            full_name.setError("Must be more then 3 character");
            return false;
        } else if (!(Character.isUpperCase(val.charAt(0)))) {
            full_name.setError("First latter must be upper case");
            return false;
        } else {
            full_name.setError(null);
            return true;
        }
    }

    private Boolean validate_phone() {
        String val = user_phone.getEditText().getText().toString().trim();
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
        } else {
            user_phone.setError(null);
            return true;
        }
    }

    private Boolean validate_password_1() {
        String val = password1.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            password1.setError("Field can not be empty");
            return false;
        } else if (val.length() < 9) {
            password1.setError("Must be more then 8 character");
            return false;
        } else {
            password1.setError(null);
            return true;
        }
    }

    private Boolean validate_password_2() {
        String val_1 = password1.getEditText().getText().toString().trim();
        String val_2 = password2.getEditText().getText().toString().trim();
        if (val_2.isEmpty()) {
            password2.setError("Field can not be empty");
            return false;
        } else if (!val_1.equals(val_2)) {
            password2.setError("Password doesn't match with previous one");
            return false;
        } else {
            password2.setError(null);
            return true;
        }
    }

    private void showHaveAccountDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You already have an Account with that phone number !\nTry with another Phone Number");
        builder.setCancelable(true);

        builder.setPositiveButton(
                Html.fromHtml("<font color='#004383'>OK</font>"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

}