package com.example.hostelofdiu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class rooms_adapter extends RecyclerView.Adapter<rooms_adapter.viewholder> {
    int[] rooms_arr;
    Context context;
    session ssn;
    String id, floor_position, building_name, type;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public rooms_adapter(int[] rooms_arr, Context context, String id, String floor_position, String building_name) {
        this.rooms_arr = rooms_arr;
        this.context = context;
        this.id = id;
        this.floor_position = floor_position;
        this.building_name = building_name;

        ssn = new session(context.getApplicationContext());
        HashMap<String, String> userDetails = ssn.getuserdetailFromSession();
        type = userDetails.get(session.KEY_TYPE);

    }

    @NonNull
    @Override
    public rooms_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rooms_card, parent, false);
        return new rooms_adapter.viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull rooms_adapter.viewholder holder, @SuppressLint("RecyclerView") int position) {
        String room_p = rooms_arr[position] + "";
        db.collection("occupied_rooms")
                .whereEqualTo("building_name", building_name)
                .whereEqualTo("floor_number", floor_position)
                .whereEqualTo("room_number", room_p)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            holder.rooms_lout.setBackgroundResource(R.drawable.custom_button_2);
                        } else {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                if (documentSnapshot.getString("application_condition").equals("requested")) {
                                    holder.rooms_lout.setBackgroundResource(R.drawable.custom_button_2_1);
                                }
                                if (documentSnapshot.getString("application_condition").equals("occupied")) {
                                    holder.rooms_lout.setBackgroundResource(R.drawable.custom_button_2_2);
                                }
                            }
                        }
                    }
                });

        holder.rooms_txtvw.setText("Room : " + room_p);
        holder.rooms_lout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ssn.checkLogin()) {
                    Toast.makeText(view.getContext(), "You are not Loged in yet !", Toast.LENGTH_SHORT).show();
                } else {
                    db.collection("occupied_rooms")
                            .whereEqualTo("building_name", building_name)
                            .whereEqualTo("floor_number", floor_position)
                            .whereEqualTo("room_number", rooms_arr[position] + "")
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (queryDocumentSnapshots.isEmpty()) {
                                        if (type.equals("admin")) {
                                            Toast.makeText(view.getContext(), "This Room is Empty", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Intent intent = new Intent(view.getContext(), request_details.class);
                                            intent.putExtra("building_id_key", id);
                                            intent.putExtra("floor_position_id_key", floor_position);
                                            intent.putExtra("room_position_id_key", rooms_arr[position] + "");
                                            intent.putExtra("building_name_id_key", building_name);
                                            view.getContext().startActivity(intent);
                                        }
                                    } else if (!queryDocumentSnapshots.isEmpty()) {
                                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            String condition = documentSnapshot.getString("application_condition");
                                            if (condition.equals("requested")) {
                                                if (type.equals("admin")) {
                                                    Intent intent = new Intent(view.getContext(), profile_view.class);
                                                    intent.putExtra("user_phone_id_key", documentSnapshot.getString("uer_phone"));
                                                    view.getContext().startActivity(intent);
                                                } else {
                                                    Toast.makeText(view.getContext(), "Someone is Requested for this Room !", Toast.LENGTH_SHORT).show();
                                                }
                                            } else if (documentSnapshot.getString("application_condition").equals("occupied")) {
                                                if (type.equals("admin")) {
                                                    Intent intent = new Intent(view.getContext(), profile_view.class);
                                                    intent.putExtra("user_phone_id_key", documentSnapshot.getString("uer_phone"));
                                                    view.getContext().startActivity(intent);
                                                } else {
                                                    Toast.makeText(view.getContext(), "This Room is Occupied !", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(view.getContext(), "This Room is Empty", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return rooms_arr.length;
    }

    class viewholder extends RecyclerView.ViewHolder {

        LinearLayout rooms_lout;
        TextView rooms_txtvw;

        public viewholder(@NonNull View itemview) {
            super(itemview);

            rooms_lout = itemView.findViewById(R.id.card_id);
            rooms_txtvw = itemView.findViewById(R.id.room_txtvw_id);
        }
    }
}
