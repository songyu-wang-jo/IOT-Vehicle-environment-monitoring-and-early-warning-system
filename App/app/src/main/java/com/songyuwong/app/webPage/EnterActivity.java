package com.songyuwong.app.webPage;

import android.content.Intent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.songyuwong.app.R;

public class EnterActivity extends AppCompatActivity {

    private WebView view;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String address = intent.getStringExtra("address");
        url = "http://".concat(address);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        view = findViewById(R.id.web_view);
        WebSettings settings = view.getSettings();
        settings.setJavaScriptEnabled(true);
        view.setWebViewClient(new MyWebViewClient(this));
        view.setWebChromeClient(new MyWebChromeClient(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(EnterActivity.this, url, Toast.LENGTH_SHORT);
        view.loadUrl(url);
    }
}