package com.example.hostelofdiu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class message_person_page extends AppCompatActivity implements View.OnClickListener {

    Button back_btn;
    ImageView profile_img;
    TextView profile_name;
    RecyclerView recyclerView;
    TextInputEditText massage_txt;
    ImageButton massage_sent_btn;
    String user_phone, phone, type, massage_str, sender_room, receiver_room;
    messages_person_adapter adapter;
    ArrayList<message_person_model> massages;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference addedDocRef;
    session ssn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_message_page);
        getSupportActionBar().hide();

        ssn = new session(this);
        HashMap<String, String> userDetails = ssn.getuserdetailFromSession();
        type = userDetails.get(session.KEY_TYPE);
        user_phone = userDetails.get(session.KEY_PHONENUMBER);
        phone = getIntent().getStringExtra("profile_id_key");

        if (type.equals("admin")) {
            addedDocRef = db.collection("admin").document(user_phone);
        } else {
            addedDocRef = db.collection("users").document(user_phone);
        }

        back_btn = findViewById(R.id.message_person_page_back_btn_id);
        profile_img = findViewById(R.id.message_person_page_profile_img_id);
        profile_name = findViewById(R.id.message_person_page_profile_name_id);
        recyclerView = findViewById(R.id.message_person_page_recvw_id);
        massage_txt = findViewById(R.id.message_person_page_message_edttxt_id);
        massage_sent_btn = findViewById(R.id.message_person_page_sent_btn_id);

        addedDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (!documentSnapshot.getString("profile_img").isEmpty()) {
                        Picasso.get()
                                .load(documentSnapshot.getString("profile_img"))
                                .placeholder(R.drawable.ic_baseline_head)
                                .into(profile_img);
                    }
                    profile_name.setText(documentSnapshot.getString("name"));
                }
            }
        });

        sender_room = user_phone + phone;
        receiver_room = phone + user_phone;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        massages = new ArrayList<>();
        adapter = new messages_person_adapter(this, massages);
        recyclerView.setHasFixedSize(true);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("massages")
                .document(sender_room)
                .collection("chats")
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<DocumentSnapshot> list = value.getDocuments();
                        massages.clear();
                        for (DocumentSnapshot documentSnapshot : list) {
                            message_person_model obj = documentSnapshot.toObject(message_person_model.class);
                            obj.setMassage_id(documentSnapshot.getId());
                            massages.add(obj);
                        }
                        recyclerView.setAdapter(adapter);
                    }
                });


        back_btn.setOnClickListener(this);
        massage_sent_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.message_person_page_back_btn_id) {
            finish();
        } else if (view.getId() == R.id.message_person_page_sent_btn_id) {
            massage_str = massage_txt.getText().toString().trim();
            if (massage_str.equals("")) {
                Toast.makeText(getApplicationContext(), "Write something !", Toast.LENGTH_LONG).show();
            } else {
                Date date = new Date();
                message_person_model massage = new message_person_model(date.getTime() + "", user_phone, massage_str);
                db.collection("massages")
                        .document(sender_room)
                        .collection("chats")
                        .add(massage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                db.collection("massages")
                                        .document(receiver_room)
                                        .collection("chats")
                                        .add(massage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                if (type.equals("admin")) {
                                                    message_person_model last_massage = new message_person_model(date.getTime() + "", phone, massage_str);
                                                    db.collection("last_massage").document(phone).set(last_massage);
                                                } else {
                                                    message_person_model last_massage = new message_person_model(date.getTime() + "",user_phone, massage_str);
                                                    db.collection("last_massage").document(user_phone).set(last_massage);
                                                    db.collection("admin").document(phone).update("new_messages", FieldValue.increment(1));
                                                }
                                            }
                                        });
                            }
                        });
                massage_txt.setText("");
            }
        }
    }
}