package com.trag.quartierlatin.prise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.trag.quartierlatin.prise.extra.ExtraUtils;
import com.trag.quartierlatin.prise.extra.PriseEngine;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InsertEventActivity extends AppCompatActivity {

    @BindView(R.id.edtx_eventname)
    EditText edtxEventname;
    @BindView(R.id.edtx_eventdesc)
    EditText edtxEventdesc;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_event);
        ButterKnife.bind(this);
        bundle = getIntent().getExtras();
    }

    // Do insert new event.
    public void submitThisEvent(View v) {
        if(ExtraUtils.isNetworkAvailable(this)) {
            PriseEngine.insertEvent(bundle.getInt("userId")
                    , edtxEventname.getText().toString()
                    , edtxEventdesc.getText().toString()
                    , InsertEventActivity.this);
            Log.v("elements", bundle.getInt("userId") + edtxEventname.getText().toString() + edtxEventdesc.getText().toString());
        }
    }
}
