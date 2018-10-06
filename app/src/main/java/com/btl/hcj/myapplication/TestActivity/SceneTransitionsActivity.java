package com.btl.hcj.myapplication.TestActivity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;

import com.btl.hcj.myapplication.R;

public class SceneTransitionsActivity extends AppCompatActivity {

    ViewGroup rootContainer;
    Scene scene1;
    Scene scene2;
    Transition transitionMgr;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_transitions);

        rootContainer = findViewById(R.id.root_container);

        transitionMgr = TransitionInflater.from(this)
                .inflateTransition(R.transition.transition);

        scene1 = Scene.getSceneForLayout(rootContainer, R.layout.scene_layout, this);

        scene2 = Scene.getSceneForLayout(rootContainer, R.layout.scene2_layout, this);

        scene1.enter();
    }

    public void gotoScene2 (View view) {
        TransitionManager.go(scene2, transitionMgr);
    }

    public void gotoScene1 (View view) {
        TransitionManager.go(scene1, transitionMgr);
    }


}
