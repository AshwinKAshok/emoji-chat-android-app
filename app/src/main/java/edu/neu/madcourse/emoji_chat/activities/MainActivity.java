package edu.neu.madcourse.emoji_chat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.neu.madcourse.emoji_chat.R;

public class MainActivity extends AppCompatActivity {

    TextView sender_name_text_view;
    Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // test commit
        sender_name_text_view = findViewById(R.id.sender_name_edit_text_view);
        login_button = findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent UserLandingPage = new Intent(getApplicationContext(), UserLandingPageActivity.class);

                // TODO: check if these sender/receiver names exist in DB
                // TODO: if names do not exist create prompt to check name or create new user
                // TODO: Validate sender/receiver names for null values
                UserLandingPage.putExtra("sender_name", sender_name_text_view.getText().toString());
                startActivity(UserLandingPage);
            }
        });
    }
}