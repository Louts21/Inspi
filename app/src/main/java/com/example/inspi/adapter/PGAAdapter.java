package com.example.inspi.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inspi.R;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PGAAdapter extends RecyclerView.Adapter<PGAAdapter.PictureViewHolder> {

    private Map<String, String> pictureMap;

    public PGAAdapter(Map<String, String> map) {
        pictureMap = map;
    }

    @NonNull
    @Override
    public PictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_picture_gallery, parent, false);
        return new PictureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureViewHolder holder, int position) {
        int counter1 = 0;
        for (String fileName: pictureMap.values()) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(fileName));
                holder.picture.setImageBitmap(bitmap);
                counter1++;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return pictureMap.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PictureViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView pictureTitle;
        ImageView picture;

        public PictureViewHolder(@NonNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            pictureTitle = itemView.findViewById(R.id.pictureTitle);
            picture = itemView.findViewById(R.id.photo);
        }
    }
}
