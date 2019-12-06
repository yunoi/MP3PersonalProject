package com.example.yunoi.mp3player;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomViewHolder>{

    private int layout;
    private ArrayList<MainData> list;
    private  int currentPosition;
    private int lastCheckedPosition = -1;

    public ListAdapter(int layout, ArrayList<MainData> list) {
        this.layout = layout;
        this.list = list;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getLastCheckedPosition() {
        return lastCheckedPosition;
    }

    @NonNull
    @Override
    public ListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        ListAdapter.CustomViewHolder holder = new ListAdapter.CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, final int position) {
        currentPosition = position;
//        customViewHolder.tvSinger.setText(list.get(currentPosition).getSinger());
        customViewHolder.tvTitle.setText(list.get(currentPosition).getTitle());
        customViewHolder.radioButton.setChecked(lastCheckedPosition == position);
        customViewHolder.itemView.setTag(currentPosition);
    }

    @Override
    public int getItemCount() {
        return (list != null) ? list.size() : 0;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvSinger, tvTitle;
        RadioButton radioButton;
        ImageView imageView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSinger = itemView.findViewById(R.id.tvSinger);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            radioButton = itemView.findViewById(R.id.radioButton);
            imageView = itemView.findViewById(R.id.imageView);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition = getAdapterPosition();

                    notifyDataSetChanged();
                }
            });
        }

    }
}
