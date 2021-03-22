package edu.neu.madcourse.emoji_chat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.emoji_chat.R;
import edu.neu.madcourse.emoji_chat.models.Users;

public class MainActivity extends AppCompatActivity {

    TextView sender_name_text_view;
    Button login_button;
    Button create_user_button;

    FirebaseDatabase root_node;
    DatabaseReference child_node_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // test commit
        sender_name_text_view = findViewById(R.id.sender_name_edit_text_view);
        login_button = findViewById(R.id.login_button);
        create_user_button = findViewById(R.id.create_user_button);

        root_node = FirebaseDatabase.getInstance();
        child_node_ref = root_node.getReference("users");

        login_button.setOnClickListener(v -> {
            String senderName = sender_name_text_view.getText().toString();
            if (senderName.isEmpty()) {
                Snackbar.make(v, "Please enter a valid username",
                        Snackbar.LENGTH_LONG).show();
            } else {
                child_node_ref.orderByChild("name")
                        .equalTo(senderName)
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
                                    Snackbar.make(v, "User does not exists. Create a new user " +
                                                    "and try login again", Snackbar.LENGTH_SHORT)
                                            .show();
                                } else {
                                    // Fetch task success, but return value != null means user exists. Hence login success. continue to destination activity.
                                    Intent UserLandingPage = new Intent(getApplicationContext(),
                                            UserLandingPageActivity.class);
                                    UserLandingPage.putExtra("sender_name", senderName);
                                    startActivity(UserLandingPage);
                                }
                            }
                        });
            }
        });

        create_user_button.setOnClickListener(v -> {
            String senderName = sender_name_text_view.getText().toString();

            if (senderName.isEmpty()) {
                Snackbar.make(v, "Please enter a valid username",
                        Snackbar.LENGTH_LONG).show();
            } else {
                child_node_ref.orderByChild("name")
                        .equalTo(senderName)
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
                                    Users user = new Users(senderName, "0");
                                    DatabaseReference newRef = child_node_ref.push();
                                    newRef.setValue(user);

                                    Snackbar.make(v, "User created. Try login again",
                                            Snackbar.LENGTH_SHORT).show();
                                } else {
                                    // User already exists - tell to login instead of creating new
                                    Snackbar.make(v, "User already exists. You can just login now!",
                                            Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}