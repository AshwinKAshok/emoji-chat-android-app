package edu.neu.madcourse.emoji_chat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.emoji_chat.R;
import edu.neu.madcourse.emoji_chat.models.Message;

public class SendMessageActivity extends AppCompatActivity implements View.OnClickListener{

    TextView receiver_name_text_view;
    // TextView message_text_view;
    Button send_message_button;

    String sender_name;
    String receiver_name;

    FirebaseDatabase root_node;
    DatabaseReference child_node_ref;

    // Stickers
    ImageView emoji_1;
    ImageView emoji_2;
    ImageView emoji_3;
    ImageView emoji_4;
    ImageView emoji_5;
    ImageView emoji_6;

    ImageView selected_emoji;

    private static final int HIGHLIGHT_COLOR = Color.argb(50, 100, 100, 100);
    private static final int NON_HIGHLIGHT_COLOR = Color.argb(0, 0, 0, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        receiver_name_text_view = findViewById(R.id.receiver_name_edit_text_view);
        receiver_name_text_view.setText("");
        // message_text_view = findViewById(R.id.receiver_message_edit_text_view);
        send_message_button = findViewById(R.id.send_message_button);

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
            sender_name = extras.getString("sender_name");
        }

        // to know which emoji the user has clicked
        setEmojiListeners();

        send_message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiver_name = receiver_name_text_view.getText().toString();
                String msg = "Hardcoded Test message"; //message_text_view.getText().toString();

                root_node = FirebaseDatabase.getInstance();
                child_node_ref = root_node.getReference("messages").child(receiver_name + "-received");

                Message message = new Message(sender_name, receiver_name, msg);
                DatabaseReference newRef = child_node_ref.push();
                newRef.setValue(message);
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
                receiver_name_text_view.setText("Emoji 1");
                highlightSelectedEmoji(R.id.imageView1);
                break;
            case R.id.imageView2:
                receiver_name_text_view.setText("Emoji 2");
                highlightSelectedEmoji(R.id.imageView2);
                break;
            case R.id.imageView3:
                receiver_name_text_view.setText("Emoji 3");
                highlightSelectedEmoji(R.id.imageView3);
                break;
            case R.id.imageView4:
                receiver_name_text_view.setText("Emoji 4");
                highlightSelectedEmoji(R.id.imageView4);
                break;
            case R.id.imageView5:
                receiver_name_text_view.setText("Emoji 5");
                highlightSelectedEmoji(R.id.imageView5);
                break;
            case R.id.imageView6:
                receiver_name_text_view.setText("Emoji 6");
                highlightSelectedEmoji(R.id.imageView6);
                break;
        }
    }

    public void highlightSelectedEmoji(int newSelectedEmojiId){
        if(selected_emoji != null){
            selected_emoji.setColorFilter(NON_HIGHLIGHT_COLOR);
        }
        selected_emoji = findViewById(newSelectedEmojiId);
        selected_emoji.setColorFilter(HIGHLIGHT_COLOR);
    }
}