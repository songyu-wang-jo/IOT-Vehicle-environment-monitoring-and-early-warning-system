package com.songyuwong.app.webPage;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.songyuwong.app.R;



public class MyWebChromeClient extends WebChromeClient {

    private AppCompatActivity activity;

    public MyWebChromeClient(AppCompatActivity myActivity) {
        this.activity = myActivity;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        TextView loading = activity.findViewById(R.id.loading);
        WebView webView = activity.findViewById(R.id.web_view);
        if(newProgress < 100){
            loading.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            String progress = "加载中\n"+newProgress + "%";
            loading.setText(progress);
        }else {
            loading.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
        }
    }
}
