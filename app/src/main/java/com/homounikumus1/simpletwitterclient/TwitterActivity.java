package com.homounikumus1.simpletwitterclient;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import java.util.Objects;

public class TwitterActivity extends AppCompatActivity implements View.OnClickListener {
    public static int fragment = 1;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        if (savedInstanceState!=null) {
            fragment = savedInstanceState.getInt("fragment");
            if (fragment>2) {
                fragment = 1;
            }
        }

        Intent intent = getIntent();
        if (intent!=null) {
            userName = intent.getStringExtra("userName");
        }

        ImageButton message = findViewById(R.id.message);
        Button homeTimeLine = findViewById(R.id.homeTimeLine);
        ImageButton userTimeLine = findViewById(R.id.userTimeLine);

        userTimeLine.setOnClickListener(this);
        message.setOnClickListener(this);
        homeTimeLine.setOnClickListener(this);

        fragment(fragment, userName);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("fragment", fragment);
    }

    @Override
    public void onBackPressed() {
        if (fragment == 1) {
            this.finish();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit:
                signOut();
                return true;
            case R.id.search:
                search();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void search() {
        LayoutInflater li = getLayoutInflater();
        @SuppressLint("InflateParams") final View editView = li.inflate(R.layout.edit, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(editView);

        final AlertDialog alert = builder.create();
        final String[] editableText = {""};
        Button okBtn = editView.findViewById(R.id.ok_btn);
        Button cancelBtn = editView.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editableText[0].equals("")) {

                    Fragment fragment = SearchTimelineFragment.newInstance(editableText[0]);
                    transaction(fragment);
                    alert.cancel();
                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.enter), Toast.LENGTH_SHORT).show();
                }

            }
        });

        EditText text = editView.findViewById(R.id.edit);

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editableText[0] = s.toString();
            }
        });

        text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Objects.requireNonNull(alert.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });


        alert.show();


    }

    private void signOut() {
        TwitterLoginActivity.mAuth.signOut();
        TwitterCore.getInstance().getSessionManager().clearActiveSession();

        Intent intent = new Intent(this, TwitterLoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.homeTimeLine:
                fragment(1, userName);
                break;
            case R.id.userTimeLine:
                fragment(2, userName);
                break;
            case R.id.message:
                Intent intent = new ComposerActivity.Builder(this)
                        .session(TwitterCore.getInstance().getSessionManager().getActiveSession())
                        .darkTheme()
                        .createIntent();
                startActivity(intent);
                break;
        }
    }

    public void fragment(int i, String userName) {
        Fragment fragment;
        switch (i) {
            case 1:
                fragment = new HomeTimelineFragment();
                transaction(fragment);
                break;
            case 2:
                fragment = UserTimelineFragment.newInstance(userName);
                transaction(fragment);
                break;
        }
    }

    private void transaction(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment, "visible_fragment");
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}
