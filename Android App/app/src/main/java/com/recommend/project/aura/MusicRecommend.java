package com.recommend.project.aura;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MusicRecommend extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView textView;
    String music[]={
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend);
        textView=findViewById(R.id.title);
        recyclerView=findViewById(R.id.recyclerView);
        textView.setText("Recommended Songs are: ");

        Adapter adapter=new Adapter(this,music);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final String ourEmo = getIntent().getStringExtra("emotion");
        connectServer(ourEmo);
    }

    void connectServer(String emotion){

//        https://www.myawesomesite.com/turtles/types?type=1&sort=relevance#section-name
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("bcec3655f047.ngrok.io")
                .appendQueryParameter("emotion", emotion);
        String myUrl = builder.build().toString();

//        TextView responseText = findViewById(R.id.responseText);
//        responseText.setText("waiting for server..");
        Toast.makeText(getApplicationContext(), "Waiting for server...", Toast.LENGTH_SHORT).show();
        postRequest(myUrl);
    }

    void postRequest(String postUrl) {

        OkHttpClient client = new OkHttpClient();

        try {
            Request request = new Request.Builder()
                    .url(postUrl)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                String res = response.body().string();
                                displaySongs(res);
//                                Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

        } catch (Exception e) {

        }
    }

    private void displaySongs(String res) {

        String str[] = res.split("\\^");
        List<String> al = new ArrayList<String>();
        al = Arrays.asList(str);
        int i = 0;
        for(String s: al){
            music[i] = s;
            i++;
        }

        Adapter adapter=new Adapter(this,music);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

}