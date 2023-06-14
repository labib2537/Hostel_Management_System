package com.example.hostelofdiu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class complain_box_page extends AppCompatActivity implements View.OnClickListener {


    Button back_btn, submit_btn;
    String phone,complain_txt;
    TextInputEditText complain_box;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    session ssn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_complain_box_page);
        getSupportActionBar().hide();

        ssn = new session(this);
        HashMap<String, String> userDetails = ssn.getuserdetailFromSession();
        phone = userDetails.get(session.KEY_PHONENUMBER);

        back_btn= findViewById(R.id.complain_box_back_btn_id);
        submit_btn= findViewById(R.id.complain_box_page_submit_btn_id);
        complain_box= findViewById(R.id.complain_box_edtxt_id);

        back_btn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.complain_box_back_btn_id) {
            finish();
        }else if (view.getId() == R.id.complain_box_page_submit_btn_id) {
            complain_txt=complain_box.getText().toString().trim();
            if(complain_txt.equals("")){
                Toast.makeText(getApplicationContext(), "Write something first", Toast.LENGTH_LONG).show();
            }else {
                final String time=new Date().getTime()+"";
                Map<String, String> complains = new HashMap<>();
                complains.put("phone", phone);
                complains.put("complain_text", complain_txt);
                complains.put("time", time);
                db.collection("complain_box").add(complains).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Complained Successfully !", Toast.LENGTH_LONG).show();
                    }
                });
                finish();
            }
        }
    }
}