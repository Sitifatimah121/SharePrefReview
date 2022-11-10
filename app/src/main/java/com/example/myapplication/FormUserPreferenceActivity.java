package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class FormUserPreferenceActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtName, edtEmail, edtAge, edtPhone;
    private RadioGroup rbLove;
    private RadioButton rbYes, rbNo;

    private static final String FIELD_REQUIRED = "Field ini Tidak boleh kosong";
    private static final String FIELD_NUMBER = "Field ini harus numerik";
    private static final String FILED_IS_NOT_VALID = "Field ini harus email";

    public static final String EXTRA_TYPE_FORM = "extra_type_form";
    public static final String EXTRA_RESULT = "extra_result";
    public static final int EXTRA_CODE = 101;

    public static final int TYPE_ADD = 1;
    public static final int TYPE_UPDATE = 2;

    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_user_preference);

        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtAge = findViewById(R.id.edt_age);
        edtPhone = findViewById(R.id.edt_phone);
        rbLove = findViewById(R.id.rg_love_mu);
        rbYes = findViewById(R.id.rb_yes);
        rbNo = findViewById(R.id.rb_no);

        Button btnSave = findViewById(R.id.btn_save);

        btnSave.setOnClickListener(this);

        //Mengambil Data Dari Intent
        userModel = getIntent().getParcelableExtra("USER");
        int formType = getIntent().getIntExtra(EXTRA_TYPE_FORM, 0);

        String actionBarTitle = "";
        String btnTitle = "";

        switch (formType){
            case TYPE_ADD:
                actionBarTitle = "Tambah Baru";
                btnTitle ="Simpan";
                break;
            case TYPE_UPDATE:
                actionBarTitle = "Ubah";
                btnTitle = "Update";
                showPreferenceInForm();
        }

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnSave.setText(btnTitle);


    }

    // mengubah data dari intent ke viewnya
    private void showPreferenceInForm() {
        edtName.setText(userModel.getName());
        edtEmail.setText(userModel.getEmail());
        edtAge.setText(String.valueOf(userModel.getAge()));
        edtPhone.setText(userModel.getPhoneNumber());

        if (userModel.isLove()){
            rbYes.setChecked(true);
        } else {
            rbNo.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save){
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String age = edtAge.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            boolean isLove = rbLove.getCheckedRadioButtonId() == R.id.rb_yes;

            if (TextUtils.isEmpty(name)){
                edtName.setError(FIELD_REQUIRED);
                return;
            }

            if (TextUtils.isEmpty(email)){
                edtEmail.setError(FIELD_REQUIRED);
                return;
            }

            if (!isValidEmail(email)){
                edtEmail.setError(FILED_IS_NOT_VALID);
                return;
            }

            if (TextUtils.isEmpty(age)){
                edtAge.setError(FIELD_REQUIRED);
                return;
            }

            if (TextUtils.isEmpty(phone)){
                edtPhone.setError(FIELD_REQUIRED);
                return;
            }

            if (!TextUtils.isDigitsOnly(phone)){
                edtPhone.setError(FIELD_NUMBER);
                return;
            }

            saveUser(name, email, age, phone, isLove);

            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_RESULT, userModel);
            setResult(EXTRA_CODE, resultIntent);

            finish();
        }
    }

    private void saveUser(String name, String email, String age, String phone, boolean isLove){
        UserPreferences userPreferences = new UserPreferences(this);

        userModel.setName(name);
        userModel.setEmail(email);
        userModel.setAge(Integer.parseInt(age));
        userModel.setPhoneNumber(phone);
        userModel.setLove(isLove);

        userPreferences.setUser(userModel);
        Toast.makeText(this,"Data Tersimpan", Toast.LENGTH_SHORT).show();
    }

    private boolean isValidEmail(CharSequence email){
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Agar form kembali ke MainActivity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}