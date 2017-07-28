package ru.nsu.fit.g14201.chernova.Cube;

import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Created by Катя on 13.02.2017.
 */
public class Spector {
    PrintStream fileForSpector = null;
    int[] spector;

    private static final boolean isClass = false;

    public Spector(int[] spector){
        this.spector = spector;
        String spectorName = Integer.toString(this.spector[0]);
        for(int i = 1; i < spector.length; i++)
            spectorName = spectorName + "_" + Integer.toString(spector[i]);

        if(isClass)
            spectorName += "_codes";

        try {
            fileForSpector = new PrintStream(spectorName + ".txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public PrintStream getFile(){
        return fileForSpector;
    }
    public int[] getSpectorValues(){ return spector; }

}
