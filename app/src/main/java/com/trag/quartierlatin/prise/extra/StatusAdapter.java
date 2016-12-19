package com.trag.quartierlatin.prise.extra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.trag.quartierlatin.prise.R;

import org.w3c.dom.Text;

/**
 * Created by QuartierLatin on 21/6/2559.
 */
public class StatusAdapter extends ArrayAdapter<Integer> {

    private Context context;
    private int layoutResource;
    private Integer[] status ;

    public StatusAdapter(Context context, int resource, Integer[] status) {
        super(context, resource, status);
        this.context = context;
        this.layoutResource = resource;
        this.status = new Integer[]{1, 2, 3, 4, 5};
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        row = layoutInflater.inflate(layoutResource, parent, false);

        ImageView imgStatus = (ImageView)row.findViewById(R.id.img_status);
        TextView txtStatus = (TextView)row.findViewById(R.id.txt_status);
        Integer status = this.status[position];
        switch (status){
            case 1 :
                imgStatus.setImageResource(context.getResources().getIdentifier("ico_status_onseat", "drawable", context.getPackageName() ));
                txtStatus.setText(context.getResources().getString(R.string.viewguest_status_ready));
                break;
            case 2 :
                imgStatus.setImageResource(context.getResources().getIdentifier("ico_status_unready", "drawable", context.getPackageName() ));
                txtStatus.setText(context.getResources().getString(R.string.viewguest_status_unready));
                break;
            case 3 :
                imgStatus.setImageResource(context.getResources().getIdentifier("ico_status_absent", "drawable", context.getPackageName() ));
                txtStatus.setText(context.getResources().getString(R.string.viewguest_status_absent));
                break;
            case 4 :
                imgStatus.setImageResource(context.getResources().getIdentifier("ico_status_award_received", "drawable", context.getPackageName() ));
                txtStatus.setText(context.getResources().getString(R.string.viewguest_status_received));
                break;
            case 5 :
                imgStatus.setImageResource(context.getResources().getIdentifier("ico_status_already_quit", "drawable", context.getPackageName() ));
                txtStatus.setText(context.getResources().getString(R.string.viewguest_status_quited));
                break;
            default : // TODO
        }
        return row;
    }
}
