package com.gcrick.ovj.guesscricket;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A placeholder fragment containing a simple view.
 */
public class SampleFragmentActivityFragment extends Fragment {

    public SampleFragmentActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sample_fragment, container, false);

        EditText ed = (EditText) view.findViewById(R.id.editText);
        Button sendButton = (Button) view.findViewById(R.id.button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                
            }
        });

        return view;
    }


}
