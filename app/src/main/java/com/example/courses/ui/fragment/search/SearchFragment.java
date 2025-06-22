package com.example.courses.ui.fragment.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courses.R;
import com.example.courses.data.CourseAdapter;
import com.example.courses.data.model.Course;
import com.example.courses.data.source.SupabaseClient;
import com.example.courses.databinding.FragmentSearchBinding;
import com.example.courses.ui.CourseDetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private RecyclerView recyclerView;
    private EditText editTextSearch;
    private CourseAdapter adapter;
    private List<Course> courseList;
    SupabaseClient s;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        recyclerView = binding.recyclerViewSearchResults;
        editTextSearch = binding.etSearchCourse;
        courseList = new ArrayList<>();
        s = new SupabaseClient();

        getC();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getC(){
        s.getCourses(requireContext(), new SupabaseClient.supa_callback() {
            @Override
            public void onFailure(IOException e) {
                getC();
            }

            @Override
            public void onResponse(String responseBody) {
                parseCourse(responseBody);
                requireActivity().runOnUiThread(()->{
                    adapter = new CourseAdapter(courseList, requireContext(), new CourseAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Course course) {
                            Intent i = new Intent(requireActivity(), CourseDetailsActivity.class);
                            i.putExtra("course", course);
                            startActivity(i);
                        }
                    });

                    recyclerView.setAdapter(adapter);

                    editTextSearch.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {}
                    });
                });
            }
        });
    }

    public void parseCourse(String response){
        courseList.clear();
        try{
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                Course c = new Course();
                c.setId(object.getString("id"));
                c.setTitle(object.getString("title"));
                c.setDescription(object.getString("description"));
                c.setPrice(object.getDouble("price"));
                c.setCreatedAt(object.getString("created_at"));
                courseList.add(c);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}