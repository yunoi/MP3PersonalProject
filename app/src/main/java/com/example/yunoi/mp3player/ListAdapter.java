package com.example.yunoi.mp3player;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomViewHolder> {

    private int layout;
    private ArrayList<MainData> list;
    private int currentPosition;
    private int lastCheckedPosition = -1;
    private musicListSelectListener listener;

    public ListAdapter(int layout, ArrayList<MainData> list) {
        this.layout = layout;
        this.list = list;
    }

    // 라디오버튼 리스너 인터페이스
    public interface musicListSelectListener {
        void onMusicClick(View v, int position);
    }

    public void onMusicListClick(musicListSelectListener listener) {
        this.listener = listener;
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
    public void onBindViewHolder(@NonNull final CustomViewHolder customViewHolder, final int position) {
        currentPosition = position;
//        customViewHolder.tvSinger.setText(list.get(currentPosition).getSinger());
        customViewHolder.tvTitle.setText(list.get(currentPosition).getTitle());
        customViewHolder.itemView.setTag(customViewHolder);
        customViewHolder.radioButton.setTag(customViewHolder);
//        if(customViewHolder.radioButton.isChecked()){
//            currentPosition = customViewHolder.getAdapterPosition();
//        }
        customViewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMusicClick(v, currentPosition);
                }
            }
        });
        customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMusicClick(v, currentPosition);
                }
                customViewHolder.radioButton.setChecked(true);
                list.get(position).setIschecked(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list != null) ? list.size() : 0;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvSinger, tvTitle;
        RadioButton radioButton;
        ImageView imageView;

        public TextView getTvSinger() {
            return tvSinger;
        }

        public void setTvSinger(TextView tvSinger) {
            this.tvSinger = tvSinger;
        }

        public TextView getTvTitle() {
            return tvTitle;
        }

        public void setTvTitle(TextView tvTitle) {
            this.tvTitle = tvTitle;
        }

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
