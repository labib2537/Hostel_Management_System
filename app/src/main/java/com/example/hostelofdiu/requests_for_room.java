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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class requests_for_room extends AppCompatActivity implements View.OnClickListener {

    String phone;
    Button back_btn;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    requests_adapter adapter;
    ArrayList<myRoom_model> request_list;
    session ssn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_requests_for_room);
        getSupportActionBar().hide();

        ssn = new session(this);
        HashMap<String, String> userDetails = ssn.getuserdetailFromSession();
        phone = userDetails.get(session.KEY_PHONENUMBER);

        back_btn= findViewById(R.id.requests_page_back_bnt_id);
        recyclerView = findViewById(R.id.requests_page_recview_id);

        request_list = new ArrayList<>();
        adapter = new requests_adapter(request_list, this);
        GridLayoutManager gridLayoutManager = new
                GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
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
                            if(documentSnapshot.getString("application_condition").equals("requested")){
                                myRoom_model obj = documentSnapshot.toObject(myRoom_model.class);
                                obj.setId(documentSnapshot.getId());
                                request_list.add(obj);
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