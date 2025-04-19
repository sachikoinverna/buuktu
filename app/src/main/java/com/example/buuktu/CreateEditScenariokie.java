package com.example.buuktu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEditScenariokie#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEditScenariokie extends Fragment {
    public CreateEditScenariokie() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateEditScenariokie.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateEditScenariokie newInstance() {
        return new CreateEditScenariokie();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_edit_scenariokie, container, false);
        return view;
    }
}