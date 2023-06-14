package com.example.hostelofdiu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class admin_complain_box extends AppCompatActivity implements View.OnClickListener {

    Button back_btn;
    RecyclerView recyclerView;
    ArrayList<complain_box_model> complain_list;
    complain_box_adapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_admin_complain_box);
        getSupportActionBar().hide();


        back_btn= findViewById(R.id.admin_complainbox_page_back_bnt_id);
        recyclerView= findViewById(R.id.admin_complainbox_page_recvw_id);

        complain_list = new ArrayList<>();
        adapter = new complain_box_adapter(complain_list, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        db.collection("complain_box")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list) {
                            complain_box_model obj = documentSnapshot.toObject(complain_box_model.class);
                            complain_list.add(obj);
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