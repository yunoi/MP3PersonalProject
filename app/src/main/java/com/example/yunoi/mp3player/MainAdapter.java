package com.example.yunoi.mp3player;

import android.database.sqlite.SQLiteDatabase;
import android.media.Rating;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> {

    private int layout;
    private ArrayList<MainData> list;
    private  int currentPosition;
    private int lastCheckedPosition = -1;
    private RadioButton selectedRadioButton;
    private musicSelectListener listener;


    public MainAdapter(int layout, ArrayList<MainData> list) {
        this.layout = layout;
        this.list = list;
    }

    // 라디오버튼 리스너 인터페이스
    public interface musicSelectListener {
        void onMusicClick(View v, int position);
    }

    public void onMusicClick(musicSelectListener listener) {
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
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, final int position) {
        currentPosition = position;
        customViewHolder.itemView.setTag(customViewHolder);
        customViewHolder.radioButton.setTag(customViewHolder);
        customViewHolder.tvSinger.setText(list.get(currentPosition).getSinger());
        customViewHolder.tvTitle.setText(list.get(currentPosition).getTitle());
        customViewHolder.tvJanre.setText(list.get(currentPosition).getJanre());
        customViewHolder.ratingBar.setRating(list.get(currentPosition).getRate());
        customViewHolder.radioButton.setChecked(lastCheckedPosition == position);

        customViewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMusicClick(v, position);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (list != null) ? list.size() : 0;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvSinger, tvTitle, tvJanre;
        RatingBar ratingBar;
        RadioButton radioButton;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSinger = itemView.findViewById(R.id.tvSinger);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvJanre = itemView.findViewById(R.id.tvJanre);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            radioButton = itemView.findViewById(R.id.radioButton);

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
