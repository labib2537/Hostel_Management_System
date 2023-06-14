package com.example.hostelofdiu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class profile_view extends AppCompatActivity implements View.OnClickListener {

    Button back_btn;
    ImageView profile_img;
    TextView profile_name_txtvw, phone_txtvw;
    RecyclerView recyclerView;
    String phone;
    ArrayList<myRoom_model> history_list;
    profile_history_adapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_profile_view);
        getSupportActionBar().hide();

        phone = getIntent().getStringExtra("user_phone_id_key");

        back_btn = findViewById(R.id.profile_view_back_btn_id);
        profile_img = findViewById(R.id.profile_view_profile_image_id);
        profile_name_txtvw = findViewById(R.id.profile_view_name_txtvw);
        phone_txtvw = findViewById(R.id.profile_view_phone_txtvw);
        db.collection("users").document(phone)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            if (!documentSnapshot.getString("profile_img").equals("")) {
                                Picasso.get()
                                        .load(documentSnapshot.getString("profile_img"))
                                        .placeholder(R.drawable.ic_baseline_head)
                                        .into(profile_img);
                            }
                            profile_name_txtvw.setText(documentSnapshot.getString("name"));
                            phone_txtvw.setText("Phone : "+phone);
                        }
                    }
                });

        recyclerView = findViewById(R.id.profile_view_recvw_id);
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
                            if(documentSnapshot.getString("uer_phone").equals(phone)){
                                myRoom_model obj = documentSnapshot.toObject(myRoom_model.class);
                                history_list.add(obj);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}