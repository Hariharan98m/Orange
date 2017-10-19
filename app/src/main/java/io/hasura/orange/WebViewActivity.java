package io.hasura.orange;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewActivity extends BaseActivity {
    String book_name;
    boolean flag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        String url= getIntent().getStringExtra("profile");
        book_name= getIntent().getStringExtra("book_name");

        if(book_name!=null)
            setTitle(book_name);
        else
            setTitle(getIntent().getStringExtra("name"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebView webView= (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (flag) AD.show();
                else{
                    showProgressDialog(true);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(flag) {AD.dismiss();flag=false;}
                else
                    showProgressDialog(false);

            }
        });
        Log.i("URL",url);

        if(book_name!=null) {
            webView.loadUrl("https://www.amazon.in/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords="+book_name+" book");
        }else
            webView.loadUrl(url);
    }
}
