package edu.neu.madcourse.emoji_chat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.neu.madcourse.emoji_chat.R;

public class UserLandingPageActivity extends AppCompatActivity {

    TextView messages_count_text_view;
    Button start_send_messages_activity_button;
    Button start_all_messages_activty_button;

    String sender_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_landing_page);

        messages_count_text_view = findViewById(R.id.messages_count_text_view);
        start_send_messages_activity_button = findViewById(R.id.start_send_message_activity_button);
        start_all_messages_activty_button = findViewById(R.id.start_show_all_messages_activity_button);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            sender_name = extras.getString("sender_name");
        }

        start_send_messages_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sent_message_activity = new Intent(getApplicationContext(), SendMessageActivity.class);
                sent_message_activity.putExtra("sender_name", sender_name);
                startActivity(sent_message_activity);
            }
        });

        start_all_messages_activty_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent all_messages_activity = new Intent(getApplicationContext(), AllMessagesActivity.class);
                all_messages_activity.putExtra("curr_user_name", sender_name);
                startActivity(all_messages_activity);
            }
        });
    };
}