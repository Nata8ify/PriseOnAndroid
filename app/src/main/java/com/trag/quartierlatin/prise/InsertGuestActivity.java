package com.trag.quartierlatin.prise;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.trag.quartierlatin.prise.extra.ExtraUtils;
import com.trag.quartierlatin.prise.extra.PriseEngine;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InsertGuestActivity extends AppCompatActivity {

    @BindView(R.id.edtx_seatrow)
    EditText edtxSeatrow;
    @BindView(R.id.edtx_seatno)
    EditText edtxSeatno;
    @BindView(R.id.edtx_guestname)
    EditText edtxGuestname;
    @BindView(R.id.edtx_guestcorp)
    EditText edtxGuestcorp;
    @BindView(R.id.edtx_guestposition)
    EditText edtxGuestposition;
    @BindView(R.id.edtx_award)
    EditText edtxAward;
    @BindView(R.id.edtx_awardno)
    EditText edtxAwardno;
    @BindView(R.id.txt_status)
    TextView txtStatus;
    @BindView(R.id.spnr_status)
    Spinner spnrStatus;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.btnReset)
    Button btnReset;


    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_guest);
        ButterKnife.bind(this);
        spnrStatus.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{
                getResources().getString(R.string.viewguest_status_ready)
                , getResources().getString(R.string.viewguest_status_unready)
                , getResources().getString(R.string.viewguest_status_absent)
                , getResources().getString(R.string.viewguest_status_received)
                , getResources().getString(R.string.viewguest_status_quited)
        }));
        bundle = getIntent().getExtras();
    }

//    public void submitInsertGuest(View v){
//        PriseEngine.insertGuestInformation(edtxSeatrow.getText().toString()
//        , edtxSeatno.getText().toString()
//        , edtxGuestname.getText().toString()
//        , edtxGuestcorp.getText().toString()
//        , edtxGuestposition.getText().toString()
//        , edtxAward.getText().toString()
//        , Integer.valueOf(edtxAwardno.getText().toString())
//        , spnrStatus.getSelectedItemPosition()+1
//        , bundle.getInt("userId")
//        , bundle.getInt("eventId")
//        , InsertGuestActivity.this);
//    }
//
    private String[] guestInfos;
    public void submitInsertGuest(View v) {
        if(!edtxGuestname.getText().toString().equals("")
                & !edtxAward.getText().toString().equals("")
                & !edtxAwardno.getText().toString().equals("")){
            Log.v("NOT NULL:",edtxSeatrow.getText().toString());
            guestInfos = new String[10];
            guestInfos[0] = edtxSeatrow.getText().toString();
            guestInfos[1] = edtxSeatno.getText().toString();
            guestInfos[2] = edtxGuestname.getText().toString();
            guestInfos[3] = edtxGuestcorp.getText().toString();
            guestInfos[4] = edtxGuestposition.getText().toString();
            guestInfos[5] = edtxAward.getText().toString();
            guestInfos[6] = edtxAwardno.getText().toString();
            guestInfos[7] = spnrStatus.getSelectedItemPosition() + 1 + "";
            guestInfos[8] = String.valueOf(bundle.getInt("userId"));
            guestInfos[9] = String.valueOf(bundle.getInt("eventId"));
//                    edtxSeatrow.getText().toString()
//                    , edtxSeatno.getText().toString()
//                    , edtxGuestname.getText().toString()
//                    , edtxGuestcorp.getText().toString()
//                    , edtxGuestposition.getText().toString()
//                    , edtxAward.getText().toString()
//                    , edtxAwardno.getText().toString()
//                    , spnrStatus.getSelectedItemPosition() + 1 + ""
//                    , String.valueOf(bundle.getInt("userId"))
//                    , String.valueOf(bundle.getInt("eventId"))
//            };
            if(ExtraUtils.isNetworkAvailable(this)) {
                new InsertAsyncTask().execute(guestInfos);
            }
                 } else {
            AlertDialog alertEmptyAwardNoDialog = new AlertDialog.Builder(InsertGuestActivity.this)
                    .setTitle(InsertGuestActivity.this.getResources().getString(R.string.insertguest_alrt_empty_title))
                    .setMessage(InsertGuestActivity.this.getResources().getString(R.string.insertguest_alrt_empty_message))
                    .setNegativeButton(InsertGuestActivity.this.getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
            alertEmptyAwardNoDialog.show();
        }
    }

    public void resetGuest(View v) {
        edtxSeatrow.setText("");
        edtxSeatno.setText("");
        edtxGuestname.setText("");
        edtxGuestcorp.setText("");
        edtxGuestposition.setText("");
        edtxAward.setText("");
        edtxAwardno.setText("");

    }

    class InsertAsyncTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(String... params) {

                // AwradNo can't be ""
                PriseEngine.insertGuestInformation(params[0]
                        , params[1]
                        , params[2]
                        , params[3]
                        , params[4]
                        , params[5]
                        , Integer.valueOf(params[6])
                        , Integer.valueOf(params[7])
                        , Integer.valueOf(params[8])
                        , Integer.valueOf(params[9])
                        , InsertGuestActivity.this);

            return null;
        }



        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(InsertGuestActivity.this, ""
                    , InsertGuestActivity.this.getResources().getString(R.string.progress));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
        }
    }
}
