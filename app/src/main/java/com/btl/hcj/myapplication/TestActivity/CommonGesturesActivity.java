package com.btl.hcj.myapplication.TestActivity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.MotionEvent;

import android.support.v4.app.Fragment;

import com.btl.hcj.myapplication.MyFragments.ExamToolbarFragment;
import com.btl.hcj.myapplication.MyFragments.TextFragment;
import com.btl.hcj.myapplication.R;


public class CommonGesturesActivity extends AppCompatActivity
        implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, ExamToolbarFragment.ToolbarListener{

    private TextView gestureText;
    private TextView fragmentText;
    // 호환성 유지 필요 시 private GestureDetectorCompat gDetector;
    private GestureDetector gDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_gestures);

        gestureText = findViewById(R.id.gestureStatusText);
        fragmentText = findViewById(R.id.textView);
        this.gDetector = new GestureDetector(this, this);
        gDetector.setOnDoubleTapListener(this);
    }

    // 터치이벤트를 감지해서, GestureDetector 인스턴스에게 전달
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        gestureText.setText("onSingleTapConfirmed");
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        gestureText.setText("onDoubleTap");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        gestureText.setText("onDoubleTapEvent");
        return true;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        gestureText.setText("onDown");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        gestureText.setText("onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        gestureText.setText("onSingleTapUp");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        gestureText.setText("onScroll");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        gestureText.setText("onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        gestureText.setText("onFling");
        return true;
    }

    @Override
    public void onButtonClick(int fontSize, String text) {
        TextFragment textFragment = (TextFragment) getSupportFragmentManager().findFragmentById(R.id.text_fragment);
        textFragment.changeTextProperties(fontSize, text);
    }

//   메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_example, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TextFragment tf = (TextFragment) getSupportFragmentManager().findFragmentById(R.id.text_fragment);
        switch (item.getItemId()) {
            case R.id.menu_red:
                if (item.isCheckable()) item.setChecked(false);
                else item.setChecked(true);
                gestureText.setTextColor(Color.RED);
                fragmentText.setTextColor(Color.RED);
                return true;
            case R.id.menu_green:
                if (item.isCheckable()) item.setChecked(false);
                else item.setChecked(true);
                gestureText.setTextColor(Color.GREEN);
                tf.changeTextColor(Color.GREEN);
                return true;
            case R.id.menu_blue:
                if (item.isCheckable()) item.setChecked(false);
                else item.setChecked(true);
                gestureText.setTextColor(Color.BLUE);
                tf.changeTextColor(Color.BLUE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
