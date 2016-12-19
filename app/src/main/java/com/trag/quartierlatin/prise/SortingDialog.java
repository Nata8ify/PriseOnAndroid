package com.trag.quartierlatin.prise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SortingDialog extends AppCompatActivity {

    @BindView(R.id.rbtn_desc)
    RadioButton rbtnDesc;
    @BindView(R.id.rbtn_asc)
    RadioButton rbtnAsc;
    @BindView(R.id.linray_sorting_type)
    LinearLayout linraySortingType;
    @BindView(R.id.spnr_sorting)
    Spinner spnrSorting;
    @BindView(R.id.linray_sorting_set)
    LinearLayout linraySortingSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sorting_dialog);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rbtn_desc, R.id.rbtn_asc, R.id.spnr_sorting})
    public void onClick(View view) {
        boolean isChecked = ((RadioButton)view).isChecked();
        switch (view.getId()) {
            case R.id.rbtn_desc:
                if(isChecked){
                    rbtnAsc.setChecked(false);
                }
                break;
            case R.id.rbtn_asc:
                if(isChecked){
                    rbtnDesc.setChecked(false);
                }
                break;
            case R.id.spnr_sorting:
                break;
        }
    }
}
