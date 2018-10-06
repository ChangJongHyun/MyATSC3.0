package com.btl.hcj.myapplication.MyFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.btl.hcj.myapplication.R;


public class TextFragment extends Fragment {

    private static TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.text_fragment, container, false);

        textView = view.findViewById(R.id.textView);

        return view;
    }

    public void changeTextProperties(int fontSize, String text) {
        textView.setTextSize(fontSize);
        textView.setText(text);
    }

    public void changeTextColor(int Color) {
        textView.setTextColor(Color);
    }
}
