package edu.neu.madcourse.emoji_chat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import edu.neu.madcourse.emoji_chat.R;

public class ChatActivity extends AppCompatActivity {
    TextView sender_messages;
    TextView receiver_messages;
    TextView enter_text_message;
    TextView send_message_button;

    String sender_name;
    String receiver_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sender_messages = findViewById(R.id.sender_text_view);
        receiver_messages = findViewById(R.id.receiver_text_view);
        enter_text_message = findViewById(R.id.enter_text_message_edit_text_view);
        send_message_button = findViewById(R.id.send_text_button);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            sender_name = extras.getString("sender_name");
            receiver_name = extras.getString("receiver_name");
        }



    }
}