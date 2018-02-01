package com.trag.quartierlatin.prise;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.trag.quartierlatin.prise.extra.ExtraUtils;
import com.trag.quartierlatin.prise.extra.PriseWebAppFactors;
import com.trag.quartierlatin.prise.extra.User;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//import xyz.hanks.library.SmallBang;

public class LoginMainActivity extends AppCompatActivity {

    @BindView(R.id.edtx_username)
    EditText edtxUsername;
    @BindView(R.id.edtx_passsword)
    EditText edtxPasssword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.linray_root_login_form)
    LinearLayout linrayRootLoginForm;
    @BindView(R.id.img_user)
    ImageView imgUser;
    @BindView(R.id.txt_invalid_user)
    TextView txtInvalidUser;
    @BindView(R.id.txtlnk_newaccount)
    TextView txtLinkCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        ButterKnife.bind(this);
        doInitialOnCreate();
        doListenerOnCreate();
//        doAnimationOnCreate();
    }

    private void doInitialOnCreate() {
        edtxPasssword.setVisibility(View.GONE);
    }

    private void doListenerOnCreate() {
        edtxUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edtxUsername.getText().toString().length() == 1) {
                    edtxPasssword.setVisibility(View.VISIBLE);
                    edtxPasssword.setAlpha(0);
                    edtxPasssword.animate()
                            .alpha(1);
//                    Log.v("KEY","KEY");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtLinkCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do Browser Link!
            }
        });
    }

//    private SmallBang smallBang;
//
//    private void doAnimationOnCreate() {
//        smallBang = new SmallBang(this);
//        smallBang.bang(imgUser);
//    }


    @OnClick(R.id.btn_login)
    public void onClick() {
        // ProgressDialog
        if (edtxUsername.getText().toString().equals("") | edtxPasssword.getText().toString().equals("")) {
            Toast.makeText(this, R.string.login_login_not_complete, Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this,R.string.login_login_not_complete, Toast.LENGTH_SHORT).show();
            if (ExtraUtils.isNetworkAvailable(LoginMainActivity.this)) {
                new LoginAsyncTask().execute(new String[]{edtxUsername.getText().toString()
                        , edtxPasssword.getText().toString()});
            }
        }


    }

    class LoginAsyncTask extends AsyncTask<String, Void, Void> {

        private ProgressDialog loginProgressDialog;
        private boolean isNoUsername;

        @Override
        protected void onPreExecute() {
            loginProgressDialog = ProgressDialog.show(LoginMainActivity.this, ""
                    , LoginMainActivity.this.getResources().getString(R.string.progress)
                    , false);
            loginProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            User user;

            try {

                JsonObject userJSON = Ion.with(LoginMainActivity.this)
                        .load(PriseWebAppFactors.URL_APP_LOGIN)
                        .setBodyParameter("username", params[0])
                        .setBodyParameter("password", params[1])
                        .asJsonObject()
                        .get();
                Log.v("userJSON", userJSON.toString());
                user = new User(
                        userJSON.get("userId").getAsInt()
                        , userJSON.get("username").getAsString()
                        , userJSON.get("password").getAsString()
                        , userJSON.get("email").getAsString()
                        , userJSON.get("name").getAsString());
                if (user != null) { // Do keep user info here.
                    Intent intent = new Intent(LoginMainActivity.this, ViewEventActivity.class);
                    intent.putExtra("userId", user.getUserId());
                    intent.putExtra("username", user.getUsername());
                    intent.putExtra("password", user.getPassword());
                    intent.putExtra("email", user.getEmail());
                    intent.putExtra("privilage", user.getPrivilage());
                    intent.putExtra("name", user.getName());
                    startActivity(new Intent(intent));

                    finish();
                } else {
                    Intent toSignup = new Intent(LoginMainActivity.this, SignupActivity.class);
                    toSignup.putExtra("username", params[0]);
                    startActivity(toSignup);
                    finish();

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                // Login Failed
                e.printStackTrace();
            } catch (Exception e) {
                // Login Failed
//                Intent toSignup = new Intent(LoginMainActivity.this, SignupActivity.class);
//                toSignup.putExtra("username", params[0]);
//                startActivity(toSignup);
                isNoUsername = true;
                e.printStackTrace();
//                Toast.makeText(LoginMainActivity.this, getResources().getString(R.string.login_login_no_user), Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (isNoUsername) { //User check and animate message
                txtInvalidUser.animate()
                        .alpha(1)
                        .alphaBy(0)
                        .setDuration(1000)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {
                                txtInvalidUser.setText(getResources().getString(R.string.login_login_no_user));
                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        })
                .start();

            }
            loginProgressDialog.dismiss();
        }
    }

    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void viewClickCreateNexAccount(View v){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PriseWebAppFactors.URL_APP_REGISTER)));
    }
}
