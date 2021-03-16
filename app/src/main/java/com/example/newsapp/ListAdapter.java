package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Item> items;

    public ListAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.list_item, parent, false);
        }

        // get current item to be displayed
        Item currentItem = (Item) getItem(position);

        // get the TextView for item name and item description
        TextView title = convertView.findViewById(R.id.titleField);

        TextView description = convertView.findViewById(R.id.descriptionField);

        ImageView image = convertView.findViewById(R.id.articleImage);

        TextView author = convertView.findViewById(R.id.authorField);
        TextView date = convertView.findViewById(R.id.dateField);


        //sets the text for item name and item description from the current item object
        title.setText(currentItem.getTitle());
        description.setText(currentItem.getDescription());
        author.setText(currentItem.getAuthor());
        date.setText(currentItem.getDate());
        image.setImageBitmap(currentItem.getImage());

        // returns the view for the current row
        return convertView;
    }

}
