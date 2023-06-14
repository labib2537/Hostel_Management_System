package com.example.hostelofdiu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class messages_person_adapter extends RecyclerView.Adapter {

    float time_1, time_2, duration;
    String time, phone,type;
    int t;
    final int ITEM_SENT = 1, ITEM_RECEIVE = 2;
    Context context;
    ArrayList<message_person_model> massages;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference addedDocRef;
    session ssn;

    public messages_person_adapter(Context context, ArrayList<message_person_model> massages) {
        this.context = context;
        this.massages = massages;

        ssn = new session(context.getApplicationContext());
        HashMap<String, String> userDetails = ssn.getuserdetailFromSession();
        phone = userDetails.get(session.KEY_PHONENUMBER);
        type = userDetails.get(session.KEY_TYPE);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_massage_1, parent, false);
            return new sentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_massage_2, parent, false);
            return new receiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (massages.get(position).getPhone().equals(phone)) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        time_1 = Float.parseFloat(massages.get(position).getTime());
        time_2 = new Date().getTime();
        duration = (time_2 - time_1) / 1000;
        calculate_time(duration);
        if (holder.getClass() == sentViewHolder.class) {
            sentViewHolder viewHolder = (sentViewHolder) holder;
            viewHolder.sent_txt.setText(massages.get(position).getMassage());
            viewHolder.sent_time.setText(time);
        } else {
            receiverViewHolder viewHolder = (receiverViewHolder) holder;
            if (type.equals("admin")) {
                addedDocRef = db.collection("users").document(massages.get(position).getPhone());
            } else {
                addedDocRef = db.collection("admin").document(massages.get(position).getPhone());
            }
            addedDocRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                if (!documentSnapshot.getString("profile_img").isEmpty()) {
                                    Picasso.get()
                                            .load(documentSnapshot.getString("profile_img"))
                                            .placeholder(R.drawable.ic_baseline_head)
                                            .into(viewHolder.profile_img);
                                }
                                viewHolder.profile_name.setText(documentSnapshot.getString("name"));
                            }
                        }
                    });
            viewHolder.receive_txt.setText(massages.get(position).getMassage());
            viewHolder.receive_time.setText(time);
        }
    }

    @Override
    public int getItemCount() {
        return massages.size();
    }


    public class sentViewHolder extends RecyclerView.ViewHolder {
        TextView sent_txt, sent_time;

        public sentViewHolder(@NonNull View itemView) {
            super(itemView);
            sent_txt = itemView.findViewById(R.id.masssage_1_massage_textvw_id);
            sent_time = itemView.findViewById(R.id.massage_1_time_textvw_id);
        }
    }

    public class receiverViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_img;
        TextView profile_name, receive_txt, receive_time;

        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_img = itemView.findViewById(R.id.massage_user_img_id);
            profile_name = itemView.findViewById(R.id.massage_user_name_id);
            receive_txt = itemView.findViewById(R.id.massage_2_massage_textvw_id);
            receive_time = itemView.findViewById(R.id.massage_2_time_textvw_id);
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
