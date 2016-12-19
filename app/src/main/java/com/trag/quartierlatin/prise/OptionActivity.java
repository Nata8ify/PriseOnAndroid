package com.trag.quartierlatin.prise;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OptionActivity extends AppCompatActivity {


    @BindView(R.id.spnr_lang)
    Spinner spnrLang;
    @BindView(R.id.btn_okay)
    Button btnOkay;
    private ArrayAdapter langsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        ButterKnife.bind(this);
        initView();
    }


    private Locale locale;
    private Configuration conf;
    private String localeLang;
    private String localeCountry;
    private SharedPreferences prefs;

    private void initView() {
        // Config Init
        conf = new Configuration();

        //View Init
        langsAdapter = new ArrayAdapter(OptionActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.option_lang_ary));
        spnrLang.setAdapter(langsAdapter);

        //Listener Init
    }

    @OnClick(R.id.btn_okay)
    public void onClick() {

        switch (spnrLang.getSelectedItemPosition()) {
            case 0:
                localeLang = "en";
                localeCountry = "US";
                Log.v("CASE:", localeCountry);
                break;
            case 1:
                localeLang = "th";
                localeCountry = "TH";
                Log.v("CASE:", localeCountry);
                break;
        }
        saveLocale();
    }

    public void saveLocale() {
        locale = new Locale(localeLang, localeCountry);
        conf = new Configuration();
        conf.locale = this.locale;
        getApplicationContext().getResources().updateConfiguration(this.conf, null);
//        onConfigurationChanged(conf);
        prefs = getSharedPreferences("Commonrefs", OptionActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Language", localeLang);
        editor.commit();
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.option_pls_restart), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
