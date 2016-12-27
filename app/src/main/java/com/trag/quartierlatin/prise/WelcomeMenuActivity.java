package com.trag.quartierlatin.prise;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.trag.quartierlatin.prise.extra.ExtraUtils;
import com.trag.quartierlatin.prise.extra.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeMenuActivity extends AppCompatActivity {

    @BindView(R.id.txt_welcome)
    TextView txtWelcome;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.btnViewGuest)
    Button btnViewGuest;
    @BindView(R.id.btnCreatePrivilage)
    Button btnCreatePrivilage;
    @BindView(R.id.btnOption)
    Button btnOption;
    @BindView(R.id.btnAbout)
    Button btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_menu);
        ButterKnife.bind(this);
        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(10l);
        User.actName = getIntent().getExtras().getString("name");
        User.thisUserId = getIntent().getExtras().getInt("userId");
        doOnCreate();
    }

    private void doOnCreate() {
        txtWelcome.setText(getResources().getString(R.string.welcome_welcome) + " " + getIntent().getExtras().getString("name"));
    }

    //On Backpress


    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        AlertDialog alrtQuit = new AlertDialog.Builder(WelcomeMenuActivity.this)
                .setTitle(getResources().getString(R.string.welcome_alrtquit_title))
                .setMessage(getResources().getString(R.string.welcome_alrtquit_message))
                .setPositiveButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("whichh", which + "");
                    }
                })
                .setNegativeButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("whichh", which + "");
                        WelcomeMenuActivity.this.finish();
                    }
                })
                .create();
        alrtQuit.show();
    }

    @OnClick({R.id.btnViewGuest, R.id.btnCreatePrivilage, R.id.btnOption, R.id.btnAbout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnViewGuest:
                if (ExtraUtils.isNetworkAvailable(WelcomeMenuActivity.this)) {
                    Intent toEventIntent = new Intent(WelcomeMenuActivity.this, ViewEventActivity.class);
                    toEventIntent.putExtra("userId", getIntent().getExtras().getInt("userId"));
                    toEventIntent.putExtra("thisUserName", getIntent().getExtras().getString("name"));
                    startActivity(toEventIntent);
                }
                break;
            case R.id.btnCreatePrivilage:
                break;
            case R.id.btnOption:
                    startActivity(new Intent(WelcomeMenuActivity.this, OptionActivity.class));
                break;
            case R.id.btnAbout:
                startActivity(new Intent(WelcomeMenuActivity.this, AboutActivity.class));
                break;
        }
    }

}
