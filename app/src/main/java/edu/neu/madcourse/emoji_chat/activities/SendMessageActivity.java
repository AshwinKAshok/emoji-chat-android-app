package edu.neu.madcourse.emoji_chat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.emoji_chat.R;
import edu.neu.madcourse.emoji_chat.models.Message;

public class SendMessageActivity extends AppCompatActivity {

    TextView receiver_name_text_view;
    // TextView message_text_view;
    Button send_message_button;

    String senderName;
    String receiverName;

    FirebaseDatabase root_node;
    DatabaseReference child_node_ref;

    // Stickers
    ImageView emoji_1;
    ImageView emoji_2;
    ImageView emoji_3;
    ImageView emoji_4;
    ImageView emoji_5;
    ImageView emoji_6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        receiver_name_text_view = findViewById(R.id.receiver_name_edit_text_view);
        receiver_name_text_view.setText("");
        send_message_button = findViewById(R.id.send_message_button);
        root_node = FirebaseDatabase.getInstance();

        // ids for image views
        emoji_1 = findViewById(R.id.imageView1);
        emoji_2 = findViewById(R.id.imageView2);
        emoji_3 = findViewById(R.id.imageView3);
        emoji_4 = findViewById(R.id.imageView4);
        emoji_5 = findViewById(R.id.imageView5);
        emoji_6 = findViewById(R.id.imageView6);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            senderName = extras.getString("sender_name");
        }

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
                                Snackbar.make(v, "Please try again!",
                                        Snackbar.LENGTH_LONG).show();
                            } else {
                                Log.d("firebase", String.valueOf(task.getResult().getValue()));

                                // Fetch task success, but return value == null means no such user exists
                                if(task.getResult().getValue() == null) {
                                    Snackbar.make(v, "Please enter a valid username. " +
                                                    "Entered user doesn't exists.",
                                            Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String msg = "Hardcoded Test message"; //message_text_view.getText().toString();
                                    Message message = new Message(senderName, receiverName, msg);
                                    child_node_ref = root_node.getReference("messages")
                                            .child(receiverName + "-received");
                                    DatabaseReference newRef = child_node_ref.push();
                                    newRef.setValue(message);
                                }
                            }
                        });
            }
        });

    }
}