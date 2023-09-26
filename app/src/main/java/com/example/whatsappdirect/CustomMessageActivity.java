package com.example.whatsappdirect;


        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.preference.PreferenceManager;
        import android.view.View;
        import android.widget.Toast;

        import com.google.android.material.chip.Chip;
        import com.google.android.material.textfield.TextInputEditText;

public class CustomMessageActivity extends AppCompatActivity {
    TextInputEditText MyEditText;
    Chip saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_message);

        init();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultMsg = sharedPreferences.getString("default_message", "Default message");

        // Set the default message in the input text box
        MyEditText.setText(defaultMsg);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = MyEditText.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("default_message", message);
                editor.apply();
                Toast.makeText(getApplicationContext(), "Message saved", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
    public void init(){

        saveButton = (Chip) findViewById(R.id.SaveChip);
        MyEditText = (TextInputEditText) findViewById(R.id.defalut);
    }

}

