package edu.neu.madcourse.emoji_chat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import edu.neu.madcourse.emoji_chat.R;
import edu.neu.madcourse.emoji_chat.models.Users;

public class UserLandingPageActivity extends AppCompatActivity {

    TextView messages_count_text_view;
    Button start_send_messages_activity_button;
    Button start_all_messages_activity_button;

    String sender_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_landing_page);

        messages_count_text_view = findViewById(R.id.messages_count_text_view);
        start_send_messages_activity_button = findViewById(R.id.start_send_message_activity_button);
        start_all_messages_activity_button = findViewById(R.id.start_show_all_messages_activity_button);
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            sender_name = extras.getString("sender_name");
        }

        String message = "You have sent ... stickers in total!";
        messages_count_text_view.setText(message);
        setSendMessagesCountForUser(sender_name);

        start_send_messages_activity_button.setOnClickListener(v -> {
            Intent sent_message_activity = new Intent(getApplicationContext(), SendMessageActivity.class);
            sent_message_activity.putExtra("sender_name", sender_name);
            startActivity(sent_message_activity);
        });

        start_all_messages_activity_button.setOnClickListener(v -> {
            Intent all_messages_activity = new Intent(getApplicationContext(), AllMessagesActivity.class);
            all_messages_activity.putExtra("curr_user_name", sender_name);
            startActivity(all_messages_activity);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String message = "You have sent ... stickers in total";
        messages_count_text_view.setText(message);
        setSendMessagesCountForUser(sender_name);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String message = "You have sent ... stickers in total";
        messages_count_text_view.setText(message);
        setSendMessagesCountForUser(sender_name);
    }

    private void setSendMessagesCountForUser(String userName) {
        FirebaseDatabase root_node = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = root_node.getReference("users");

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
                            HashMap<Object, HashMap<String, String>> map = (HashMap<Object, HashMap<String, String>>) task.getResult().getValue();
                            for (HashMap<String, String> user: map.values()) {
                                int x = Integer.parseInt(user.get("count"));
                                String message = "You have sent " + x + " stickers in total";
                                messages_count_text_view.setText(message);
                            }
                        }
                    }
                });
    }
}