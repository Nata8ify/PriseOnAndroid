package com.trag.quartierlatin.prise.extra;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trag.quartierlatin.prise.R;

import java.util.*;

/**
 * Created by PNattawut on 20-Dec-16.
 */


public class LogAdapter extends ArrayAdapter<EventLogging> {

    private ArrayList<EventLogging> eventLoggings;
    private Context context;

    public LogAdapter(Context context, int resource, ArrayList<EventLogging> eventLoggings) {
        super(context, resource, eventLoggings);
        this.context = context;
        this.eventLoggings = eventLoggings;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View row = layoutInflater.inflate(R.layout.log_row, parent, false);
        TextView txtLogByName = (TextView) row.findViewById(R.id.txt_log_by_name);
        TextView txtLogDetail = (TextView) row.findViewById(R.id.txt_log_detail);
        TextView txtLogDateTime = (TextView) row.findViewById(R.id.txt_log_datetime);
        txtLogByName.setText(eventLoggings.get(position).getlByName());
        txtLogDetail.setText(this.logStatementBuilder(eventLoggings.get(position)));
        txtLogDateTime.setText(eventLoggings.get(position).getLogDate());
        if(position == eventLoggings.size()-1){
            ((RelativeLayout)row.findViewById(R.id.relay_logroot)).setBackgroundColor(Color.GRAY);
        }
        return row;
    }

    private String logStatementBuilder(EventLogging eLog) {
        switch (eLog.getLogType()) {
            case 11:
                return eLog.getVargs1() +" "+ context.getResources().getString(R.string.viewguest_dialog_logtype_11);
            case 12:
                return eLog.getVargs1() +" "+ context.getResources().getString(R.string.viewguest_dialog_logtype_12);
            case 13:
                return eLog.getVargs1() +" "+ context.getResources().getString(R.string.viewguest_dialog_logtype_13);
            case 50:
                return eLog.getVargs1() +" "+ context.getResources().getString(R.string.viewguest_dialog_logtype_50);
            case 51:
                return eLog.getVargs1() +" "+ context.getResources().getString(R.string.viewguest_dialog_logtype_51);
            case 54:
                return eLog.getVargs1() +" "+ context.getResources().getString(R.string.viewguest_dialog_logtype_54);
            case 99:
                return eLog.getVargs1() +" "+ context.getResources().getString(R.string.viewguest_dialog_logtype_99);
            default:
                return "Undefined Event [No LogType : " + eLog.getLogType() + "]";
        }
    }


}
