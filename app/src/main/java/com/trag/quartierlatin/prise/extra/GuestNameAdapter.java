package com.trag.quartierlatin.prise.extra;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.trag.quartierlatin.prise.R;

import java.util.ArrayList;

/**
 * Created by PNattawut on 27-Dec-16.
 */

public class GuestNameAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<Guest> guests;
    private int resource;

    public GuestNameAdapter(Context context, int resource, ArrayList<Guest> guests) {
        super(context, resource, guests);
        this.guests = guests;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        row = layoutInflater.inflate(resource, parent, false);
        TextView guestName = (TextView)row.findViewById(R.id.txt_guestname);
        guestName.setText(this.guests.get(position).getGuestName());
        return row;
    }


}
