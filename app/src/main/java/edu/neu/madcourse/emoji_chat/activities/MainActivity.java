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
    TextView receiver_name_text_view;
    Button start_chat_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sender_name_text_view = findViewById(R.id.sender_name_edit_text_view);
        receiver_name_text_view = findViewById(R.id.receiver_name_edit_text_view);
        start_chat_button = findViewById(R.id.start_chat_button);

        start_chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start_chat = new Intent(getApplicationContext(), ChatActivity.class);

                // TODO: check if these sender/receiver names exist in DB
                // TODO: if names do not exist create prompt to check name or create new user
                // TODO: Validate sender/receiver names for null values
                start_chat.putExtra("sender_name", sender_name_text_view.getText().toString());
                start_chat.putExtra("receiver_name", receiver_name_text_view.getText().toString());
                startActivity(start_chat);
            }
        });
    }
}