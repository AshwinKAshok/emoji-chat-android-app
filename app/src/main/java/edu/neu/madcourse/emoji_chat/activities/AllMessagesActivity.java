package edu.neu.madcourse.emoji_chat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.neu.madcourse.emoji_chat.R;
import edu.neu.madcourse.emoji_chat.models.Message;

public class AllMessagesActivity extends AppCompatActivity {
    TextView all_messages_text_view;

    FirebaseDatabase root_node;
    DatabaseReference child_node_ref;

    String curr_user_name;

    String all_messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_messages);

        all_messages_text_view = findViewById(R.id.all_messages_text_view);
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            curr_user_name = extras.getString("curr_user_name");
        }

        root_node = FirebaseDatabase.getInstance();
        child_node_ref = root_node.getReference("messages").child(curr_user_name + "-received");

        all_messages = "";

        child_node_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message msg = dataSnapshot.getValue(Message.class);
                all_messages += msg.getImg_src();
                all_messages += "\n";

                all_messages_text_view.setText(all_messages);
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
    }


}