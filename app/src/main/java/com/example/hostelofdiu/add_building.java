package com.example.hostelofdiu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class add_building extends AppCompatActivity implements View.OnClickListener {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button back_btn, add_building;
    AppCompatButton pick_image_btn;
    TextInputLayout building_name_edt, floor_edt, room_in_floor_edt, details,building_for;
    ImageSlider add_building_image_slider;
    ArrayList<SlideModel> imageList = new ArrayList<>();
    ArrayList<Uri> imag_array = new ArrayList<>();
    ArrayList<String> imageUrls = new ArrayList<>();
    String image_uri_str="";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_add_building);
        getSupportActionBar().hide();

        building_name_edt = findViewById(R.id.add_building_page_building_name_edttxt_id);
        floor_edt = findViewById(R.id.add_building_page_building_flor_edttxt_id);
        room_in_floor_edt = findViewById(R.id.add_building_page_building_room_edttxt_id);
        details = findViewById(R.id.add_building_page_building_description_edttxt_id);
        building_for= findViewById(R.id.add_building_page_building_for_id);
        add_building_image_slider= findViewById(R.id.add_building_page_image_slider);
        pick_image_btn= findViewById(R.id.add_building_page_add_sliding_image_id);
        imageList.add(new SlideModel(R.drawable.gallery_icon, ScaleTypes.CENTER_INSIDE));
        add_building_image_slider.setImageList(imageList);

        back_btn = findViewById(R.id.add_building_page_back_btn_id);
        add_building = findViewById(R.id.add_building_page_add_building_btn_id);

        back_btn.setOnClickListener(this);
        add_building.setOnClickListener(this);
        pick_image_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_building_page_back_btn_id) {
            finish();
        }
        if (view.getId() == R.id.add_building_page_add_sliding_image_id) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 10);

        }
        else if (view.getId() == R.id.add_building_page_add_building_btn_id) {
            if (!validate_building_name_floor_room()) {
                return;
            }  if (imag_array.isEmpty()) {
                Toast.makeText(this, "Image can't be empty!", Toast.LENGTH_SHORT).show();
                return;
            }else {
                String floors = floor_edt.getEditText().getText().toString().trim();
                String rooms = room_in_floor_edt.getEditText().getText().toString().trim();
                String building_name = building_name_edt.getEditText().getText().toString().trim();
                String building_details = details.getEditText().getText().toString().trim();
                String building_for_str =  building_for.getEditText().getText().toString().trim();


                CompletableFuture<Void> uploadCompletionFuture = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uploadCompletionFuture = new CompletableFuture<>();
                }

                for (int i = 0; i < imag_array.size(); i++) {
                    StorageReference storageRef = storage.getReference().child("buildings")
                            .child(String.valueOf(System.currentTimeMillis()));
                    UploadTask uploadTask = storageRef.putFile(imag_array.get(i));

                    CompletableFuture<Void> finalUploadCompletionFuture = uploadCompletionFuture;
                    uploadTask.continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return storageRef.getDownloadUrl();
                    }).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String downloadUri = task.getResult().toString();
                            image_uri_str=image_uri_str+" "+downloadUri;
                            imageUrls.add(downloadUri);
                            if (imageUrls.size() == imageList.size()) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    finalUploadCompletionFuture.complete(null);
                                }
                            }
                        }
                    });
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uploadCompletionFuture.thenAccept((Void) -> {
                        db.collection("buildings").whereEqualTo("building_name", building_name).get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if (queryDocumentSnapshots.isEmpty()) {
                                            int rooms_int = Integer.parseInt(rooms);
                                            int floors_int = Integer.parseInt(floors);
                                            int total_rooms = rooms_int * floors_int;
                                            Map<String, String> building = new HashMap<>();
                                            building.put("building_name", building_name);
                                            building.put("total_flor", floors);
                                            building.put("total_room_inflor", rooms);
                                            building.put("available_rooms", total_rooms + "");
                                            building.put("building_details", building_details);
                                            building.put("building_for", building_for_str);
                                            building.put("first_image", imageUrls.get(0));
                                            building.put("building_room_imags", image_uri_str);
                                            db.collection("buildings").add(building);
                                            Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "There is a Building already exist with that Name !", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    });
                }

            }

        }
    }

    private Boolean validate_building_name_floor_room() {
        String val_0 = floor_edt.getEditText().getText().toString().trim();
        String val_1 = room_in_floor_edt.getEditText().getText().toString().trim();
        String val_2 = building_name_edt.getEditText().getText().toString().trim();
        String val_3 = details.getEditText().getText().toString().trim();
        String val_4 = building_for.getEditText().getText().toString().trim();

        if (val_2.isEmpty()) {
            building_name_edt.setError("Field can not be empty");
            return false;
        } else if (val_0.isEmpty()) {
            floor_edt.setError("Field can not be empty");
            return false;
        } else if (val_1.isEmpty()) {
            room_in_floor_edt.setError("Field can not be empty");
            return false;
        } else if (val_3.isEmpty()) {
            details.setError("Field can not be empty");
            return false;
        } else if (val_4.isEmpty()) {
            building_for.setError("Field can not be empty");
            return false;
        } else {
            floor_edt.setError(null);
            room_in_floor_edt.setError(null);
            building_name_edt.setError(null);
            details.setError(null);
            building_for.setError(null);
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 10 && data.getClipData() != null) {
                try {
                    imageList.clear();
                    imag_array.clear();
                    imageUrls.clear();
                    image_uri_str="";
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        imag_array.add(data.getClipData().getItemAt(i).getUri());
                        imageList.add(new SlideModel(data.getClipData().getItemAt(i).getUri().toString(), ScaleTypes.CENTER_CROP));
                    }

                }catch (Exception e){
                    System.out.println(e.getMessage().toString());
                }
            }
        add_building_image_slider.setImageList(imageList);
    }

}