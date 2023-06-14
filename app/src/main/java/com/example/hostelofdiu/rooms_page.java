package com.example.hostelofdiu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class rooms_page extends AppCompatActivity implements View.OnClickListener {

    TextView building_name,total_floors;
    Button back_btn;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    rooms_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_rooms_page);
        getSupportActionBar().hide();


        String id = getIntent().getStringExtra("building_id_key");
        String floor_position=getIntent().getStringExtra("floor_nong");

        building_name=findViewById(R.id.rooms_page_name_id);
        total_floors=findViewById(R.id.rooms_page_floors_id);
        back_btn=findViewById(R.id.room_page_back_btn_id);
        recyclerView=findViewById(R.id.rooms_page_recvw_id);

        db.collection("buildings").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String name,rms_in_flr;
                    name=documentSnapshot.getString("building_name");
                    rms_in_flr=documentSnapshot.getString("total_room_inflor");
                    building_name.setText("Building : "+name);
                    total_floors.setText("Floor : "+floor_position);
                    int rms_flr=Integer.parseInt(rms_in_flr);
                    int[] room_arr=new int[rms_flr];
                    for(int i=0;i<rms_flr;i++){
                        room_arr[i]=i+1;
                    }
                    adapter = new rooms_adapter(room_arr, getApplicationContext(),id,floor_position,name);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 4, GridLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(adapter);
                }
            }
        });


        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}