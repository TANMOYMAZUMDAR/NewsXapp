package com.example.newsx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class NewsDetailsActivity extends AppCompatActivity {

    //UI
    private FrameLayout frameLayout;
    private ProgressBar progressBar;
    private WebView webView;

    private String url="";//will get from intent

    //actionBar
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        //init actionBar
        actionBar=getSupportActionBar();
        //back button in actionbar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        frameLayout=findViewById(R.id.frameLayout);
        progressBar=findViewById(R.id.progressBar);
        webView=findViewById(R.id.webView);

        //get url from intent
        url=getIntent().getStringExtra("url");

        webView.setWebViewClient(new HelpClient());
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                frameLayout.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);

                actionBar.setTitle("Loading");
                if(newProgress==100) {
                    //page is loading
                    frameLayout.setVisibility(View.GONE);
                    actionBar.setTitle(view.getTitle());
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.loadUrl(url);
        progressBar.setProgress(0);

    }

    private class HelpClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String mUrl) {
            view.loadUrl(mUrl);
            frameLayout.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

            Toast.makeText(NewsDetailsActivity.this,""+error.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode==KeyEvent.KEYCODE_BACK) && webView.canGoBack()){
            webView.goBack();
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go to previous activity by clicking back button in actionbar
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //get selected nes item it
        int id=item.getItemId();
        if(id==R.id.action_refresh)
        {
            webView.reload();
        }
        else if(id==R.id.action_copy){
              copyUrl();
        }
        else if(id==R.id.action_share){
              shareUrl();
        }
        return super.onOptionsItemSelected(item);
    }


    private void copyUrl(){
        String urlToCopy=webView.getUrl();

        ClipboardManager cb=(ClipboardManager)this.getSystemService(Context.CLIPBOARD_SERVICE);
        cb.setText(urlToCopy);

        Toast.makeText(this,"URL copied...",Toast.LENGTH_SHORT).show();
    }

    private void shareUrl(){
        String urlToShare=webView.getUrl();

        Intent shareIntent =new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,urlToShare);
        startActivity(Intent.createChooser(shareIntent,"Share Via"));

    }
}