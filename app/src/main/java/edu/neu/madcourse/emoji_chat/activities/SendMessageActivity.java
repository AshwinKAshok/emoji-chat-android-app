package edu.neu.madcourse.emoji_chat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.emoji_chat.R;
import edu.neu.madcourse.emoji_chat.models.Message;

public class SendMessageActivity extends AppCompatActivity {

    TextView receiver_name_text_view;
    TextView message_text_view;
    Button send_message_button;

    String sender_name;
    String receiver_name;

    FirebaseDatabase root_node;
    DatabaseReference child_node_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        receiver_name_text_view = findViewById(R.id.receiver_name_edit_text_view);
        message_text_view = findViewById(R.id.receiver_message_edit_text_view);
        send_message_button = findViewById(R.id.send_message_button);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            sender_name = extras.getString("sender_name");
        }

        send_message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiver_name = receiver_name_text_view.getText().toString();
                String msg = message_text_view.getText().toString();

                root_node = FirebaseDatabase.getInstance();
                child_node_ref = root_node.getReference("messages").child(receiver_name + "-received");

                Message message = new Message(sender_name, receiver_name, msg);
                DatabaseReference newRef = child_node_ref.push();
                newRef.setValue(message);
            }
        });

    }
}