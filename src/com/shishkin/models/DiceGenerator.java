package com.shishkin.models;

import java.util.ArrayList;
import java.util.List;

public class DiceGenerator {
    private DiceGenerator() {
    }

    public static List<Dice> generate(int countDices, int countFaces) {
        List<Dice> diceList = new ArrayList<>();
        for (int i = 0; i < countDices; i++) {
            diceList.add(new Dice(countFaces));
        }
        return diceList;
    }

}
