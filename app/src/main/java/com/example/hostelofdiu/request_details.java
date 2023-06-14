package com.example.hostelofdiu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class request_details extends AppCompatActivity implements View.OnClickListener {

    Button apply_btn, back_btn;
    ImageView profile_imgvw;
    session ssn;
    String phone, name, id, building_name, floor_position, room_position;
    TextView name_txtvw, phone_txtvw, building_name_txtvw, floor_number_txtvw, room_number_txtvw;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_request_details);
        getSupportActionBar().hide();

        ssn = new session(this);
        HashMap<String, String> userDetails = ssn.getuserdetailFromSession();
        phone = userDetails.get(session.KEY_PHONENUMBER);
        name = userDetails.get(session.KEY_NAME);

        id = getIntent().getStringExtra("building_id_key");
        floor_position = getIntent().getStringExtra("floor_position_id_key");
        room_position = getIntent().getStringExtra("room_position_id_key");
        building_name = getIntent().getStringExtra("building_name_id_key");

        name_txtvw = findViewById(R.id.details_page_profile_name_id);
        name_txtvw.setText(name);
        phone_txtvw = findViewById(R.id.details_page_phone_txtvw_id);
        phone_txtvw.setText("Phone : " + phone);
        building_name_txtvw = findViewById(R.id.details_page_building_name_txtvw_id);
        building_name_txtvw.setText("Building Name : : " + building_name);
        floor_number_txtvw = findViewById(R.id.details_page_floor_number_txtvw_id);
        floor_number_txtvw.setText("Floor Number : " + floor_position);
        room_number_txtvw = findViewById(R.id.details_page_room_number_txtvw_id);
        room_number_txtvw.setText("Room Number : " + room_position);
        profile_imgvw = findViewById(R.id.details_page_profile_img_id);

        db.collection("users").document(phone)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            if (!documentSnapshot.getString("profile_img").equals("")) {
                                Picasso.get()
                                        .load(documentSnapshot.getString("profile_img"))
                                        .placeholder(R.drawable.ic_baseline_head)
                                        .into(profile_imgvw);
                            }
                        }
                    }
                });

        apply_btn = findViewById(R.id.details_page_apply_btn_id);
        back_btn = findViewById(R.id.details_page_back_btn_id);


        back_btn.setOnClickListener(this);
        apply_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.details_page_back_btn_id) {
            finish();
        } else if (view.getId() == R.id.details_page_apply_btn_id) {
            Map<String, String> apply = new HashMap<>();
            final String time=new Date().getTime()+"";
            apply.put("user_name", name);
            apply.put("uer_phone", phone);
            apply.put("building_name", building_name);
            apply.put("floor_number", floor_position);
            apply.put("room_number", room_position);
            apply.put("application_time", time);
            apply.put("leaving_date", "");
            apply.put("occupation_date", "");
            apply.put("requeste_for", "occupy");
            apply.put("application_condition", "requested");
            db.collection("occupied_rooms").add(apply);
            db.collection("admin").document("+8801748388446")
                    .update("new_requests", FieldValue.increment(1));
            Toast.makeText(getApplicationContext(), "Successfull !", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}