package com.example.hostelofdiu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class profile_page extends AppCompatActivity implements View.OnClickListener {
    Button back_btn;
    ImageView profile_img;
    TextView name,phone,edit_name_txtvw;
    String _phone, type,urlp;
    RecyclerView recyclerView;
    session ssn;
    ArrayList<myRoom_model> history_list;
    profile_history_adapter adapter;
    FirebaseStorage storage;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_profile_page);
        getSupportActionBar().hide();

        ssn = new session(this);
        HashMap<String, String> userDetails = ssn.getuserdetailFromSession();
        _phone = userDetails.get(session.KEY_PHONENUMBER);
        type = userDetails.get(session.KEY_TYPE);

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        back_btn= findViewById(R.id.profile_back_btn_id);
        profile_img= findViewById(R.id.profile_img_id);
        name= findViewById(R.id.profile_name_id);
        phone= findViewById(R.id.profile_phone_id);
        edit_name_txtvw = findViewById(R.id.profile_name_edt_id);
        recyclerView= findViewById(R.id.profile_recvw_id);

        history_list = new ArrayList<>();
        adapter = new profile_history_adapter(history_list, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        db.collection("occupied_rooms")
                .orderBy("application_time", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list) {
                            if(documentSnapshot.getString("uer_phone").equals(_phone)){
                                myRoom_model obj = documentSnapshot.toObject(myRoom_model.class);
                                history_list.add(obj);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });


        if(type.equals("admin")){
            db.collection("admin")
                    .whereEqualTo("phone", _phone)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                urlp = document.getString("profile_img");
                                if (!urlp.equals("")) {
                                    Picasso.get()
                                            .load(urlp)
                                            .placeholder(R.drawable.ic_baseline_head)
                                            .into(profile_img);
                                }
                                name.setText(document.getString("name"));
                                phone.setText("Phone : "+document.getString("phone"));
                            }
                        }
                    });
        }else {
            db.collection("users")
                    .whereEqualTo("phone", _phone)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                urlp = document.getString("profile_img");
                                if (!urlp.equals("")) {
                                    Picasso.get()
                                            .load(urlp)
                                            .placeholder(R.drawable.ic_baseline_head)
                                            .into(profile_img);
                                }
                                name.setText(document.getString("name"));
                                phone.setText("Phone : "+document.getString("phone"));
                            }
                        }
                    });
        }

        back_btn.setOnClickListener(this);
        profile_img.setOnClickListener(this);
        edit_name_txtvw.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.profile_back_btn_id) {
            Intent intent = new Intent(view.getContext(), home_page.class);
            view.getContext().startActivity(intent);
        }else  if (view.getId() == R.id.profile_name_edt_id) {
            Intent intent = new Intent(view.getContext(), change_name_page.class);
            startActivityForResult(intent, 11);
        }else if (view.getId() == R.id.profile_img_id) {
            BottomSheetDialog sheetDialog = new BottomSheetDialog(this,
                    R.style.BottomSheetDialogTheme);
            View sheetView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.profile_bottomsheet,findViewById(R.id.profile_img_bottomsheet));
            sheetView.findViewById(R.id.profile_img_bottomsheet_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (urlp.equals("")) {
                        Toast.makeText(getApplicationContext(), "No Profile Image exist !", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(profile_page.this, full_img_show.class);
                        intent.putExtra("fullimgkey", urlp);
                        startActivity(intent);
                    }
                    sheetDialog.dismiss();
                }
            });
            sheetView.findViewById(R.id.profile_img_bottomsheet_upload).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, 10);
                    sheetDialog.dismiss();
                }
            });
            sheetView.findViewById(R.id.profile_img_bottomsheet_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (urlp.equals("")) {
                        Toast.makeText(getApplicationContext(), "No Profile Image exist !", Toast.LENGTH_SHORT).show();
                    } else {
                        StorageReference storageRef = storage.getReference().child("profile_img").child(_phone);
                        storageRef.delete();
                        if(type.equals("admin")){
                            db.collection("admin").document(_phone).update("profile_img", "");
                        }else {
                            db.collection("users").document(_phone).update("profile_img", "");
                        }
                        Toast.makeText(getApplicationContext(), "Successfully Deleted\nRefresh the page", Toast.LENGTH_SHORT).show();
                    }
                    sheetDialog.dismiss();
                }
            });
            sheetDialog.setContentView(sheetView);
            sheetDialog.show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 10 && data.getData() != null) {
                Uri uri = data.getData();
                profile_img.setImageURI(uri);

                StorageReference storageRef = storage.getReference().child("profile_img").child(_phone);

                storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(getApplicationContext(), "Successfully uploaded !", Toast.LENGTH_SHORT).show();
                                if(type.equals("admin")){
                                    db.collection("admin").document(_phone).update("profile_img", uri.toString());
                                }else {
                                    db.collection("users").document(_phone).update("profile_img", uri.toString());
                                }
                            }
                        });
                    }
                });

            }
            if (requestCode == 11 && resultCode == RESULT_OK) {
                String extraReturn = data.getStringExtra("extra_return");
                name.setText(extraReturn);
            }
        } catch (Exception e) {

        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, home_page.class);
        startActivity(intent);
    }
}