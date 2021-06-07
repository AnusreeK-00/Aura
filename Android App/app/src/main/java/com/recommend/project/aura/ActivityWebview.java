package com.recommend.project.aura;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class ActivityWebview extends AppCompatActivity {
    WebView myWebView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_music_movie);
        String songName= getIntent().getStringExtra("song");
        String[] list= songName.split(" ");
        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String url="https://www.youtube.com/results?search_query=";
        int count=0;
        for(String word: list){
            url += word;
            if(count!=list.length-1){
                url+="+";
            }
            count++;
        }

        myWebView.loadUrl(url);

    }

}