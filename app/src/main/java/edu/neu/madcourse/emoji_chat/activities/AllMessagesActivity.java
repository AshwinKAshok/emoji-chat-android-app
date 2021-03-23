package edu.neu.madcourse.emoji_chat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.emoji_chat.R;

public class AllMessagesActivity extends AppCompatActivity {
    List<StickerView> stickers;

    FirebaseDatabase root_node;
    DatabaseReference child_node_ref;

    String curr_user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_messages);

        stickers = new ArrayList<>();

        final RecyclerView stickersRV = findViewById(R.id.recyclerView);
        final StickerRVAdapter stickersAdapter = new StickerRVAdapter(stickers, this);

        stickersRV.setAdapter(stickersAdapter);
        stickersRV.setLayoutManager(new LinearLayoutManager(this));

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            curr_user_name = extras.getString("curr_user_name");
        }

        root_node = FirebaseDatabase.getInstance();
        child_node_ref = root_node.getReference("messages").child(curr_user_name + "-received");

        child_node_ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        handleStickers(snapshot);
                        stickersAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle Database error here
                    }
                }
        );


    }

    private void handleStickers(DataSnapshot _snapshot) {
        stickers.clear();
        for (DataSnapshot sticker : _snapshot.getChildren()) {
            StickerView newSticker = new StickerView(
                    sticker.child("img_src").getValue().toString(),
                    sticker.child("sender").getValue().toString()
            );

            stickers.add(0,newSticker);
        }

    }


}