package edu.neu.madcourse.emoji_chat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.neu.madcourse.emoji_chat.R;

public class ChatActivity extends AppCompatActivity {
    TextView sender_messages;
    TextView receiver_messages;
    TextView enter_text_message;
    TextView send_message_button;

    String sender_name;
    String receiver_name;

    FirebaseDatabase root_node;
    DatabaseReference child_node_sender;
    DatabaseReference child_node_receiver;

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

        root_node = FirebaseDatabase.getInstance();
        child_node_sender = root_node.getReference("users").child(sender_name);
        child_node_receiver = root_node.getReference("users").child(receiver_name);

        child_node_sender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                sender_messages.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        child_node_receiver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                receiver_messages.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        send_message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        child_node_receiver.setValue(enter_text_message.getText().toString());
                    }
                }).start();

            }
        });
    }
}