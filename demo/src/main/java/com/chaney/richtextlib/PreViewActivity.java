package com.chaney.richtextlib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chaney.richtextview.RichTextView;

import java.util.ArrayList;

public class PreViewActivity extends AppCompatActivity  {


    String content;

    ArrayList<String> urls;

    RichTextView richTextView;

    int winth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_view);
        richTextView = (RichTextView) findViewById(R.id.richtext_view);
        winth = getWindowManager().getDefaultDisplay().getWidth();
        getIntents();
        initRichText();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initRichText() {
        richTextView.setImageLoader(new RichTextView.ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Glide.with(PreViewActivity.this).load(url).into(imageView);
//                Picasso.with(PreViewActivity.this).load(url).into(imageView);
            }
        });

//        int size = urls.size();
//        urls.clear();
//        for(int index =0 ;index<size;index++){
//            urls.add("http://photo.enterdesk.com/2010-10-24/enterdesk.com-3B11711A460036C51C19F87E7064FE9D.jpg");
//        }
        richTextView.loadingRichText(content,urls);
    }


    private void getIntents() {
        Intent intent = getIntent();
        content = intent.getStringExtra("content");
        urls = intent.getStringArrayListExtra("urls");
    }


    public static void startAc(Context context,String content,ArrayList<String> urls){
        Intent intent = new Intent(context,PreViewActivity.class);
        intent.putExtra("content",content);
        intent.putExtra("urls",urls);
        context.startActivity(intent);
    }
}
