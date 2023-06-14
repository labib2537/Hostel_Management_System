package com.example.hostelofdiu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;

public class myRoom_details extends AppCompatActivity implements View.OnClickListener {

    Button back_btn, cancel_btn;
    ImageView profile_imgvw;
    session ssn;
    String phone, name, request_id, requested_for, building_name, floor_position, room_position,
            occupation_date,leaving_date, application_condition,from;
    TextView name_txtvw, phone_txtvw, building_name_txtvw, floor_number_txtvw, room_number_txtvw,
            ocdate_txtvw,ledate_txtvw, application_condition_txtvw;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_my_room_details);
        getSupportActionBar().hide();

        ssn = new session(this);
        HashMap<String, String> userDetails = ssn.getuserdetailFromSession();
        phone = userDetails.get(session.KEY_PHONENUMBER);
        name = userDetails.get(session.KEY_NAME);


        from = getIntent().getStringExtra("from_id_key");
        floor_position = getIntent().getStringExtra("floor_position_id_key");
        room_position = getIntent().getStringExtra("room_position_id_key");
        building_name = getIntent().getStringExtra("building_name_id_key");
        occupation_date = getIntent().getStringExtra("occupation_date_id_key");
        leaving_date = getIntent().getStringExtra("leaving_date_id_key");
        application_condition = getIntent().getStringExtra("condition_id_key");
        request_id = getIntent().getStringExtra("request_id_key");
        requested_for = getIntent().getStringExtra("requested_for_id_key");

        name_txtvw = findViewById(R.id.my_room_details_page_profile_name_id);
        name_txtvw.setText(name);
        phone_txtvw = findViewById(R.id.my_room_details_page_phone_txtvw_id);
        phone_txtvw.setText("Phone : " + phone);
        building_name_txtvw = findViewById(R.id.my_room_details_page_building_name_txtvw_id);
        building_name_txtvw.setText("Building Name : : " + building_name);
        floor_number_txtvw = findViewById(R.id.my_room_details_page_floor_number_txtvw_id);
        floor_number_txtvw.setText("Floor Number : " + floor_position);
        room_number_txtvw = findViewById(R.id.my_room_details_page_room_number_txtvw_id);
        room_number_txtvw.setText("Room Number : " + room_position);
        ocdate_txtvw= findViewById(R.id.my_room_details_page_ocdate_txtvw_id);
        ledate_txtvw = findViewById(R.id.my_room_details_page_levdate_txtvw_id);
        if (!occupation_date.equals("")) {
            ocdate_txtvw.setText("Date of Occupation : " + occupation_date);
        }
        if (!leaving_date.equals("")) {
            ledate_txtvw.setText("Date of leave : " + leaving_date);
        }
        application_condition_txtvw = findViewById(R.id.my_room_details_page_condition_txtvw_id);
        if (application_condition.equals("requested") && requested_for.equals("occupy")) {
            application_condition_txtvw.setBackgroundResource(R.drawable.custom_button_2_1);
            application_condition_txtvw.setText("Requested for Room");
        } else if (application_condition.equals("requested") && requested_for.equals("leave")) {
            application_condition_txtvw.setBackgroundResource(R.drawable.custom_button_2_1);
            application_condition_txtvw.setText("Requested for leave");
        } else if (application_condition.equals("leaved")) {
            application_condition_txtvw.setBackgroundResource(R.drawable.custom_button_2_2);
            application_condition_txtvw.setText("leaved");
        }else if (application_condition.equals("rejected")) {
            application_condition_txtvw.setBackgroundResource(R.drawable.custom_button_2_2);
            application_condition_txtvw.setText("rejected");
        }  else {
            application_condition_txtvw.setBackgroundResource(R.drawable.custom_button_2_2);
            application_condition_txtvw.setText("Occupied");
        }
        profile_imgvw = findViewById(R.id.my_room_details_page_profile_img_id);
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


        cancel_btn = findViewById(R.id.my_room_details_page_cancel_btn_id);
        if(from.equals("history")){
            cancel_btn.setVisibility(View.GONE);
        }
        back_btn = findViewById(R.id.my_room_details_page_back_btn_id);
        back_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.my_room_details_page_back_btn_id) {
            finish();
        } else if (view.getId() == R.id.my_room_details_page_cancel_btn_id) {
            if (requested_for.equals("occupy") || requested_for.equals("")) {
                showCancelRoomDialogue();
            } else if (application_condition.equals("requested") && requested_for.equals("leave")) {
                Toast.makeText(getApplicationContext(), "You already have Requested", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showCancelRoomDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you Really want to 'Leave' this Room ?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                Html.fromHtml("<font color='#004383'>Yes</font>"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (application_condition.equals("requested") && requested_for.equals("occupy")) {
                            db.collection("occupied_rooms").document(request_id).delete();
                        } else if (application_condition.equals("occupied") && requested_for.equals("")) {
                            final String time = new Date().getTime() + "";
                            db.collection("occupied_rooms").document(request_id).update("application_condition", "requested");
                            db.collection("occupied_rooms").document(request_id).update("application_time", time);
                            db.collection("occupied_rooms").document(request_id).update("requeste_for", "leave");
                            finish();
                        }
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
}