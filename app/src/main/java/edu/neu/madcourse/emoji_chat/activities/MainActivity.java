package edu.neu.madcourse.emoji_chat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;

import edu.neu.madcourse.emoji_chat.R;
import edu.neu.madcourse.emoji_chat.models.Users;
import edu.neu.madcourse.emoji_chat.service.StickerMessagingService;

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
//                                    StickerMessagingService.userLoggedIn(senderName);

                                    HashMap<String, HashMap<String, String>> map = (HashMap<String, HashMap<String, String>>) task.getResult().getValue();
                                    String userId = "";
                                    int count = 0;
                                    for (String key: map.keySet()) {
                                        userId = key;
//                                        count = Integer.parseInt(map.get(key).get("count"));
                                    }


                                    String finalUserId = userId;
                                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                        @Override
                                        public void onSuccess(InstanceIdResult instanceIdResult) {
                                            String token = instanceIdResult.getToken();
                                            child_node_ref.child(finalUserId).child("count").setValue(token);
                                        }
                                    });

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

    public static void userLoggedIn(String userName) {

    }
}