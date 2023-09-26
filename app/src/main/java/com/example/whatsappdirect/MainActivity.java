package com.example.whatsappdirect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.whatsappdirect.CustomMessageActivity;
import com.example.whatsappdirect.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
//import com.hbb20.CountryCodePicker
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {


    LinearLayout linearLayout;

    TextInputEditText te1, custommsg, mobileNumber, defaultmsg;
    RadioButton b1, b2;
    Chip c1;
    RadioGroup radioGroup;
  //  CountryCodePicker countryCodePicker;
    TextInputEditText multiPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultMsg = sharedPreferences.getString("default_message", "Default message");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custommsg.setVisibility(View.GONE);

            }
        });

        // Add OnClickListener to btn2 to show custommsg view
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custommsg.setVisibility(View.VISIBLE);
            }
        });


        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // CountryCodePicker codePicker = (CountryCodePicker) findViewById(R.id.countryCodePicker);
               // String countryCode = codePicker.getSelectedCountryCode();

                int selectedId = radioGroup.getCheckedRadioButtonId();
                String message;
                if (selectedId == R.id.radio_button_1) {
                    message = defaultMsg;
                } else {
                    message = custommsg.getText().toString();
                }
                String phoneNumber = mobileNumber.getText().toString();
                String multiNumber = multiPhoneNumber.getText().toString();

                if (!phoneNumber.isEmpty()) {
                    if (phoneNumber.matches("^[1-9]\\d{9}$")) {
                        sendmsg("+91", phoneNumber, message);
                    }
                } else if (!multiNumber.isEmpty()) {
                    sendMultipleMsgs("91", multiNumber, message);

                }


            }
        });

    }

    public void init() {

        c1 = (Chip) findViewById(R.id.Sendchip);

        mobileNumber = (TextInputEditText) findViewById(R.id.phoneNumberEditText);
      //  countryCodePicker = (CountryCodePicker) findViewById(R.id.countryCodePicker);
       // countryCodePicker.setDefaultCountryUsingNameCode("IN");
        multiPhoneNumber = findViewById(R.id.multipleNumber);
        custommsg = (TextInputEditText) findViewById(R.id.custommsg);
        defaultmsg = (TextInputEditText) findViewById(R.id.defalut);
        b1 = (RadioButton) findViewById(R.id.radio_button_1);
        b2 = (RadioButton) findViewById(R.id.radio_button_2);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.check(R.id.radio_button_1);
        radioGroup.setOnCheckedChangeListener(new radiobutton());

    }

    public final void sendmsg(String countryCode, String phoneNumber, String message) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            String phone = countryCode + phoneNumber;
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + phone + "&text=" + message));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMultipleMsgs(String countryCode, String multiNumber, String str2) {
        String phoneNumber = countryCode + multiNumber;
        String[] numbers = phoneNumber.split(",");
        for (String number : numbers) {
            try {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + number + "&text=" + str2));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class radiobutton implements RadioGroup.OnCheckedChangeListener {
        public radiobutton() {
        }

        @Override // android.widget.RadioGroup.OnCheckedChangeListener
        public final void onCheckedChanged(RadioGroup radioGroup, int i2) {
            if (i2 == R.id.custommsg) {
                defaultmsg.setVisibility(View.GONE);
                custommsg.setVisibility(View.VISIBLE);
            } else if (i2 == R.id.defalut) {
                defaultmsg.setVisibility(View.VISIBLE);
                custommsg.setVisibility(View.GONE);

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem item) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.setPackage("com.whatsapp");
                        i.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.shareText));
                        startActivity(Intent.createChooser(i, "Suggest your friend"));

                        return true;
                    }
                });
                break;
            case R.id.setting:
                Intent i = new Intent(getApplicationContext(), CustomMessageActivity.class);
                startActivity(i);
                break;
            case R.id.exit:
                Exit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Exit() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Confirm Exit");
        dialog.setIcon(R.drawable.baseline_share_24);
        dialog.setMessage("Are you sure you want to exit?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "WelCome Back", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "No Action", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

}



