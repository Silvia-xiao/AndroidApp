package com.example.androidass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

public class PostToFbActivity extends AppCompatActivity {
    TextView contentTV;
    Button postBT;
    String movieName;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_to_fb);
        contentTV = findViewById(R.id.fb_content_tv);
        //postBT = findViewById(R.id.fb_post_bt);
        Intent intent = getIntent();
        movieName = intent.getStringExtra("movieName");
        contentTV.setText("Share with your friends! Let's watch " + movieName + " together!");
        initFacebook();
    }

    private void initFacebook() {
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //facebook call back
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void shareToFacebook(View view) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://www.imdb.com/"))
                    .build();
            shareDialog.show(linkContent);
        }
    }

}
