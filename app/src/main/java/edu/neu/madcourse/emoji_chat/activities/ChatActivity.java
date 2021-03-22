package edu.neu.madcourse.emoji_chat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.neu.madcourse.emoji_chat.R;
import edu.neu.madcourse.emoji_chat.models.Message;

public class ChatActivity extends AppCompatActivity {
    // test push comment
    TextView sender_messages;
    TextView receiver_messages;
    TextView enter_text_message;
    TextView send_message_button;

    String sender_name;
    String receiver_name;

    String sender_messages_db_name;
    String receiver_messages_db_name;

    FirebaseDatabase root_node;
    DatabaseReference child_node_sender;
    DatabaseReference child_node_receiver;
    DatabaseReference child_node_sender_messages;
    DatabaseReference child_node_receiver_message;

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

            sender_messages_db_name = sender_name + "-messages";
            receiver_messages_db_name = receiver_name + "-messages";
            Log.d("child_node_sender_name: ", sender_messages_db_name );
            Log.d("child_node_receiver_name: ", receiver_messages_db_name );
        }

        root_node = FirebaseDatabase.getInstance();
        child_node_sender = root_node.getReference("users").child(sender_name);
        child_node_receiver = root_node.getReference("users").child(receiver_name);
        child_node_sender_messages = root_node.getReference("messages").child(sender_messages_db_name);
        child_node_receiver_message = root_node.getReference("messages").child(receiver_messages_db_name);



        child_node_sender_messages.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("child_node_sender: ..............", Long.toString(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        child_node_receiver_message.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("child_node_receiver: ..............", Long.toString(dataSnapshot.getChildrenCount()));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
                        Message message = new Message(sender_name, receiver_name, enter_text_message.getText().toString());
                        DatabaseReference newRef = child_node_sender_messages.push();
                        newRef.setValue(message);

                        newRef = child_node_receiver_message.push();
                        newRef.setValue(message);
                    }
                }).start();

            }
        });
    }
}