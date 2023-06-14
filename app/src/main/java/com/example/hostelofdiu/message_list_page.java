package com.example.hostelofdiu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class message_list_page extends AppCompatActivity implements View.OnClickListener {


    Button back_btn;
    RecyclerView recyclerView;
    String phone;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    message_list_page_adapter adapter;
    ArrayList<message_person_model> message_list;
    session ssn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_message_list_page);
        getSupportActionBar().hide();

        ssn = new session(this);
        HashMap<String, String> userDetails = ssn.getuserdetailFromSession();
        phone = userDetails.get(session.KEY_PHONENUMBER);

        back_btn = findViewById(R.id.messages_page_back_bnt_id);
        recyclerView = findViewById(R.id.messages_page_recview_id);

        message_list = new ArrayList<>();
        adapter = new message_list_page_adapter(message_list, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        db.collection("last_massage")
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<DocumentSnapshot> list = value.getDocuments();
                        message_list.clear();
                        for (DocumentSnapshot documentSnapshot : list) {
                            message_person_model obj = documentSnapshot.toObject(message_person_model.class);
                            message_list.add(obj);
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