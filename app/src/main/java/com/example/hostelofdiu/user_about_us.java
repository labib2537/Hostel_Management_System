package com.example.hostelofdiu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class user_about_us extends AppCompatActivity implements View.OnClickListener {
    Button back_btn;
    TextView textView;
    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_user_about_us);
        getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();

        back_btn = findViewById(R.id.usr_abtus_back_btn_id);
        textView = findViewById(R.id.abt_us_txtvw_id);

        db.collection("about_us")
                .document("about_us")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        textView.setText(documentSnapshot.getString("txts").trim());
                    }
                });
        back_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.usr_abtus_back_btn_id) {
            finish();
        }
    }
}