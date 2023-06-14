package com.example.hostelofdiu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class message_list_page_adapter extends RecyclerView.Adapter<message_list_page_adapter.viewholder> {

    ArrayList<message_person_model> messages_list;
    Context context;
    float time_1, time_2, duration;
    String time;
    int t;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public message_list_page_adapter(ArrayList<message_person_model> messages_list, Context context) {
        this.messages_list = messages_list;
        this.context = context;
    }

    @NonNull
    @Override
    public message_list_page_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.massages, parent, false);
        return new message_list_page_adapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull message_list_page_adapter.viewholder holder, @SuppressLint("RecyclerView") int position) {


        db.collection("users")
                .document(messages_list.get(position).getPhone())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            if(!documentSnapshot.getString("profile_img").equals("")){
                                Picasso.get()
                                        .load(documentSnapshot.getString("profile_img"))
                                        .placeholder(R.drawable.ic_baseline_head)
                                        .into(holder.profile_img);
                            }
                            holder.name_txtvw.setText(documentSnapshot.getString("name"));
                        }
                    }
                });

        holder.message_txtvw.setText(messages_list.get(position).getMassage());
        time_1 = Float.parseFloat(messages_list.get(position).getTime());
        time_2 = new Date().getTime();
        duration = (time_2 - time_1) / 1000;
        calculate_time(duration);
        holder.time_txtvw.setText(time);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), message_person_page.class);
                intent.putExtra("profile_id_key", messages_list.get(position).getPhone());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return messages_list.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        ImageView profile_img;
        TextView name_txtvw,message_txtvw,time_txtvw;

        public viewholder(@NonNull View itemview) {
            super(itemview);

            profile_img = itemView.findViewById(R.id.massage_user_img_id);
            linearLayout = itemView.findViewById(R.id.massage_user_layout_id);
            name_txtvw = itemView.findViewById(R.id.massage_user_name_id);
            message_txtvw = itemView.findViewById(R.id.last_massage_text_id);
            time_txtvw = itemView.findViewById(R.id.last_massage_time_text_id);
        }
    }

    //Time calculation function
    void calculate_time(float duration) {
        if (duration > 59 && duration < 3600) {
            duration = duration / 60;
            t = Math.round(duration);
            time = t + " min";
        } else if (duration > 3600 && duration < 86400) {
            duration = duration / 60 / 60;
            t = Math.round(duration);
            time = t + " hr";
        } else if (duration > 86400 && duration < 604800) {
            duration = duration / 60 / 60 / 24;
            t = Math.round(duration);
            time = t + " day";
        } else if (duration > 604800 && duration < 2592000) {
            duration = duration / 60 / 60 / 24 / 7;
            t = Math.round(duration);
            time = t + " week";
        } else if (duration > 2592000 && duration < 31536000) {
            duration = duration / 60 / 60 / 24 / 30;
            t = Math.round(duration);
            time = t + " month";
        } else if (duration > 31536000) {
            duration = duration / 60 / 60 / 24 / 365;
            t = Math.round(duration);
            time = t + " yr";
        } else {
            time = "1 min";
        }
    }

}
