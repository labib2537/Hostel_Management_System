package com.example.hostelofdiu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class forgot_password_phone_page extends AppCompatActivity implements View.OnClickListener {

    Button back_btn, next_btn;
    TextInputLayout phone_edttxt;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_forgot_password_phone_page);
        getSupportActionBar().hide();

        back_btn = findViewById(R.id.forgotpassword_back_bnt_id);
        next_btn = findViewById(R.id.forgotpassword_next_btn_id);
        phone_edttxt = findViewById(R.id.forgotpassword_phone_edtxt_id);

        back_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.forgotpassword_back_bnt_id) {
            finish();
        } else if (view.getId() == R.id.forgotpassword_next_btn_id) {
            if (!validate_phone()) {
                return;
            }

            String _user_phone=phone_edttxt.getEditText().getText().toString().trim();
            String _phoneN0="+88"+_user_phone;

            db.collection("users")
                    .whereEqualTo("phone", _phoneN0)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "No account exist with that phone number !", Toast.LENGTH_SHORT).show();
                            } else {
                                intent = new Intent(getApplicationContext(), forgot_password_otp.class);
                                intent.putExtra("phnf",_phoneN0);
                                startActivity(intent);
                            }
                        }
                    });
        }

    }
    private Boolean validate_phone() {
        String val=phone_edttxt.getEditText().getText().toString().trim();
        Pattern p = Pattern.compile("^\\d{11}$");
        Matcher m = p.matcher(val);
        if(val.isEmpty()){
            phone_edttxt.setError("Field can not be empty");
            return false;
        }
        else if (!m.matches()){
            phone_edttxt.setError("Must be 11 number");
            return false;
        }
        else if (val.charAt(0)!='0' || val.charAt(1)!='1'){
            phone_edttxt.setError("Must be start with 01");
            return false;
        }
        else {
            phone_edttxt.setError(null);
            return true;
        }
    }
}