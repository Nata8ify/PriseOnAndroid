package com.trag.quartierlatin.prise;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.trag.quartierlatin.prise.extra.PriseEngine;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditGuestActivity extends AppCompatActivity {

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
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.linrlay_guest_editor_form)
    LinearLayout linrlayGuestEditorForm;

    //Necessary Factors
    private Bundle activityBundel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_guest);
        ButterKnife.bind(this);
        initial();
    }

    private void initial() {
        activityBundel = getIntent().getExtras();
        edtxSeatrow.setText(activityBundel.getString("seatRow"));
        edtxSeatno.setText(String.valueOf(activityBundel.getInt("seatNo")));
        edtxGuestname.setText(activityBundel.getString("gName"));
        edtxGuestcorp.setText(activityBundel.getString("gCorp"));
        edtxGuestposition.setText(activityBundel.getString("gPosition"));
        edtxAward.setText(activityBundel.getString("award"));
        edtxAwardno.setText(String.valueOf(activityBundel.getInt("awardNo")));
    }

    public void submitEditGuest(View v){
        if(edtxGuestname.getText().toString().equals("")
                | edtxAward.getText().toString().equals("")
                | edtxAwardno.getText().toString().equals("")){
            Log.v("isPass","NOT PASS!");
        } else {
            final ProgressDialog editProgress = ProgressDialog.show(EditGuestActivity.this, ""
                    , EditGuestActivity.this.getResources().getString(R.string.progress)
                    , true);
            PriseEngine.editGuestInformation(edtxSeatrow.getText().toString()
                    , edtxSeatno.getText().toString()
                    , edtxGuestname.getText().toString()
                    , edtxGuestcorp.getText().toString()
                    , edtxGuestposition.getText().toString()
                    , edtxAward.getText().toString()
                    , Integer.valueOf(edtxAwardno.getText().toString())
                    , activityBundel.getInt("gStatus")
                    , activityBundel.getInt("userId")
                    , activityBundel.getInt("eventId")
                    , activityBundel.getInt("gNo")
                    , EditGuestActivity.this);
            AlertDialog alrtFinish = new AlertDialog.Builder(EditGuestActivity.this)
                    .setTitle(getResources().getString(R.string.editguest_alrt_finish_title))
                    .setPositiveButton(getResources().getString(R.string.goback), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editProgress.dismiss();
                            EditGuestActivity.this.finish();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.dismiss), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editProgress.dismiss();
                        }
                    })
                    .create();
            alrtFinish.show();

        }
    }

}
