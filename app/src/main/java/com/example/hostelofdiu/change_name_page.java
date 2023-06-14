package com.example.hostelofdiu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class change_name_page extends AppCompatActivity implements View.OnClickListener {

    String phone,type;
    Button back_btn,save_btn;
    TextInputLayout name_edttxt;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    session ssn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_change_name_page);
        getSupportActionBar().hide();

        ssn = new session(this);
        HashMap<String, String> userDetails = ssn.getuserdetailFromSession();
        phone = userDetails.get(session.KEY_PHONENUMBER);
        type = userDetails.get(session.KEY_TYPE);


        back_btn = findViewById(R.id.edtname_back_btn_id);
        save_btn= findViewById(R.id.edtname_save_btn_id);
        name_edttxt= findViewById(R.id.edtname_username_edtxt_id);

        if(type.equals("admin")){
            db.collection("admin").whereEqualTo("phone",phone)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot document:queryDocumentSnapshots){
                                name_edttxt.getEditText().setText(document.getString("name"));
                            }
                        }
                    });
        }else {
            db.collection("users").whereEqualTo("phone",phone)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot document:queryDocumentSnapshots){
                                name_edttxt.getEditText().setText(document.getString("name"));
                            }
                        }
                    });
        }

        back_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.edtname_back_btn_id) {
           finish();
        }else  if (view.getId() == R.id.edtname_save_btn_id) {
            if (!validate_name()) {
                return;
            }else {
                String given_name = name_edttxt.getEditText().getText().toString().trim();
                ssn = new session(this);
                if(type.equals("admin")){
                    db.collection("admin").document(phone).update("name", given_name);
                    ssn.createLoginSession(given_name, phone,"admin");
                }else {
                    db.collection("users").document(phone).update("name", given_name);
                    db.collection("users").document(phone).update("name_lower", given_name.toLowerCase());
                    ssn.createLoginSession(given_name, phone,"user");
                }
                Toast.makeText(getApplicationContext(), "Successfully Edited", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("extra_return", given_name);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
    private Boolean validate_name() {
        String val = name_edttxt.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            name_edttxt.setError("Field can not be empty");
            return false;
        } else if (val.length() < 4) {
            name_edttxt.setError("Must be more then 3 character");
            return false;
        } else if (!(Character.isUpperCase(val.charAt(0)))) {
            name_edttxt.setError("First latter must be upper case");
            return false;
        } else {
            name_edttxt.setError(null);
            return true;
        }
    }
}