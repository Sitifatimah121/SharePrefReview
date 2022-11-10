package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvName, tvEmail, tvAge, tvPhone, tvIsLove;
    private Button btnSave;

    private UserPreferences mUserPreferences;
    private UserModel userModel;
    private boolean isPreferenceEmpty = false;

    final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
        if (result.getData() != null && result.getResultCode() == FormUserPreferenceActivity.EXTRA_CODE){
            userModel = result.getData().getParcelableExtra(FormUserPreferenceActivity.EXTRA_RESULT);
            populateView(userModel);
            checkForm(userModel);
        }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        tvAge = findViewById(R.id.tv_age);
        tvPhone = findViewById(R.id.tv_phone);
        tvIsLove = findViewById(R.id.tv_is_love_mu);
        btnSave = findViewById(R.id.btn_save);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("My User Preference");
        }

        mUserPreferences = new UserPreferences(this);
        showExitingPreference();

        btnSave.setOnClickListener(this);

    }

    private void showExitingPreference() {
        userModel = mUserPreferences.getUser();
        populateView(userModel);
        checkForm(userModel);
    }

    private void populateView(UserModel userModel) {
        tvName.setText(userModel.getName().isEmpty() ? "Tidak ada" : userModel.getName());
        tvAge.setText(String.valueOf(userModel.getAge()).isEmpty() ? "Tidak ada" : String.valueOf(userModel.getAge()));
        tvIsLove.setText(userModel.isLove() ? "Ya" : "Tidak");
        tvEmail.setText(userModel.getEmail().isEmpty() ? "Tidak ada" : userModel.getEmail());
        tvPhone.setText(userModel.getPhoneNumber().isEmpty() ? "Tidak ada" : userModel.getPhoneNumber());
    }

    private void checkForm(UserModel userModel) {
        if (!userModel.getName().isEmpty()){
            btnSave.setText(getString(R.string.change));
            isPreferenceEmpty = false;
        } else {
            btnSave.setText(getString(R.string.save));
            isPreferenceEmpty = true;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save){
            Intent intent = new Intent(MainActivity.this, FormUserPreferenceActivity.class);
            if (isPreferenceEmpty){
                intent.putExtra(FormUserPreferenceActivity.EXTRA_TYPE_FORM, FormUserPreferenceActivity.TYPE_ADD);
            } else {
                intent.putExtra(FormUserPreferenceActivity.EXTRA_TYPE_FORM, FormUserPreferenceActivity.TYPE_UPDATE);
            }
            intent.putExtra("USER", userModel);
            resultLauncher.launch(intent);
        }
    }
}