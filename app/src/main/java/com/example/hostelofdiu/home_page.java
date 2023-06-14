package com.example.hostelofdiu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class home_page extends AppCompatActivity implements View.OnClickListener {

    LinearLayout my_room_btn, message_btn, complain_box_btn, profile_layout,
            user_lout, admin_lout, history_lout, user_about_us;
    LinearLayout admin_room_requests, admin_users, admin_messages,
            admin_complainbox, admin_adbuilding, admin_about_us;
    DrawerLayout drawerLayout;
    ImageView profile_img;
    TextView profile_name, login_txt,request_for_badge_txt,message_badge_txt;
    Button menu;
    RecyclerView recyclerView;
    ArrayList<building_model> building_list;
    String phone, admin_phone, admin_img, name, type,new_requests_txt,new_messages_txt;
    building_adapter adapter;
    Intent intent;
    session ssn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LinearLayout.LayoutParams lout_empty, lout_not_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        int layout_width = LinearLayout.LayoutParams.MATCH_PARENT;
        int layout_height = LinearLayout.LayoutParams.WRAP_CONTENT;
        lout_empty = new LinearLayout.LayoutParams(0, 0);
        lout_not_empty = new LinearLayout.LayoutParams(layout_width, layout_height);

        ssn = new session(this);
        HashMap<String, String> userDetails = ssn.getuserdetailFromSession();
        phone = userDetails.get(session.KEY_PHONENUMBER);
        name = userDetails.get(session.KEY_NAME);
        type = userDetails.get(session.KEY_TYPE);

        drawerLayout = findViewById(R.id.nav_drawer_layout);
        menu = findViewById(R.id.menu_btn_id);
        recyclerView = findViewById(R.id.home_recview_id);

        user_lout = findViewById(R.id.user_lout);
        admin_lout = findViewById(R.id.admin_lout);
        //User
        history_lout = findViewById(R.id.myhistory_lout_btn);
        profile_img = findViewById(R.id.drawer_logedin_profile_image);
        profile_name = findViewById(R.id.drawer_logedin_name);
        my_room_btn = findViewById(R.id.myroom_lout_btn);
        message_btn = findViewById(R.id.message_lout_btn);
        complain_box_btn = findViewById(R.id.complain_lout_btn);
        login_txt = findViewById(R.id.login_text);
        profile_layout = findViewById(R.id.drawer_profile_lout);
        user_about_us = findViewById(R.id.drawer_abtus_lout);

        //Admin
        admin_adbuilding = findViewById(R.id.addbuilding_lout_btn);
        admin_room_requests = findViewById(R.id.admin_drawer_requests_lout);
        admin_users = findViewById(R.id.admin_drawer_users_lout);
        admin_messages = findViewById(R.id.admin_drawer_messages_lout);
        admin_complainbox = findViewById(R.id.admin_drawer_complainbox_lout);
        admin_about_us = findViewById(R.id.admin_drawer_abtus_lout);
        request_for_badge_txt = findViewById(R.id.request_for_room_badge_id);
        message_badge_txt = findViewById(R.id.admin_message_badge_id);

        building_list = new ArrayList<>();
        adapter = new building_adapter(building_list, this);
        GridLayoutManager gridLayoutManager = new
                GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        db.collection("buildings")
                .orderBy("building_name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        List<DocumentSnapshot> list = value.getDocuments();
                        building_list.clear();
                        for (DocumentSnapshot documentSnapshot : list) {
                            building_model obj = documentSnapshot.toObject(building_model.class);
                            obj.setBuilding_id(documentSnapshot.getId());
                            building_list.add(obj);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
        if (ssn.checkLogin()) {
            if (type.equals("admin")) {
                user_lout.setLayoutParams(lout_empty);
                admin_lout.setLayoutParams(lout_not_empty);
                profile_name.setLayoutParams(lout_not_empty);
                db.collection("admin")
                        .whereEqualTo("phone", phone)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value,
                                                @Nullable FirebaseFirestoreException error) {
                                for (QueryDocumentSnapshot document :  value) {
                                    admin_img = document.getString("profile_img");
                                    new_requests_txt=document.get("new_requests").toString();
                                    new_messages_txt=document.get("new_messages").toString();
                                    if (!admin_img.equals("")) {
                                        Picasso.get()
                                                .load(admin_img)
                                                .placeholder(R.drawable.ic_baseline_head)
                                                .into(profile_img);
                                    }
                                    admin_phone = document.getString("phone");
                                    profile_name.setText(name);
                                    if(!new_requests_txt.equals("0")){
                                        request_for_badge_txt.setVisibility(View.VISIBLE);
                                        request_for_badge_txt.setText(new_requests_txt);
                                    }else {
                                        request_for_badge_txt.setVisibility(View.GONE);
                                    }

                                    if(!new_messages_txt.equals("0")){
                                        message_badge_txt.setVisibility(View.VISIBLE);
                                        message_badge_txt.setText(new_messages_txt);
                                    }else {
                                        message_badge_txt.setVisibility(View.GONE);
                                    }
                                }
                            }
                        });
            } else {
                user_lout.setLayoutParams(lout_not_empty);
                admin_lout.setLayoutParams(lout_empty);
                profile_name.setLayoutParams(lout_not_empty);
                db.collection("users")
                        .whereEqualTo("phone", phone)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    String urlp = document.getString("profile_img");
                                    if (!urlp.equals("")) {
                                        Picasso.get()
                                                .load(urlp)
                                                .placeholder(R.drawable.ic_baseline_head)
                                                .into(profile_img);
                                    }
                                    profile_name.setText(document.getString("name"));
                                }
                            }
                        });
            }
            login_txt.setText("LogOut");
        } else {
            profile_name.setLayoutParams(lout_empty);
            user_lout.setLayoutParams(lout_not_empty);
            admin_lout.setLayoutParams(lout_empty);
        }

        menu.setOnClickListener(this);
        my_room_btn.setOnClickListener(this);
        history_lout.setOnClickListener(this);
        message_btn.setOnClickListener(this);
        complain_box_btn.setOnClickListener(this);
        login_txt.setOnClickListener(this);
        profile_layout.setOnClickListener(this);
        user_about_us.setOnClickListener(this);

        admin_room_requests.setOnClickListener(this);
        admin_users.setOnClickListener(this);
        admin_messages.setOnClickListener(this);
        admin_complainbox.setOnClickListener(this);
        admin_adbuilding.setOnClickListener(this);
        admin_about_us.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.menu_btn_id) {
            drawerLayout.openDrawer(GravityCompat.END);
        }
        else if (view.getId() == R.id.myroom_lout_btn) {
            drawerLayout.closeDrawer(GravityCompat.END);
            if (ssn.checkLogin()) {
                intent = new Intent(view.getContext(), my_room_page.class);
                view.getContext().startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "You are not Loged In yet", Toast.LENGTH_SHORT).show();
            }
        }
        else if (view.getId() == R.id.myhistory_lout_btn) {
            drawerLayout.closeDrawer(GravityCompat.END);
            if (ssn.checkLogin()) {
                intent = new Intent(view.getContext(), my_history_page.class);
                view.getContext().startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "You are not Loged In yet", Toast.LENGTH_SHORT).show();
            }
        }
        else if (view.getId() == R.id.message_lout_btn) {
            drawerLayout.closeDrawer(GravityCompat.END);
            if (ssn.checkLogin()) {
                db.collection("admin")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    intent = new Intent(view.getContext(), message_person_page.class);
                                    intent.putExtra("profile_id_key", documentSnapshot.getString("phone"));
                                    view.getContext().startActivity(intent);
                                }
                            }
                        });
            }
            else {
                Toast.makeText(getApplicationContext(), "You are not Loged In yet", Toast.LENGTH_SHORT).show();
            }
        }
        else if (view.getId() == R.id.complain_lout_btn) {
            drawerLayout.closeDrawer(GravityCompat.END);
            if (ssn.checkLogin()) {
                intent = new Intent(view.getContext(), complain_box_page.class);
                view.getContext().startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "You are not Loged In yet", Toast.LENGTH_SHORT).show();
            }
        }
        else if (view.getId() == R.id.login_text) {
            drawerLayout.closeDrawer(GravityCompat.END);
            if (ssn.checkLogin()) {
                profile_img.setImageResource(R.drawable.ic_baseline_head);
                profile_name.setLayoutParams(lout_empty);
                user_lout.setLayoutParams(lout_not_empty);
                admin_lout.setLayoutParams(lout_empty);
                login_txt.setText("LogIn");
                profile_name.setText("");
                ssn.logoutUserFromSession();
                drawerLayout.closeDrawer(GravityCompat.END);
                intent = new Intent(view.getContext(), login_page.class);
                view.getContext().startActivity(intent);
            }
        }
        else if (view.getId() == R.id.drawer_profile_lout) {
            drawerLayout.closeDrawer(GravityCompat.END);
            if (ssn.checkLogin()) {
                intent = new Intent(view.getContext(), profile_page.class);
                view.getContext().startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "You are not Loged In yet", Toast.LENGTH_SHORT).show();
            }
        }
        else if (view.getId() == R.id.addbuilding_lout_btn) {
            drawerLayout.closeDrawer(GravityCompat.END);
            if (ssn.checkLogin()) {
                intent = new Intent(view.getContext(), add_building.class);
                view.getContext().startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "You are not Loged In yet", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.admin_drawer_requests_lout) {
            drawerLayout.closeDrawer(GravityCompat.END);
            if (ssn.checkLogin()) {
                intent = new Intent(view.getContext(), requests_for_room.class);
                db.collection("admin").document(phone).update("new_requests",0);
                view.getContext().startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "You are not Loged In yet", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.admin_drawer_users_lout) {
            drawerLayout.closeDrawer(GravityCompat.END);
            if (ssn.checkLogin()) {
                intent = new Intent(view.getContext(), users_page.class);
                view.getContext().startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "You are not Loged In yet", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.admin_drawer_messages_lout) {
            drawerLayout.closeDrawer(GravityCompat.END);
            if (ssn.checkLogin()) {
                intent = new Intent(view.getContext(), message_list_page.class);
                db.collection("admin").document(phone).update("new_messages",0);
                view.getContext().startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "You are not Loged In yet", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.admin_drawer_complainbox_lout) {
            drawerLayout.closeDrawer(GravityCompat.END);
            if (ssn.checkLogin()) {
                intent = new Intent(view.getContext(), admin_complain_box.class);
                view.getContext().startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "You are not Loged In yet", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.drawer_abtus_lout) {
            drawerLayout.closeDrawer(GravityCompat.END);
            intent = new Intent(view.getContext(), user_about_us.class);
            view.getContext().startActivity(intent);

        } else if (view.getId() == R.id.admin_drawer_abtus_lout) {
            drawerLayout.closeDrawer(GravityCompat.END);
            intent = new Intent(view.getContext(), admin_about_us.class);
            view.getContext().startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}