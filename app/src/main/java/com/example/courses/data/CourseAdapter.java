package com.example.courses.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courses.R;
import com.example.courses.data.model.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> implements Filterable {

    public interface OnItemClickListener {
        void onItemClick(Course course);
    }

    private List<Course> courseList;
    private List<Course> courseListFiltered;
    private Context context;
    private OnItemClickListener listener;

    public CourseAdapter(List<Course> courseList, Context context, OnItemClickListener listener) {
        this.courseList = courseList;
        this.courseListFiltered = courseList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseListFiltered.get(position);

        holder.title.setText(course.getTitle());
        holder.price.setText(String.format("$%.2f", course.getPrice()));
    }

    @Override
    public int getItemCount() {
        return courseListFiltered.size();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView title, price;

        public CourseViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitle);
            price = itemView.findViewById(R.id.textViewPrice);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(courseListFiltered.get(position));
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString().toLowerCase();

                if (charString.isEmpty()) {
                    courseListFiltered = courseList;
                } else {
                    List<Course> filteredList = new ArrayList<>();
                    for (Course row : courseList) {
                        if (row.getTitle().toLowerCase().contains(charString) ||
                                row.getDescription().toLowerCase().contains(charString)) {
                            filteredList.add(row);
                        }
                    }
                    courseListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = courseListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                courseListFiltered = (List<Course>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
