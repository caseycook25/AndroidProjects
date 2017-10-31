package com.example.cook01.project1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener {

    private ImageView fOne;
    private ImageView fTwo;
    private ImageView fThree;
    private ImageView reset;
    private Animation animRotate1;
    private int dollars = 5;
    private TextView myText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animRotate1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate);
        animRotate1.setAnimationListener(this);
    }

    // Button is clicked
    public void doGoButton(View view) {
        dollars -= 1;
        myText = (TextView) findViewById(R.id.moneyLeft);

        rotateIt();

        if (dollars == 0) {
            ImageView go = (ImageView) findViewById(R.id.go);
            go.setVisibility(View.INVISIBLE);
        }
    }

    private void setMoney() {
        MainActivity.this.myText.setText("$ " + dollars);
    }

    //set flowers to temp and begin animation
    private void rotateIt() {
        getfOne();
        getFTwo();
        getFThree();
        fOne.setImageResource(R.drawable.tmp);
        fOne.startAnimation(animRotate1);
        fTwo.setImageResource(R.drawable.tmp);
        fTwo.startAnimation(animRotate1);
        fThree.setImageResource(R.drawable.tmp);
        fThree.startAnimation(animRotate1);
    }

    // get random image for each imageview and set them.
    private void randomize() {
        int rand1 = getRandomId(getImageViews());
        fOne.setImageResource(rand1);
        int rand2 = getRandomId(getImageViews());
        fTwo.setImageResource(rand2);
        int rand3 = getRandomId(getImageViews());
        fThree.setImageResource(rand3);
    }

    //compare flowers to see how much money (if any) to add
    private void moneyMath() {
        if ((fOne.getDrawable().getConstantState() == fTwo.getDrawable().getConstantState() && fOne.getDrawable().getConstantState() != fThree.getDrawable().getConstantState() && fTwo.getDrawable().getConstantState() != fThree.getDrawable().getConstantState()) ||
                (fOne.getDrawable().getConstantState() == fThree.getDrawable().getConstantState() && fOne.getDrawable().getConstantState() != fTwo.getDrawable().getConstantState() && fTwo.getDrawable().getConstantState() != fThree.getDrawable().getConstantState()) ||
                (fOne.getDrawable().getConstantState() != fTwo.getDrawable().getConstantState() && fOne.getDrawable().getConstantState() != fThree.getDrawable().getConstantState() && fTwo.getDrawable().getConstantState() == fThree.getDrawable().getConstantState())) {
            dollars += 2;
        } else if ((fOne.getDrawable().getConstantState() == fTwo.getDrawable().getConstantState() && fOne.getDrawable().getConstantState() == fThree.getDrawable().getConstantState() && fTwo.getDrawable().getConstantState() == fThree.getDrawable().getConstantState())) {
            dollars += 3;
        }
    }

    // Reset button clicked
    public void doReset(View view) {
        reset = (ImageView) findViewById(R.id.reset);
        if (reset.getVisibility() == View.VISIBLE) {
            dollars = 5;
            setMoney();
            reset.setVisibility(View.INVISIBLE);
        }
        ImageView go = (ImageView) findViewById(R.id.go);  // added after submission
        go.setVisibility(View.INVISIBLE);
    }

    private void getFThree() {
        fThree = (ImageView) findViewById(R.id.flow3);
    }

    private void getFTwo() {
        fTwo = (ImageView) findViewById(R.id.flow2);
    }

    private void getfOne() {
        fOne = (ImageView) findViewById(R.id.flow1);
    }

    private int[] getImageViews() {
        int[] resIds = new int[3];
        resIds[0] = getResources().getIdentifier("f1", "drawable", "com.example.cook01.project1");
        resIds[1] = getResources().getIdentifier("f2", "drawable", "com.example.cook01.project1");
        resIds[2] = getResources().getIdentifier("f3", "drawable", "com.example.cook01.project1");

        return resIds;
    }

    private int getRandomId(int[] _ids) {
        int i;
        Random rnd = new Random();
        i = rnd.nextInt(3);
        return _ids[i];
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        randomize();
        reset = (ImageView) findViewById(R.id.reset);
        reset.setVisibility(View.VISIBLE);
        moneyMath();
        setMoney();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}