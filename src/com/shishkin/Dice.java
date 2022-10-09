package com.shishkin;

import java.util.Random;

public class Dice {
    private static final Random r = new Random();
    private final int countFaces;

    public Dice(int countFaces) {
        this.countFaces = countFaces;
    }

    public int flip() {
        return r.nextInt(countFaces) + 1;
    }
}
