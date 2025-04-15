package com.example.soundslike.ui.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider; // Import

import com.example.soundslike.R;

public class LibraryFragment extends Fragment {

    private LibraryViewModel libraryViewModel; // Add ViewModel reference

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the correct layout
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        // Initialize ViewModel
        libraryViewModel = new ViewModelProvider(this).get(LibraryViewModel.class);

        // TODO: Find RecyclerViews and setup adapters later
        // TODO: Setup click listener for Liked Songs card

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: Observe ViewModel LiveData later
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // TODO: Null out view references later
    }
}