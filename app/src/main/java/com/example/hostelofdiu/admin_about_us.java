package com.example.hostelofdiu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class admin_about_us extends AppCompatActivity implements View.OnClickListener {

    Button back_btn, submit_btn;
    String phone,abtus_txt;
    TextInputEditText abtus_box;
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_admin_about_us);
        getSupportActionBar().hide();

        back_btn= findViewById(R.id.admin_abtus_back_btn_id);
        submit_btn= findViewById(R.id.abtus_page_submit_btn_id);
        abtus_box= findViewById(R.id.abtus_edtxt_id);

        db.collection("about_us")
                .document("about_us")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        abtus_box.setText(documentSnapshot.getString("txts").trim());
                    }
                });

        back_btn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.admin_abtus_back_btn_id) {
            finish();
        }
        else if (view.getId() == R.id.abtus_page_submit_btn_id) {
            abtus_txt=abtus_box.getText().toString().trim();
            if(abtus_txt.equals("")){
                Toast.makeText(getApplicationContext(), "Write something first", Toast.LENGTH_LONG).show();
            }else {
                db.collection("about_us").document("about_us").update("txts",abtus_txt);
                Toast.makeText(getApplicationContext(), "Successfully Submitted !", Toast.LENGTH_LONG).show();
            }
        }
    }
}