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

public class floors_adapter extends RecyclerView.Adapter<floors_adapter.viewholder> {
    int[] flr_arr;
    Context context;
    String id;

    public floors_adapter(int[] flr_arr, Context context, String id) {
        this.flr_arr = flr_arr;
        this.context = context;
        this.id = id;
    }

    @NonNull
    @Override
    public floors_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.floors_card, parent, false);
        return new floors_adapter.viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull floors_adapter.viewholder holder, @SuppressLint("RecyclerView") int position) {

        holder.floor_txtvw.setText("Floor : " + flr_arr[position]);
        holder.floor_lout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), rooms_page.class);
                intent.putExtra("building_id_key", id);
                intent.putExtra("floor_nong", flr_arr[position]+"");
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flr_arr.length;
    }

    class viewholder extends RecyclerView.ViewHolder {

        LinearLayout floor_lout;
        TextView floor_txtvw;

        public viewholder(@NonNull View itemview) {
            super(itemview);

            floor_lout = itemView.findViewById(R.id.card_id);
            floor_txtvw = itemView.findViewById(R.id.flr_txtvw_id);
        }
    }
}
