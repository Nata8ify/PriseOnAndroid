package com.trag.quartierlatin.prise;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.trag.quartierlatin.prise.extra.Event;
import com.trag.quartierlatin.prise.extra.EventAdapter;
import com.trag.quartierlatin.prise.extra.ExtraUtils;
import com.trag.quartierlatin.prise.extra.PriseEngine;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewEventActivity extends AppCompatActivity {

    @BindView(R.id.listw_events)
    ListView listwEvents;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private RefreshInfoAsyncTask refreshTask;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        ButterKnife.bind(this);
        currentUserId = getIntent().getExtras().getInt("userId");
//        refreshTask = new RefreshInfoAsyncTask();
//        refreshTask.execute();
        doEventList(currentUserId);
    }


    //Content Provide
    private ArrayList<Event> events;

    private void doEventList(int userId) {
        events = PriseEngine.getEvents(ViewEventActivity.this, userId);
        EventAdapter eventAdapter = new EventAdapter(this, R.layout.event_row, events, currentUserId);
        listwEvents.setAdapter(eventAdapter);
        listwEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExtraUtils.isNetworkAvailable(ViewEventActivity.this)) {
                    new ProgressAsyncTask().execute(new Integer[]{position});
                }
            }
        });
        listwEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(ViewEventActivity.this)
                        .setItems(new String[]{ViewEventActivity.this.getResources().getString(R.string.delete)}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which + 1) {
                                    case 1:
                                        final AlertDialog alrtDelete = new AlertDialog.Builder(ViewEventActivity.this)
                                                .setMessage(ViewEventActivity.this.getResources().getString(R.string.areyousure))
                                                .setNegativeButton(ViewEventActivity.this.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        PriseEngine.deleteEvent(getIntent().getExtras().getInt("userId"), events.get(position).getEventId(), ViewEventActivity.this);
                                                        doEventList(getIntent().getExtras().getInt("userId"));
                                                    }
                                                })
                                                .setPositiveButton(ViewEventActivity.this.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .create();
                                        alrtDelete.show();
                                        break;
                                    default: // ToDo
                                }
                            }
                        })
                        .create();
                alertDialog.show();
                return true;
            }
        });

    }

    public void goInsertEvent(View v) {
        Intent insertEventIntent = new Intent(ViewEventActivity.this, InsertEventActivity.class);
        insertEventIntent.putExtra("userId", getIntent().getExtras().getInt("userId"));
        startActivity(insertEventIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ExtraUtils.isNetworkAvailable(this)) {
            doEventList(getIntent().getExtras().getInt("userId"));
        }
    }

    class ProgressAsyncTask extends AsyncTask<Integer, Integer, Long> {
        private ProgressDialog progressDialog;

        @Override
        protected Long doInBackground(Integer... params) {
            Intent viewGuestIntent = new Intent(ViewEventActivity.this, ViewGuestActivity.class);
            viewGuestIntent.putExtra("userId", events.get(params[0]).getUserId());
            viewGuestIntent.putExtra("eventId", events.get(params[0]).getEventId());
            viewGuestIntent.putExtra("thisUserName", getIntent().getExtras().getString("thisUserName"));
            viewGuestIntent.putExtra("eventName", events.get(params[0]).getEvent());
            viewGuestIntent.putExtra("eventDescr", events.get(params[0]).getDescription());
            Log.v("params", params[0] + "");
            startActivity(viewGuestIntent);
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ViewEventActivity.this, "", getResources().getString(R.string.progress));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Long aLong) {
            progressDialog.dismiss();
        }
    }

    class RefreshInfoAsyncTask extends AsyncTask {

        private Runnable refreshRunnable;
        private Handler refreshHandler;
        private final int INTERVAL = 3000;

        @Override
        protected void onPreExecute() {
            refreshHandler = new Handler();
            refreshRunnable = new Runnable() {
                @Override
                public void run() {
                    if (!ViewEventActivity.this.isDestroyed()) {
                        doEventList(getIntent().getExtras().getInt("userId"));
                        Log.v("refreshRunnable", "YEP!");
                    } else {
                        refreshHandler.removeCallbacks(refreshRunnable);
                    }
                }
            };
        }

        @Override
        protected Object doInBackground(Object[] params) {

            refreshHandler.postDelayed(refreshRunnable, INTERVAL);
//            refreshRunnable.run();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }
}


