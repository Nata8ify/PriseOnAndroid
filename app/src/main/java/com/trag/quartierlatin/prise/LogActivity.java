package com.trag.quartierlatin.prise;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.rey.material.widget.ImageButton;
import com.rey.material.widget.TextView;
import com.trag.quartierlatin.prise.extra.EventLogging;
import com.trag.quartierlatin.prise.extra.Guest;
import com.trag.quartierlatin.prise.extra.GuestNameAdapter;
import com.trag.quartierlatin.prise.extra.LogAdapter;
import com.trag.quartierlatin.prise.extra.PriseEngine;
import com.trag.quartierlatin.prise.extra.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogActivity extends AppCompatActivity {

    @BindView(R.id.listw_log)
    ListView listwLog;
    @BindView(R.id.btn_log_see_latest)
    Button btnLogSeeLatest;
    @BindView(R.id.btn_log_altmsg)
    Button btnLogAltmsg;
    @BindView(R.id.btn_log_empty)
    Button btnLogEmpty;
    @BindView(R.id.activity_log)
    LinearLayout activityLog;

    private LogAdapter logAdapter;
    private ArrayList<EventLogging> eventLoggings;
    private Handler handler;
    private Runnable runnable;
    private static int logCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        ButterKnife.bind(this);
        emptyButtonForHost(User.thisUserId);
        initLogData();
        handler = new Handler();
        this.doUpdate();
    }


    private void initLogData() {
        this.eventLoggings = PriseEngine.getLog(
                PriseEngine.userId,
                PriseEngine.eventId,
                LogActivity.this
        );
        if (eventLoggings != null) {
            this.logAdapter = new LogAdapter(LogActivity.this, R.layout.log_row, eventLoggings);
            this.listwLog.setAdapter(logAdapter);
            this.logCount = eventLoggings.size();
        }
    }

    private void doUpdate() {

        runnable = new Runnable() {
            @Override
            public void run() { //<-- Bug Nullpointter
                if (eventLoggings != null) {
                    eventLoggings.clear();
                    try{
                    eventLoggings.addAll(PriseEngine.getLog(
                            PriseEngine.userId,
                            PriseEngine.eventId,
                            LogActivity.this));
                    } catch (NullPointerException npex){
                        Toast.makeText(LogActivity.this, npex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    logAdapter.notifyDataSetChanged();
                    if (LogActivity.this.logCount < LogActivity.this.eventLoggings.size()) {
                        LogActivity.this.logCount = LogActivity.this.eventLoggings.size();
                        Toast.makeText(LogActivity.this, getResources().getString(R.string.txt_toast_newlog), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LogActivity.this, getResources().getString(R.string.txt_toast_nodata), Toast.LENGTH_SHORT).show();
                    initLogData();
                }
                handler.postDelayed(this, 3000);
            }
        };
        runnable.run();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        finish();
    }

    private ArrayList<Guest> guests;
    private LoadGuestsAsyncTask loadGuestsAsyncTask;
    private int thisGuestNo;
    @OnClick({R.id.btn_log_see_latest, R.id.btn_log_altmsg, R.id.btn_log_empty})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_log_see_latest:
                if ((this.eventLoggings != null)) {
                    listwLog.setSelection((this.eventLoggings.size() - 1));
                } else {
                    Toast.makeText(LogActivity.this, getResources().getString(R.string.txt_toast_nodata), Toast.LENGTH_SHORT).show();
                    }
                break;
            case R.id.btn_log_altmsg:
                final AlertDialog alrtMessage = new AlertDialog.Builder(LogActivity.this)
                        .setView(R.layout.message_dialog)
                        .create();
                alrtMessage.show();

                Button btnMsgSubmit = (Button)alrtMessage.findViewById(R.id.btn_msgsubmit);
                final android.widget.TextView txtGuestName = (android.widget.TextView)alrtMessage.findViewById(R.id.txt_logmsg_guestname);
                android.widget.ImageButton imgBtnAutoName = (android.widget.ImageButton)alrtMessage.findViewById(R.id.btn_autoname);
                final Spinner spnrStatus = (Spinner)alrtMessage.findViewById(R.id.spnr_status);
                final EditText edtxtMessage = (EditText)alrtMessage.findViewById(R.id.edtxt_message);
                spnrStatus.setAdapter(new ArrayAdapter<String>(LogActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.status)));
                spnrStatus.setSelection(1);

                if(txtGuestName.getText().toString().isEmpty()){
                    txtGuestName.setVisibility(View.GONE);
                }
                // gdATA MAY NOT LATEST.
                loadGuestsAsyncTask = new LoadGuestsAsyncTask();
                loadGuestsAsyncTask.execute();
                imgBtnAutoName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        loadGuestsAsyncTask.execute(); <-- Loading should be here.
                        AlertDialog alrtGuests = new AlertDialog.Builder(LogActivity.this)
                                .setAdapter(new GuestNameAdapter(LogActivity.this, R.layout.guestmsg_row, guests), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        txtGuestName.setVisibility(View.VISIBLE);
                                        thisGuestNo = guests.get(i).getGuestNo();
                                        txtGuestName.setText(guests.get(i).getGuestName());
                                        edtxtMessage.append(" "+guests.get(i).getGuestName());
                                    }
                                })
                                .create();
                        alrtGuests.show();
                    }
                });
                btnMsgSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PriseEngine.editGuestStatus(spnrStatus.getSelectedItemPosition()+1, thisGuestNo, PriseEngine.userId, PriseEngine.eventId, LogActivity.this);

                        //Exception for guestName to Varargs
                        PriseEngine.saveLog(PriseEngine.eventId, PriseEngine.userId, User.actName, edtxtMessage.getText().toString(), 99, LogActivity.this );
                    }
                });
                break;
            case R.id.btn_log_empty:
                PriseEngine.emptyLog(PriseEngine.userId,
                        PriseEngine.eventId, LogActivity.this);
                clearListVew();
                initLogData();
                break;
        }
    }

    private void emptyButtonForHost(int thisUserId){
        if(thisUserId != PriseEngine.userId){
            btnLogEmpty.setVisibility(View.GONE);
        }
    }

    private void clearListVew() {
        if ((this.eventLoggings != null)) {
            this.eventLoggings.clear();
            logAdapter.notifyDataSetChanged();
        }
    }


    class LoadGuestsAsyncTask extends AsyncTask<Integer, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(LogActivity.this, "", LogActivity.this.getResources().getString(R.string.progress));
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            LogActivity.this.guests = PriseEngine.getGuestArrayList(LogActivity.this, PriseEngine.userId, PriseEngine.eventId);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
        }
    }
}
