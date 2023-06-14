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

import java.util.ArrayList;
import java.util.Date;

public class requests_adapter extends RecyclerView.Adapter<requests_adapter.viewholder> {
    ArrayList<myRoom_model> request_list;
    Context context;
    String time;
    int t;
    float time_1, time_2, duration;

    public requests_adapter(ArrayList<myRoom_model> request_list, Context context) {
        this.request_list = request_list;
        this.context = context;
    }

    @NonNull
    @Override
    public requests_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_request_card, parent, false);
        return new requests_adapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull requests_adapter.viewholder holder, @SuppressLint("RecyclerView") int position) {
        time_1 = Float.parseFloat(request_list.get(position).getApplication_time());
        time_2 = new Date().getTime();
        duration = (time_2 - time_1) / 1000;
        calculate_time(duration);

        holder.name_txtvw.setText(request_list.get(position).getUser_name());
        holder.time_txtvw.setText(time);
        holder.condition_txtvw.setText(request_list.get(position).getRequeste_for());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), admin_request_details.class);
                intent.putExtra("building_name_id_key", request_list.get(position).getBuilding_name());
                intent.putExtra("floor_position_id_key", request_list.get(position).getFloor_number());
                intent.putExtra("room_position_id_key", request_list.get(position).getRoom_number());
                intent.putExtra("occupation_date_id_key", request_list.get(position).getApplication_time());
                intent.putExtra("condition_id_key", request_list.get(position).getApplication_condition());
                intent.putExtra("request_id_key", request_list.get(position).getId());
                intent.putExtra("requested_for_id_key", request_list.get(position).getRequeste_for());
                intent.putExtra("user_phone_id_key", request_list.get(position).getUer_phone());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return request_list.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        ImageView profile_img;
        LinearLayout linearLayout;
        TextView name_txtvw, time_txtvw,condition_txtvw;

        public viewholder(@NonNull View itemview) {
            super(itemview);

            profile_img = itemView.findViewById(R.id.message_person_page_profile_img_id);
            linearLayout = itemView.findViewById(R.id.request_card_lout_id);
            name_txtvw = itemView.findViewById(R.id.message_person_page_profile_name_id);
            time_txtvw = itemView.findViewById(R.id.message_person_page_rofile_name_id);
            condition_txtvw = itemView.findViewById(R.id.message_erson_page_rofile_name_id);

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
