package com.example.hostelofdiu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class profile_history_adapter extends RecyclerView.Adapter<profile_history_adapter.viewholder> {

    ArrayList<myRoom_model> history_list;
    Context context;
    float time_1, time_2, duration;
    String time;
    int t;

    public profile_history_adapter(ArrayList<myRoom_model> history_list, Context context) {
        this.history_list= history_list;
        this.context = context;
    }

    @NonNull
    @Override
    public profile_history_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_history_card, parent, false);
        return new profile_history_adapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull profile_history_adapter.viewholder holder, @SuppressLint("RecyclerView") int position) {

        holder.building_name.setText("Building Name : "+history_list.get(position).getBuilding_name());
        holder.floor_number.setText("Floor Number : "+history_list.get(position).getFloor_number());
        holder.room_number.setText("Room Number : "+history_list.get(position).getRoom_number());
        if(!history_list.get(position).getOccupation_date().equals("")){
            holder.ocupation_date.setText("Date of Occupation : "+history_list.get(position).getOccupation_date());
        }
        if(!history_list.get(position).getLeaving_date().equals("")){
            holder.leaving_date.setText("Date of Leave : "+history_list.get(position).getLeaving_date());
        }

        time_1 = Float.parseFloat(history_list.get(position).getApplication_time());
        time_2 = new Date().getTime();
        duration = (time_2 - time_1) / 1000;
        calculate_time(duration);
        holder.time.setText(time);
        holder.condition.setText(history_list.get(position).getApplication_condition());

    }

    @Override
    public int getItemCount() {
        return history_list.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        TextView building_name,floor_number,room_number,ocupation_date,leaving_date,time,condition;

        public viewholder(@NonNull View itemview) {
            super(itemview);

            building_name= itemView.findViewById(R.id.history_card_building_name_txtvw_id);
            floor_number= itemView.findViewById(R.id.history_card_floor_number_txtvw_id);
            room_number= itemView.findViewById(R.id.history_card_room_number_txtvw_id);
            ocupation_date= itemView.findViewById(R.id.history_card_ocdate_txtvw_id);
            leaving_date= itemView.findViewById(R.id.history_card_levdate_txtvw_id);
            time= itemView.findViewById(R.id.history_card__time_textvw_id);
            condition= itemView.findViewById(R.id.history_card_condition_txtvw_id);

        }

    }
    //Time calculation function
    void calculate_time(float duration) {
        if (duration > 59 && duration < 3600) {
            duration = duration / 60;
            t = Math.round(duration);
            time = t + " min ago";
        } else if (duration > 3600 && duration < 86400) {
            duration = duration / 60 / 60;
            t = Math.round(duration);
            time = t + " hr ago";
        } else if (duration > 86400 && duration < 604800) {
            duration = duration / 60 / 60 / 24;
            t = Math.round(duration);
            time = t + " day ago";
        } else if (duration > 604800 && duration < 2592000) {
            duration = duration / 60 / 60 / 24 / 7;
            t = Math.round(duration);
            time = t + " week ago";
        } else if (duration > 2592000 && duration < 31536000) {
            duration = duration / 60 / 60 / 24 / 30;
            t = Math.round(duration);
            time = t + " month ago";
        } else if (duration > 31536000) {
            duration = duration / 60 / 60 / 24 / 365;
            t = Math.round(duration);
            time = t + " yr ago";
        } else {
            time = "1 min ago";
        }
    }
}
