package com.example.courses.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courses.R;
import com.example.courses.data.LectureAdapter;
import com.example.courses.data.model.Course;
import com.example.courses.data.model.Lecture;
import com.example.courses.data.source.SupabaseClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CourseDetailsActivity extends AppCompatActivity {

    SupabaseClient s = new SupabaseClient();
    List<Lecture> lectures;
    RecyclerView rv;
    LectureAdapter adapter;
    ImageButton btn;
    TextView title, price, desc;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        btn = findViewById(R.id.back_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lectures = new ArrayList<>();
        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        desc = findViewById(R.id.desc);
        Course c = getIntent().getSerializableExtra("course", Course.class);
        title.setText(c.getTitle());
        price.setText(String.format("$%.2f", c.getPrice()));
        desc.setText(c.getDescription());

        rv = findViewById(R.id.lectRv);
        getLectures(c.getId());
    }

    public void getLectures(String id){
        s.getLectures(getApplicationContext(), id, new SupabaseClient.supa_callback() {
            @Override
            public void onFailure(IOException e) {
                getLectures(id);
            }

            @Override
            public void onResponse(String responseBody) {
                Lect_parse(responseBody);
                runOnUiThread(()->{
                    adapter = new LectureAdapter(getApplicationContext(), lectures);
                    rv.setAdapter(adapter);
                });
            }
        });
    }

    public void Lect_parse(String resp){
        try{
            lectures.clear();
            JSONArray array = new JSONArray(resp);
            for (int i = 0; i < array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                Lecture lecture = new Lecture();
                lecture.setId(obj.getString("id"));
                lecture.setCourse_id(obj.getString("course_id"));
                lecture.setTitle(obj.getString("title"));
                lecture.setContent(obj.getString("content"));
                lecture.setContent(obj.getString("video_url"));
                lectures.add(lecture);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}