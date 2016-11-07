package com.chaney.richtextlib;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.chaney.richtextview.RichTextView;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    RichTextView richTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        richTextView = (RichTextView) findViewById(R.id.richtext_view);


        findViewById(R.id.btn_select_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSelectImg();
            }
        });
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = richTextView.getTemplateRichText();
                PreViewActivity.startAc(MainActivity.this,content,richTextView.getUrls());
            }
        });
        findViewById(R.id.btn_html).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = richTextView.getHtmlRichText();
                Toast.makeText(MainActivity.this,content,Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void toSelectImg() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra("return-data",true);
        startActivityForResult(intent, 0);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            try {
                ContentResolver resolver = getContentResolver();
                // 获得图片的uri
                Uri originalUri = data.getData();

                Bitmap originalBitmap = BitmapFactory.decodeStream(resolver.openInputStream(originalUri));

                richTextView.addImageViewByLastFocusIndex(originalBitmap,originalUri.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }





}
