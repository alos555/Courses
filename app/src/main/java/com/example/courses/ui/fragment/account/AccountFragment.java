package com.example.courses.ui.fragment.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.courses.R;
import com.example.courses.databinding.FragmentAccountBinding;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Переходы по пунктам меню
        binding.btnFavourites.setOnClickListener(v ->
                Navigation.findNavController(root).navigate(R.id.navigation_favourites));

        binding.btnEditAccount.setOnClickListener(v ->
                Navigation.findNavController(root).navigate(R.id.navigation_edit_account));

        binding.btnHelp.setOnClickListener(v ->
                Navigation.findNavController(root).navigate(R.id.navigation_help));

        binding.btnMyCourses.setOnClickListener(v ->
                Navigation.findNavController(root).navigate(R.id.navigation_my_courses));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}