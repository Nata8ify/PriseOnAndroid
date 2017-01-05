package com.trag.quartierlatin.prise.extra;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.trag.quartierlatin.prise.R;

import java.util.ArrayList;


/**
 * Created by QuartierLatin on 14/6/2559.
 */
public class GuestAdapter extends ArrayAdapter<Guest> {

    private Context context;
    private int resource;
    private ArrayList<Guest> guestArrayList = null;

    private int asUserId;
    private int asEventId;

    public int getAsUserId() {
        return asUserId;
    }

    public void setAsUserId(int asUserId) {
        this.asUserId = asUserId;
    }

    public int getAsEventId() {
        return asEventId;
    }

    public void setAsEventId(int asEventId) {
        this.asEventId = asEventId;
    }

    public ArrayList<Guest> getGuestArrayList() {
        return guestArrayList;
    }

    public void setGuestArrayList(ArrayList<Guest> guestArrayList) {
        this.guestArrayList = guestArrayList;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public GuestAdapter(Context context, int resource, ArrayList<Guest> guestArrayList) {
        super(context, resource, guestArrayList);
        this.context = context;
        this.resource = resource;
        this.guestArrayList = guestArrayList;
//        Log.v("EEE","GuestAdapter Setting");
    }

    public GuestAdapter(Context context, int resource, ArrayList<Guest> guestArrayList, int asUserId, int asEventId) {
        super(context, resource, guestArrayList);
        this.context = context;
        this.resource = resource;
        this.guestArrayList = guestArrayList;
        this.asUserId = asUserId;
        this.asEventId = asEventId;
//        Log.v("EEE","GuestAdapter Setting");
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // View Setting
        View row;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        row = layoutInflater.inflate(this.resource, parent, false);
//        Log.v("EEE","View Setting");
        // Elements Setting
        TextView txtSeatRow = (TextView) row.findViewById(R.id.txt_seat_row);
        TextView txtSeatNo = (TextView) row.findViewById(R.id.txt_seat_no);
        TextView txtAward = (TextView) row.findViewById(R.id.txt_award);
        TextView txtGuestName = (TextView) row.findViewById(R.id.txt_name);
//        Spinner statusSpinner = (Spinner)row.findViewById(R.id.statusspinner);
        ImageView imgStatus = (ImageView) row.findViewById(R.id.img_status);
//        Button btnSample = (Button)row.findViewById(R.id.btnSample);
//        Log.v("EEE","Elements Setting");
        // Contents Setting
//        statusSpinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, context.getResources().getStringArray(R.array.status )));
//        statusSpinner.setVisibility(View.GONE);
        final Guest guest = guestArrayList.get(position);


        txtSeatRow.setText(guest.getSeatRow().equals("") ? "-" : guest.getSeatRow());
        txtSeatNo.setText(guest.getSeatNo().equals("") ? "-" : guest.getSeatNo());
        txtAward.setText(context.getResources().getString(R.string.viewguest_award) + " " + guest.getAward() + "");
        txtGuestName.setText(guest.getGuestName());
        switch (guest.getStatus()) {
            case 1:
                imgStatus.setImageResource(context.getResources().getIdentifier("ico_status_onseat", "drawable", getContext().getPackageName()));
                break;
            case 2:
                imgStatus.setImageResource(context.getResources().getIdentifier("ico_status_unready", "drawable", getContext().getPackageName()));
                break;
            case 3:
                imgStatus.setImageResource(context.getResources().getIdentifier("ico_status_absent", "drawable", getContext().getPackageName()));
                break;
            case 4:
                imgStatus.setImageResource(context.getResources().getIdentifier("ico_status_award_received", "drawable", getContext().getPackageName()));
                break;
            case 5:
                imgStatus.setImageResource(context.getResources().getIdentifier("ico_status_already_quit", "drawable", getContext().getPackageName()));
                break;
            default: //TODO
        }
        imgStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog statusAlertDialog = new AlertDialog.Builder(context)
                        .setAdapter(new StatusAdapter(context, R.layout.status_row, new Integer[]{1, 2, 3, 4, 5}), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (ExtraUtils.isNetworkAvailable(context)) {

                                    // To convert position to conditionNo
                                    int conditionNo = -1;
                                    switch (which) {
                                        case 0:
                                            conditionNo = 3;
                                            break;
                                        case 1:
                                            conditionNo = 4;
                                            break;
                                        case 4:
                                            conditionNo = 5;
                                            break;
                                        default:
                                            new StatusUpdateAsyncTask().execute(which, GuestAdapter.this.getGuestArrayList(), position);
                                            return;
                                    }
                                    if (conditionNo != -1) {
                                        new StatusUpdateAsyncTask().execute(which, GuestAdapter.this.getGuestArrayList(), position);
                                        PriseEngine.saveLog(guest.getEventId(), guest.getUserId(), User.actName, guest.getGuestName(), conditionNo, guest.getGuestNo(), context);
                                    }
                                }
                            }
                        })
                        .create();
                statusAlertDialog.show();
            }
        });
//        Log.v("EEE","Contents Setting");

        return row;
    }

    class StatusUpdateAsyncTask extends AsyncTask {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "", context.getString(R.string.progress));
            progressDialog.show();
//            getRefreshPosition();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            Log.v("ProgressDialog", progressDialog.getProgress() + "");
            PriseEngine.editGuestStatus((int) params[0] + 1
                    , ((ArrayList<Guest>) params[1]).get((int) (params[2])).getGuestNo()
                    , getAsUserId()
                    , getAsEventId()
                    , context);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (ExtraUtils.isNetworkAvailable(context)) {
                if (PriseEngine.priseFilter == null) {
                    GuestAdapter.this.guestArrayList.clear();
                    GuestAdapter.this.guestArrayList.addAll(PriseEngine.getSortedGuestArrayList(GuestAdapter.this.context, getAsUserId(), getAsEventId(), PriseEngine.sortById, PriseEngine.sortTypeId));
                    GuestAdapter.this.notifyDataSetChanged();
                } else {
                    PriseEngine.getFilterGuestsList(context, GuestAdapter.this, PriseEngine.userId, PriseEngine.eventId, GuestAdapter.this.guestArrayList, PriseEngine.priseFilter);
                }
            }
            progressDialog.dismiss();
        }
    }
}
