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

public class myRoom_adapter extends RecyclerView.Adapter<myRoom_adapter.viewholder> {

    ArrayList<myRoom_model> request_list;
    Context context;
    String from;

    public myRoom_adapter(ArrayList<myRoom_model> request_list, Context context,String from) {
        this.request_list = request_list;
        this.context = context;
        this.from=from;
    }

    @NonNull
    @Override
    public myRoom_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_room_request_card, parent, false);
        return new myRoom_adapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myRoom_adapter.viewholder holder, @SuppressLint("RecyclerView") int position) {
        String building_name, floor, room;
        building_name = request_list.get(position).getBuilding_name();
        floor = request_list.get(position).getFloor_number();
        room = request_list.get(position).getRoom_number();
        holder.details_txtvw.setText("[ Building : "+building_name+" ] [ Floor : "+floor+" ] [ Room : "+room+" ]");
        if(request_list.get(position).getApplication_condition().equals("requested")){
            holder.condition_txtvw.setBackgroundResource(R.drawable.custom_button_2_1);
        }else {
            holder.condition_txtvw.setBackgroundResource(R.drawable.custom_button_2_2);
        }
        holder.condition_txtvw.setText(request_list.get(position).getApplication_condition());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), myRoom_details.class);
                intent.putExtra("building_name_id_key", building_name);
                intent.putExtra("floor_position_id_key", floor);
                intent.putExtra("room_position_id_key", request_list.get(position).getRoom_number());
                intent.putExtra("occupation_date_id_key", request_list.get(position).getOccupation_date());
                intent.putExtra("leaving_date_id_key", request_list.get(position).getLeaving_date());
                intent.putExtra("condition_id_key", request_list.get(position).getApplication_condition());
                intent.putExtra("request_id_key", request_list.get(position).getId());
                intent.putExtra("from_id_key", from);
                intent.putExtra("requested_for_id_key", request_list.get(position).getRequeste_for());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return request_list.size();
    }

    class viewholder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView details_txtvw, condition_txtvw;

        public viewholder(@NonNull View itemview) {
            super(itemview);

            linearLayout = itemView.findViewById(R.id.request_card_lout_id);
            details_txtvw = itemView.findViewById(R.id.request_card_details_txtw_id);
            condition_txtvw = itemView.findViewById(R.id.request_card_condition_txtw_id);

        }

    }
}
