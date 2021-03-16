package com.example.newsapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.util.*;


public class SearchList extends AppCompatActivity {

    //NEWSApi key
    String key = null;

    //List stuff
    ArrayList<Item> items = null;
    ListAdapter adapter = null;
    ListView listView;

    //News source
    String source = null;

    //For more page loading
    int page = 1;
    boolean loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        listView = findViewById(R.id.list_view);

        key = this.getString(R.string.key);

        //Load more articles when reach bottom of list
        listView.setOnScrollListener(
                new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if(firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0)
                        {
                            if(!loading)
                            {
                                loading = true;
                                page ++;
                                getArticles(source);
                            }
                        }

                    }
                });

        // Get the Intent that started this activity and extract the string
        // (aka the news source)
        Intent intent = getIntent();
        source = intent.getStringExtra("source");

        //get the articles from the news source
        getArticles(source);

    }

    private void getArticles(final String source)
    {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =
                "https://newsapi.org/v2/everything?domains=" + source + "&sortBy=popularity&pageSize=20&page=" + page +
                        "&apiKey=" + key;

        Log.i("INFO", url);
        // Request a response from the provided URL.
        JsonObjectRequest articleRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray articles = new JSONArray();

                        //Get the articles from the response
                        try {
                            articles = response.getJSONArray("articles");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //if it is the first time loading the articles do this:
                        if(page == 1) {
                            //Create list & adapter
                            //Call jsonToList-method to populate it
                            items = jsonToListItem(articles);
                            adapter = new ListAdapter(getApplicationContext(), items);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(messageClickedHandler);
                        }
                        //if loading more pages after 1
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Loading more articles",
                                    Toast.LENGTH_SHORT).show();
                            items.addAll(jsonToListItem(articles));
                            adapter.notifyDataSetChanged();
                            loading = false;

                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
        }) {
            //Added to bypass 403
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("User-Agent", "Mozilla/5.0");
                return params;
            }
        };

        queue.add(articleRequest);
        
    }

    //Reads from the incoming JSONArray and created individual article items
    //Puts said items in an Array and returns it.
    private ArrayList<Item> jsonToListItem(JSONArray articles) {
        ArrayList<Item> items = new ArrayList<>();

        for(int i = 0; i < articles.length(); i ++)
        {
            try {
                JSONObject o = articles.getJSONObject(i);

                //Make date more readable by excluding seconds and chars
                String date = o.getString("publishedAt");
                date = date.substring(0,10) + "   " + date.substring(11,16);

                //if author is not a ~human person~ don't write anything in author field
                String author = o.getString("author");
                if(author.startsWith("https") || author.equals("null"))
                    author = "";

                //Add each article as a new Item.
                 Item item = new Item(o.getString("title"), o.getString("description"), o.getString("content"), author,
                         date, o.getString("url"), null);

                 //Get pictures for each item, but also don't do it if the article doesn't have an image
                if(!o.getString("urlToImage").equals("null"))
                    getArticleImage(o,item);

                items.add(item);

            } catch (JSONException e) {
               e.printStackTrace();

            }

        }

        return items;
    }


    private void getArticleImage(final JSONObject article, final Item item) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = article.getString("urlToImage");

        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {

                        //Assign the response to the articles image
                        //Notify that a change has occurred to update the listview
                        Bitmap resized = Bitmap.createScaledBitmap(response, 1000, 500, true);
                        item.setImage(resized);
                        adapter.notifyDataSetChanged();

                    }
                },
                0,
                0,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.RGB_565,

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        // Add ImageRequest to the RequestQueue
        queue.add(imageRequest);
    }


    //ListView listener
    private final AdapterView.OnItemClickListener messageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {

            //The clicked item
            Item item = (Item) parent.getItemAtPosition(position);

            //Make article image into byte array
            //check for image = null
            Bitmap bmp = item.getImage();
            byte[] byteArray = null;

            if(bmp != null)
            {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
            }


            //Rest of data
            //Title, author, date, content, url
            String[] data = new String[]{item.getTitle(),item.getAuthor(),item.getDate(),item.getContent(),item.getUrl()};

            //Create intent with data,and switch activity
            Intent intent = new Intent(getApplicationContext(), Article.class);
            intent.putExtra("image", byteArray);
            intent.putExtra("article", data);
            startActivity(intent);
        }
    };

}

