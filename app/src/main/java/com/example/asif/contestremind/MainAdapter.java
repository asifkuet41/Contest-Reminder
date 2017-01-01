package com.example.asif.contestremind;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * Created by asif on 1/2/17.
 */

public class MainAdapter extends ArrayAdapter<Integer> {

    private final Integer [] iconId;

    /**
     * Construct a new {@link MainAdapter}
     * @param context of the app
     * @param iconId of the contest site icon
     */
    public MainAdapter(Context context, Integer [] iconId){
        super(context,0,iconId);
        this.iconId=iconId;
    }

    /**
     * Return a list item view that displays the contest site at the given position
     *
     */
    public View getView(int position, View convertView, ViewGroup parent){
        // Check if there is an existing list item view that we can reuse.
        // Otherwise if convertView is null then inflate a new list item layout.
        View listItemView = convertView;
        if(listItemView ==  null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.contest_site_icon,parent,false);
        }
        // Find the ImageView with id image
        ImageView iconView = (ImageView) listItemView.findViewById(R.id.image);
        iconView.setImageResource(iconId[position]);
        return listItemView;
    }
}
