package com.example.buuktu.utils;

import com.example.buuktu.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColorsUtils {
    private int getRandomColor()
    {
        List<Integer> colorcode=new ArrayList<>();
        colorcode.add(R.color.white);
        colorcode.add(R.color.redError);
        colorcode.add(R.color.brownBrown);
        colorcode.add(R.color.brownMaroon);

        Random random=new Random();
        int number=random.nextInt(colorcode.size());
        return colorcode.get(number);
    }
}
