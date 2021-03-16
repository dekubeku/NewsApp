package com.example.newsapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Article extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Intent intent = getIntent();

        byte[] byteArray;
        Bitmap bmp = null;

        //make image, also resize slightly bigger
        //check if intent for image is empty
        if(intent.getByteArrayExtra("image") != null)
        {
            byteArray = intent.getByteArrayExtra("image");
            bmp = BitmapFactory.decodeByteArray(byteArray,0, byteArray.length);
            bmp = (Bitmap.createScaledBitmap(bmp, 1200, 600, true));
        }


        //Title, author, date, content, url
        String[] article = intent.getStringArrayExtra("article");
        String longString = "";

        //make long content to look like a longer article
        //if content is null, don't write anything
        if(!article[3].equals("null"))
        {
            for(int j = 0; j < 10; j ++)
                longString = longString + "\n" + article[3];

        }
        else
        {
            longString = "";
        }

        TextView content = findViewById(R.id.contentFieldInArticle);
        content.setText(longString);

        //Populate the other article fields
        TextView title = findViewById(R.id.titleFieldInArticle);
        title.setText(article[0]);

        TextView author = findViewById(R.id.authorFieldInArticle);
        author.setText(article[1]);

        TextView date = findViewById(R.id.dateFieldInArticle);
        date.setText(article[2]);

        ImageView image = findViewById(R.id.imageFieldInArticle);
        image.setImageBitmap(bmp);

        TextView url = findViewById(R.id.urlField);
        url.setText("Read article on their website:\n" +  article[4]);

        url.setMovementMethod(LinkMovementMethod.getInstance());


    }
}