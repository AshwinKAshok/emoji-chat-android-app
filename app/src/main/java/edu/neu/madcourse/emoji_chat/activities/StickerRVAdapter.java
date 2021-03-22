package edu.neu.madcourse.emoji_chat.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.neu.madcourse.emoji_chat.R;


public class StickerRVAdapter extends RecyclerView.Adapter<StickerRVAdapter.StickerViewHolder> {

    public class StickerViewHolder extends RecyclerView.ViewHolder {

        private ImageView bodyImageView;
        private TextView senderTextView;

        public StickerViewHolder(final View stickerView) {
            super(stickerView);

            bodyImageView = (ImageView) stickerView.findViewById(R.id.image_view_sticker);
            senderTextView = (TextView) stickerView.findViewById(R.id.text_view_sender);
        }

        public void setBodyImageView(String resource) {
            try {
                int resourceID = context.getResources().getIdentifier(resource, "mipmap", context.getPackageName());
                bodyImageView.setImageResource(resourceID);
            } catch (Exception e) {
                // _resource was not a valid file/ID
            }
        }

        public void setSenderTextView(String senderText) {
            senderTextView.setText("From: " + senderText);
        }

    }

    private List<StickerView> stickers;

    private Context context;

    public StickerRVAdapter(List<StickerView> stickers, Context context) {
        this.stickers = stickers;
        this.context = context;
    }

    @Override
    public StickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.sticker_card, parent, false);

        return new StickerViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(StickerViewHolder holder, int position) {
        StickerView sticker = stickers.get(position);

        holder.setBodyImageView(sticker.getImgSrc());
        holder.setSenderTextView(sticker.getSender());
    }

    @Override
    public int getItemCount() {
        return stickers.size();
    }
}
