package edu.neu.madcourse.emoji_chat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import edu.neu.madcourse.emoji_chat.R;
import edu.neu.madcourse.emoji_chat.models.Message;

public class SendMessageActivity extends AppCompatActivity implements View.OnClickListener{

    TextView receiver_name_text_view;
    Button send_message_button;

    String senderName;
    String receiverName;

    FirebaseDatabase root_node;
    DatabaseReference child_node_ref;
    DatabaseReference usersRef;

    // Stickers
    ImageView emoji_1;
    ImageView emoji_2;
    ImageView emoji_3;
    ImageView emoji_4;
    ImageView emoji_5;
    ImageView emoji_6;

    ImageView selected_emoji;

    private static final int HIGHLIGHT_COLOR = Color.argb(75, 100, 100, 200);
    private static final int NON_HIGHLIGHT_COLOR = Color.argb(0, 0, 0, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        receiver_name_text_view = findViewById(R.id.receiver_name_edit_text_view);
        receiver_name_text_view.setText("");
        send_message_button = findViewById(R.id.send_message_button);
        root_node = FirebaseDatabase.getInstance();
        usersRef = root_node.getReference("users");

        // ids of emoji image views
        emoji_1 = findViewById(R.id.imageView1);
        emoji_2 = findViewById(R.id.imageView2);
        emoji_3 = findViewById(R.id.imageView3);
        emoji_4 = findViewById(R.id.imageView4);
        emoji_5 = findViewById(R.id.imageView5);
        emoji_6 = findViewById(R.id.imageView6);

        // getting the logged in username (sender)
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            senderName = extras.getString("sender_name");
        }

        // to know which emoji the user has clicked
        setEmojiListeners();

        send_message_button.setOnClickListener(v -> {

            receiverName = receiver_name_text_view.getText().toString();
            DatabaseReference usersRef = root_node.getReference("users");

            if (receiverName.isEmpty() && !senderName.equals(receiverName)) {
                Snackbar.make(v, "Please enter a valid username to send sticker",
                        Snackbar.LENGTH_LONG).show();
            } else {
                usersRef.orderByChild("name")
                        .equalTo(receiverName)
                        .get()
                        .addOnCompleteListener((OnCompleteListener<DataSnapshot>) task -> {
                            if (!task.isSuccessful()) {
                                Log.e("firebase access unsuccessful", "Error getting data", task.getException());
                                Snackbar.make(v, "Error connecting to the database. Please try again!",
                                        Snackbar.LENGTH_LONG).show();
                            } else {
                                Log.d("firebase", String.valueOf(task.getResult().getValue()));

                                // Fetch task success, but return value == null means no such user exists
                                if(task.getResult().getValue() == null) {
                                    Snackbar.make(v, "Please enter a valid username. " +
                                                    "Entered user doesn't exists.",
                                            Snackbar.LENGTH_SHORT).show();
                                } else {
                                    if (selected_emoji != null){
                                        // Increment send message counter of sender username
                                        incrementSendMessagesCountForUser(senderName);

                                        String msg = selected_emoji.getTag().toString(); //message_text_view.getText().toString();
                                        Message message = new Message(senderName, receiverName, msg);
                                        child_node_ref = root_node.getReference("messages")
                                                .child(receiverName + "-received");
                                        DatabaseReference newRef = child_node_ref.push();
                                        newRef.setValue(message);
                                        Snackbar.make(v, "Message sent!",
                                                Snackbar.LENGTH_SHORT).show();
                                        clearSelectedData();
                                    } else{
                                        Snackbar.make(v, "Please select the emoji you want to send!",
                                                Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });

    }

    public void setEmojiListeners(){
        emoji_1.setOnClickListener(this);
        emoji_2.setOnClickListener(this);
        emoji_3.setOnClickListener(this);
        emoji_4.setOnClickListener(this);
        emoji_5.setOnClickListener(this);
        emoji_6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.imageView1:
                highlightSelectedEmoji(R.id.imageView1);
                break;
            case R.id.imageView2:
                highlightSelectedEmoji(R.id.imageView2);
                break;
            case R.id.imageView3:
                highlightSelectedEmoji(R.id.imageView3);
                break;
            case R.id.imageView4:
                highlightSelectedEmoji(R.id.imageView4);
                break;
            case R.id.imageView5:
                highlightSelectedEmoji(R.id.imageView5);
                break;
            case R.id.imageView6:
                highlightSelectedEmoji(R.id.imageView6);
                break;
        }
    }

    public void clearSelectedData(){
        if(selected_emoji != null){
            selected_emoji.setColorFilter(NON_HIGHLIGHT_COLOR);
        }
        selected_emoji = null;
        receiver_name_text_view.setText("");
    }

    public void highlightSelectedEmoji(int newSelectedEmojiId){
        if(selected_emoji != null){
            selected_emoji.setColorFilter(NON_HIGHLIGHT_COLOR);
        }
        selected_emoji = findViewById(newSelectedEmojiId);
        selected_emoji.setColorFilter(HIGHLIGHT_COLOR);

    }

    private void incrementSendMessagesCountForUser(String userName) {
        usersRef.orderByChild("name")
                .equalTo(userName)
                .get()
                .addOnCompleteListener((OnCompleteListener<DataSnapshot>) task -> {
                    if (!task.isSuccessful()) {
                        Log.e("firebase access unsuccessful", "Error getting data", task.getException());
                    } else {
                        if(task.getResult().getValue() == null) {
                            // should not be null
                            Log.e("firebase wrong result", "Null message count value. " +
                                    "Should not happen", task.getException());
                        } else {
                            System.out.println(task.getResult().getValue());
                            HashMap<String, HashMap<String, String>> map = (HashMap<String, HashMap<String, String>>) task.getResult().getValue();
                            String userId = "";
                            int count = 0;
                            for (String key: map.keySet()) {
                                userId = key;
                                count = Integer.parseInt(map.get(key).get("count"));
                            }

                            // increment current count value and put back to database
                            count++;
                            usersRef.child(userId).child("count").setValue(Integer.toString(count));
                        }
                    }
                });
    }
}