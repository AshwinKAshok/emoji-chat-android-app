package edu.neu.madcourse.emoji_chat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import edu.neu.madcourse.emoji_chat.R;
import edu.neu.madcourse.emoji_chat.models.Message;
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

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                child_node_ref.orderByChild("name")
                        .equalTo(sender_name_text_view.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase access unsuccessful", "Error getting data", task.getException());
                        }
                        else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));

                            // Fetch task success, but return value == null means no such user exists
                            if(task.getResult().getValue() == null) {
                                Snackbar mySnackbar = Snackbar.make(login_button, "User does not exists. Create a new user and try login again", Snackbar.LENGTH_SHORT);
                                mySnackbar.setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mySnackbar.dismiss();
                                    }
                                });

                                mySnackbar.show();

                            } else {
                                // Fetch task success, but return value != null means user exists. Hence login success. continue to destination activity.
                                Intent UserLandingPage = new Intent(getApplicationContext(), UserLandingPageActivity.class);
                                UserLandingPage.putExtra("sender_name", sender_name_text_view.getText().toString());
                                startActivity(UserLandingPage);
                            }

                        }
                    }
                });
            }
        });

        create_user_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Users user = new Users(sender_name_text_view.getText().toString(), "0");
                DatabaseReference newRef = child_node_ref.push();
                newRef.setValue(user);

                Snackbar mySnackbar = Snackbar.make(login_button, "User created. Try login again", Snackbar.LENGTH_SHORT);
                mySnackbar.setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mySnackbar.dismiss();
                    }
                });

                mySnackbar.show();
            }
        });
    }
}