package com.example.hostelofdiu;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class building_details extends AppCompatActivity implements View.OnClickListener {

    TextView building_name,description;
    Button back_btn, delete_btn;
    ImageButton delete_img;
    String type, building_id, building_name_str;
    session ssn;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageSlider building_image_slider;
    floors_adapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_building_details);
        getSupportActionBar().hide();

        ssn = new session(this);
        HashMap<String, String> userDetails = ssn.getuserdetailFromSession();
        type = userDetails.get(session.KEY_TYPE);

        building_id = getIntent().getStringExtra("building_id_key");
        building_name_str = getIntent().getStringExtra("building_name_key");

        building_name = findViewById(R.id.bldng_dtls_name_id);
        back_btn = findViewById(R.id.bldng_dtls_back_btn_id);
        recyclerView = findViewById(R.id.bldng_dtls_recvw_id);
        delete_btn = findViewById(R.id.bldng_dtls_delete_btn_id);
        delete_img = findViewById(R.id.bldng_dtls_delete_img_btn_id);
        description = findViewById(R.id.bldng_dtls_dscrptn);
        building_image_slider= findViewById(R.id.building_details_page_image_slider);
        if (ssn.checkLogin()) {
            if (!type.equals("admin")) {
                delete_btn.setVisibility(View.GONE);
                delete_img.setVisibility(View.GONE);
            }
        } else {
            delete_btn.setVisibility(View.GONE);
            delete_img.setVisibility(View.GONE);
        }
        db.collection("buildings").document(building_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String name, floors,image_uri_str,des;
                    name = documentSnapshot.getString("building_name");
                    floors = documentSnapshot.getString("total_flor");
                    des = documentSnapshot.getString("building_details");
                    image_uri_str=documentSnapshot.getString("building_room_imags").trim();
                    building_name.setText("Building : " + name);
                    description.setText(des);
                    int flrs = Integer.parseInt(floors);
                    int[] flr_arr = new int[flrs];
                    for (int i = 0; i < flrs; i++) {
                        flr_arr[i] = i + 1;
                    }
                    ArrayList<SlideModel> imageList = new ArrayList<>();
                    String[] img_list_str=image_uri_str.split(" ");
                    for (int i = 0; i < img_list_str.length; i++) {
                        imageList.add(new SlideModel(img_list_str[i], ScaleTypes.CENTER_CROP));
                    }
                    building_image_slider.setImageList(imageList);
                    adapter = new floors_adapter(flr_arr, getApplicationContext(), building_id);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        delete_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bldng_dtls_back_btn_id) {
            finish();
        } else if (view.getId() == R.id.bldng_dtls_delete_btn_id) {
            showDeleteDialogue();
        }
    }

    private void showDeleteDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you Really want to Delete this Building ?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                Html.fromHtml("<font color='#004383'>Yes</font>"),
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onClick(DialogInterface dialog, int id) {
                        final String time = new Date().getTime() + "";
                        LocalDate myObj = LocalDate.now();
                        final String date = myObj + "";
                        db.collection("occupied_rooms")
                                .whereEqualTo("building_name", building_name_str)
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                                String request_id = document.getId();
                                                HashMap<String, String> new_info = new HashMap<>();
                                                new_info.put("application_condition", "leaved");
                                                new_info.put("application_time", time);
                                                new_info.put("building_name", building_name_str);
                                                new_info.put("floor_number", document.getString("floor_number"));
                                                new_info.put("leaving_date", date);
                                                new_info.put("occupation_date", document.getString("occupation_date"));
                                                new_info.put("requeste_for", "");
                                                new_info.put("room_number", document.getString("room_number"));
                                                new_info.put("uer_phone", document.getString("uer_phone"));
                                                new_info.put("user_name", document.getString("user_name"));
                                                db.collection("occupied_rooms").document(request_id).set(new_info);
                                            }
                                        }
                                    }
                                });
                        db.collection("buildings").document(building_id).delete();
                        Toast.makeText(getApplicationContext(), "Successfully Deleted", Toast.LENGTH_SHORT).show();
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