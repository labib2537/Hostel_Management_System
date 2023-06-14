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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class building_adapter extends RecyclerView.Adapter<building_adapter.viewholder> {

    ArrayList<building_model> building_list;
    Context context;

    public building_adapter(ArrayList<building_model> building_list, Context context) {
        this.building_list = building_list;
        this.context = context;
    }

    @NonNull
    @Override
    public building_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull building_adapter.viewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.building_name_txtvw.setText("Building : "+building_list.get(position).getBuilding_name());
        holder.floor_txtvw.setText("Floors : "+building_list.get(position).getTotal_flor());
        holder.avilable_rms.setText("Total Room : "+building_list.get(position).available_rooms);
        holder.building_for.setText(building_list.get(position).getBuilding_for());
        Picasso.get().load(building_list.get(position).getFirst_image()).placeholder(R.drawable.hostel_building_img)
                .into(holder.building_img);
        holder.lout_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), building_details.class);
                intent.putExtra("building_id_key", building_list.get(position).getBuilding_id());
                intent.putExtra("building_name_key", building_list.get(position).getBuilding_name());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return building_list.size();
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView building_name_txtvw, floor_txtvw,avilable_rms,building_for;
        LinearLayout lout_id;
        AppCompatImageView building_img;

        public viewholder(@NonNull View itemview) {
            super(itemview);
            building_name_txtvw = itemView.findViewById(R.id.building_name_txtvw_id);
            floor_txtvw = itemView.findViewById(R.id.floors_txtvw_id);
            lout_id = itemView.findViewById(R.id.card_id);
            avilable_rms= itemView.findViewById(R.id.availabe_txtvw_id);
            building_for= itemView.findViewById(R.id.building_for_txtw_id);
            building_img= itemView.findViewById(R.id.building_first_img_id);
        }
    }
}
