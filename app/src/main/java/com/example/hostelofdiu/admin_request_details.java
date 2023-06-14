package com.example.hostelofdiu;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

public class admin_request_details extends AppCompatActivity implements View.OnClickListener {

    Button back_btn, accept_btn, reject_btn;
    ImageView profile_imgvw;
    String phone, request_id, requested_for, building_name, floor_position, room_position, application_time, application_condition;
    TextView name_txtvw, phone_txtvw, building_name_txtvw, floor_number_txtvw, room_number_txtvw,
            date_txtvw, application_condition_txtvw;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_admin_request_details);
        getSupportActionBar().hide();


        floor_position = getIntent().getStringExtra("floor_position_id_key");
        room_position = getIntent().getStringExtra("room_position_id_key");
        building_name = getIntent().getStringExtra("building_name_id_key");
        application_time = getIntent().getStringExtra("occupation_date_id_key");
        application_condition = getIntent().getStringExtra("condition_id_key");
        request_id = getIntent().getStringExtra("request_id_key");
        requested_for = getIntent().getStringExtra("requested_for_id_key");
        phone = getIntent().getStringExtra("user_phone_id_key");

        name_txtvw = findViewById(R.id.aplctn_dtls_page_name_txtvw_id);
        phone_txtvw = findViewById(R.id.aplctn_dtls_page_phone_txtvw_id);
        phone_txtvw.setText("Phone : " + phone);
        building_name_txtvw = findViewById(R.id.aplctn_dtls_page_building_name_txtvw_id);
        building_name_txtvw.setText("Building Name : : " + building_name);
        floor_number_txtvw = findViewById(R.id.aplctn_dtls_page_floor_txtvw_id);
        floor_number_txtvw.setText("Floor Number : " + floor_position);
        room_number_txtvw = findViewById(R.id.aplctn_dtls_page_room_txtvw_id);
        room_number_txtvw.setText("Room Number : " + room_position);
        date_txtvw = findViewById(R.id.aplctn_dtls_page_time_txtvw_id);
        if (!application_time.equals("")) {
            long timestamp = Long.parseLong(application_time);
            Date date = new Date(timestamp);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = formatter.format(date);
            date_txtvw.setText("Date of Application : " + formattedDate);

        }
        application_condition_txtvw = findViewById(R.id.aplctn_dtls_page_condition_txtvw_id);
        if (application_condition.equals("requested") && requested_for.equals("occupy")) {
            application_condition_txtvw.setBackgroundResource(R.drawable.custom_button_2_1);
            application_condition_txtvw.setText("Requested for Room");
        } else if (application_condition.equals("requested") && requested_for.equals("leave")) {
            application_condition_txtvw.setBackgroundResource(R.drawable.custom_button_2_1);
            application_condition_txtvw.setText("Requested for leave");
        }
        profile_imgvw = findViewById(R.id.aplctn_dtls_page_profile_img_id);
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
                            name_txtvw.setText(documentSnapshot.getString("name"));
                        }
                    }
                });


        accept_btn = findViewById(R.id.aplctn_dtls_page_accept_btn_id);
        reject_btn = findViewById(R.id.aplctn_dtls_page_reject_btn_id);
        back_btn = findViewById(R.id.aplctn_dtls_page_back_btn_id);

        back_btn.setOnClickListener(this);
        profile_imgvw.setOnClickListener(this);
        name_txtvw.setOnClickListener(this);
        accept_btn.setOnClickListener(this);
        reject_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.aplctn_dtls_page_back_btn_id) {
            finish();
        } else if (view.getId() == R.id.aplctn_dtls_page_accept_btn_id) {
            showAcceptDialogue();
        } else if (view.getId() == R.id.aplctn_dtls_page_reject_btn_id) {
            showRejectDialogue();
        }
        else if (view.getId() == R.id.aplctn_dtls_page_profile_img_id) {
            Intent intent = new Intent(view.getContext(), profile_view.class);
            intent.putExtra("user_phone_id_key",phone);
            view.getContext().startActivity(intent);
        }else if (view.getId() == R.id.aplctn_dtls_page_name_txtvw_id) {
            Intent intent = new Intent(view.getContext(), profile_view.class);
            intent.putExtra("user_phone_id_key",phone);
            view.getContext().startActivity(intent);
        }
    }

    private void showAcceptDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you Really want to 'Accept' this Application ?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                Html.fromHtml("<font color='#004383'>Yes</font>"),
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onClick(DialogInterface dialog, int id) {
                        LocalDate myObj = LocalDate.now();
                        final String time = myObj + "";
                        if (application_condition.equals("requested") && requested_for.equals("occupy")) {
                            db.collection("occupied_rooms").document(request_id).update("application_condition", "occupied");
                            db.collection("occupied_rooms").document(request_id).update("requeste_for", "");
                            db.collection("occupied_rooms").document(request_id).update("occupation_date", time);
                            db.collection("occupied_rooms").document(request_id).update("application_time", new Date().getTime()+"");
                        } else if (application_condition.equals("requested") && requested_for.equals("leave")) {
                            db.collection("occupied_rooms").document(request_id).update("application_condition", "leaved");
                            db.collection("occupied_rooms").document(request_id).update("requeste_for", "");
                            db.collection("occupied_rooms").document(request_id).update("leaving_date", time);
                            db.collection("occupied_rooms").document(request_id).update("application_time", new Date().getTime()+"");
                        }
                        finish();
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

    private void showRejectDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you Really want to 'Leave' this Room ?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                Html.fromHtml("<font color='#004383'>Yes</font>"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.collection("occupied_rooms").document(request_id).update("application_condition", "rejected");
                        db.collection("occupied_rooms").document(request_id).update("application_time", new Date().getTime()+"");
                        finish();
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