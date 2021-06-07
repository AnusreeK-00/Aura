package com.recommend.project.aura;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class PopActivity extends Activity {

    Button musicBtn;
    Button movieBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        //get extra emotion
        final String ourEmo = getIntent().getStringExtra("emotion");
        movieBtn = findViewById(R.id.movieRecommend);
        musicBtn = findViewById(R.id.musicRecommend);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.7), (int)(height*.8));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        //click button
        movieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MovieRecommend.class);
                i.putExtra("emotion", ourEmo);
                startActivity(i);
            }
        });

        musicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MusicRecommend.class);
                i.putExtra("emotion", ourEmo);
                startActivity(i);
            }
        });
    }
}