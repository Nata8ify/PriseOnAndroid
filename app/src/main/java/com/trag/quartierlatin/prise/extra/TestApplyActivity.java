package com.trag.quartierlatin.prise.extra;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rey.material.widget.Button;
import com.trag.quartierlatin.prise.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestApplyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_apply);
        ButterKnife.bind(this);
    }

}
