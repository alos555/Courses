package com.example.courses.ui.fragment.editaccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.courses.R;
import com.example.courses.data.model.DataBinding;
import com.example.courses.data.source.SupabaseClient;

import java.io.IOException;
import java.util.regex.Pattern;

public class EditAccountFragment extends Fragment {

    SupabaseClient s;
    DataBinding d;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_account, container, false);

        d = new DataBinding(requireContext());

        Button btn = view.findViewById(R.id.btnSaveChanges);
        s = new SupabaseClient();
        EditText email, name;
        email = view.findViewById(R.id.etEmail);
        email.setText(d.getEmail());
        name = view.findViewById(R.id.etName);
        name.setText(d.getName());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmail(email.getText().toString().trim())){
                    if (!email.getText().toString().trim().equals(d.getEmail())){
                        emailC(email);
                        if (!name.getText().toString().trim().equals(d.getName())){
                            nameC(name);
                            nav(view);
                        }
                        nav(view);
                    }else{
                        if (!name.getText().toString().trim().equals(d.getName())){
                            nameC(name);
                            nav(view);
                        }
                        nav(view);
                    }
                }else{
                    Toast.makeText(requireContext(), "Неверный формат почты", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void emailC(EditText ETemail){
        s.changeEmail(requireContext(), ETemail.getText().toString().trim(), new SupabaseClient.supa_callback() {
            @Override
            public void onFailure(IOException e) {
                emailC(ETemail);
            }

            @Override
            public void onResponse(String responseBody) {
                d.setEmail(ETemail.getText().toString().trim());
            }
        });
    }

    public void nameC(EditText ETname){
        s.updateProfile(requireContext(), ETname.getText().toString().trim(), new SupabaseClient.supa_callback() {
            @Override
            public void onFailure(IOException e) {
                nameC(ETname);
            }

            @Override
            public void onResponse(String responseBody) {
                d.setName(ETname.getText().toString().trim());
            }
        });
    }

    public static boolean checkEmail(String email) {
        String emailPattern = "^[a-z0-9]+@[a-z0-9]+\\.[a-z0-9]+$";
        return Pattern.compile(emailPattern).matcher(email).matches();
    }

    public void nav(View view){
        Navigation.findNavController(view).navigate(R.id.navigation_home);
    }
}