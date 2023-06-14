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

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class users_adapter extends RecyclerView.Adapter<users_adapter.viewholder>  {

    ArrayList<users_model> users_list;
    Context context;

    public users_adapter(ArrayList<users_model> users_list, Context context) {
        this.users_list = users_list;
        this.context = context;
    }

    @NonNull
    @Override
    public users_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_card, parent, false);
        return new users_adapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull users_adapter.viewholder holder, @SuppressLint("RecyclerView") int position) {

        if(!users_list.get(position).getProfile_img().equals("")){
            Picasso.get()
                    .load(users_list.get(position).getProfile_img())
                    .placeholder(R.drawable.ic_baseline_head)
                    .into(holder.profile_img);
        }
        holder.name_txtvw.setText(users_list.get(position).getName());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), profile_view.class);
                intent.putExtra("user_phone_id_key", users_list.get(position).getPhone());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users_list.size();
    }


    class viewholder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        ImageView profile_img;
        TextView name_txtvw;

        public viewholder(@NonNull View itemview) {
            super(itemview);

            profile_img = itemView.findViewById(R.id.users_profile_img_id);
            linearLayout = itemView.findViewById(R.id.users_card_lout_id);
            name_txtvw = itemView.findViewById(R.id.users_profile_name_id);
        }
    }
}
