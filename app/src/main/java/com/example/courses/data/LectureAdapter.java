package com.example.courses.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courses.data.model.Lecture;
import com.example.courses.R;

import java.util.List;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.ViewHolder> {

    private List<Lecture> lectures;
    private Context context;

    public LectureAdapter(Context context, List<Lecture> lectures) {
        this.context = context;
        this.lectures = lectures;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lecture lecture = lectures.get(position);

        holder.tvLectureNumber.setText(String.format("%02d", position + 1));

        holder.tvLectureTitle.setText(lecture.getTitle());
    }

    @Override
    public int getItemCount() {
        return lectures.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLectureNumber;
        TextView tvLectureTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLectureNumber = itemView.findViewById(R.id.tv_lecture_number);
            tvLectureTitle = itemView.findViewById(R.id.tv_lecture_title);
        }
    }
}