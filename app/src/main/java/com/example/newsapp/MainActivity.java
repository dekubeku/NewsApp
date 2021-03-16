package com.example.newsapp;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Add listener to search field
        final EditText search = findViewById(R.id.search);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //When input is done ..
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //Display "Loading"-text
                    Toast.makeText(getApplicationContext(), "Loading articles from " + search.getText(),
                            Toast.LENGTH_SHORT).show();

                    //Switch to article activity
                    searchListSwitch(search.getText().toString());

                    return true;
                }
                return false;
            }
        });
    }


    private void searchListSwitch(String source)
    {
        Intent intent = new Intent(this, SearchList.class);
        intent.putExtra("source", source);
        startActivity(intent);
    }


}