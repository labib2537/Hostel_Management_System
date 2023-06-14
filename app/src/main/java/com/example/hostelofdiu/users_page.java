package com.example.hostelofdiu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class users_page extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    Button back_btn;
    EditText editText;
    RecyclerView recyclerView;
    String phone;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    users_adapter adapter;
    ArrayList<users_model> request_list;
    GridLayoutManager gridLayoutManager;
    session ssn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_users_page);
        getSupportActionBar().hide();

        ssn = new session(this);
        HashMap<String, String> userDetails = ssn.getuserdetailFromSession();
        phone = userDetails.get(session.KEY_PHONENUMBER);

        back_btn = findViewById(R.id.users_page_back_bnt_id);
        recyclerView = findViewById(R.id.users_page_recview_id);
        editText = findViewById(R.id.users_page_edttxt_id);

        request_list = new ArrayList<>();
        adapter = new users_adapter(request_list, this);
        gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        db.collection("users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list) {
                            users_model obj = documentSnapshot.toObject(users_model.class);
                            request_list.add(obj);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        back_btn.setOnClickListener(this);
        editText.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        finish();
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        recyclerView.setLayoutManager(gridLayoutManager);
        if (editable.toString().isEmpty()) {

            db.collection("users")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            request_list.clear();
                            for (DocumentSnapshot documentSnapshot : list) {
                                users_model obj = documentSnapshot.toObject(users_model.class);
                                request_list.add(obj);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });

        } else {
            db.collection("users")
                    .orderBy("name_lower")
                    .startAt(editable.toString().toLowerCase())
                    .endAt(editable.toString().toLowerCase() + "\uf8ff")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            request_list.clear();
                            for (DocumentSnapshot documentSnapshot : list) {
                                users_model obj = documentSnapshot.toObject(users_model.class);
                                request_list.add(obj);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });

        }
    }
}