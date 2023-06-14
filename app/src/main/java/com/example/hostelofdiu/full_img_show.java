package com.example.hostelofdiu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

public class full_img_show extends AppCompatActivity {
    TouchImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_full_img_show);
        getSupportActionBar().hide();

        imageView=findViewById(R.id.flimgvw_id);
        String full_img_url=getIntent().getStringExtra("fullimgkey");
        Picasso.get()
                .load(full_img_url)
                .placeholder(R.drawable.ic_baseline_head)
                .into(imageView);
    }
}