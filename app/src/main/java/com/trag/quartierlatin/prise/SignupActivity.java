package com.trag.quartierlatin.prise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.edtx_username)
    EditText edtxUsername;
    @BindView(R.id.edtx_passsword)
    EditText edtxPasssword;
    @BindView(R.id.btn_signup)
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        edtxUsername.setText(getIntent().getExtras().getString("username"));
    }

    @OnClick(R.id.btn_signup)
    public void onClick() {
    }
}
