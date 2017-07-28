package ru.nsu.fit.g14201.chernova;

import ru.nsu.fit.g14201.chernova.Cube.Code;
import ru.nsu.fit.g14201.chernova.Cube.Cube;
import ru.nsu.fit.g14201.chernova.Cube.LittleCube;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class Main
{
    private static void countGamiltonovCyclesInLittleCube(int N){
        LittleCube cube = new LittleCube(N);
        long starttime = System.currentTimeMillis();
        cube.GamiltonCycle();
        System.out.println(cube.getCountGamiltonov());
        System.out.print("Time: ");
        System.out.println(System.currentTimeMillis() - starttime);
    }
    private static void countGamiltonovCyclesInBigCube(int N){
        Cube cube = new Cube(N);
        long starttime = System.currentTimeMillis();
        cube.GamiltonCycle();
        System.out.println(cube.getCountGamiltonov());
        System.out.print("Time: ");
        System.out.println(System.currentTimeMillis() - starttime);
    }
    /*для каждого кода для каждого K найдет расстояния Хемминга*/
    private static void findKandDForCodesInLittleCube(int N){
        LittleCube cube = new LittleCube(N);
        long starttime = System.currentTimeMillis();
        cube.GamiltonCycle();
        System.out.println(cube.getCountGamiltonov());
        System.out.print("Time: ");
        System.out.println(System.currentTimeMillis() - starttime);

        for (Code code : cube.getGreysCodes()) {
            code.calculateHemmingDistance();
        }
    }
    /*по заданным K и D выведет все коды Грея, удовлетворяющие им*/
    private static void printHemmingListForLittleCube(int N){
        LittleCube cube = new LittleCube(N);
        long starttime = System.currentTimeMillis();
        cube.GamiltonCycle();
        System.out.println(cube.getCountGamiltonov());
        System.out.print("Time: ");
        System.out.println(System.currentTimeMillis() - starttime);

        for (Code code : cube.getGreysCodes()) {
            code.calculateHemmingDistance();
        }
        try {
            cube.printHemmingList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*выводит таблицу соответствия K, D и количество гамильтоновых циклов, которые удовлетворяют им*/
    private static void printTableKDForLittleCube(int N){
        LittleCube cube = new LittleCube(N);
        long starttime = System.currentTimeMillis();
        cube.GamiltonCycle();
        System.out.println(cube.getCountGamiltonov());
        System.out.print("Time: ");
        System.out.println(System.currentTimeMillis() - starttime);

        for (Code code : cube.getGreysCodes()) {
            code.calculateHemmingDistance();
        }
        try {
            cube.printTableKD();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*выводит таблицу соответствия K, D и количество гамильтоновых циклов по категориям, которые удовлетворяют им*/
    private static void printTableKDWithCategoriesForLittleCube(int N){
        LittleCube cube = new LittleCube(N);
        long starttime = System.currentTimeMillis();
        cube.GamiltonCycle();
        System.out.println(cube.getCountGamiltonov());
        System.out.print("Time: ");
        System.out.println(System.currentTimeMillis() - starttime);

        for (Code code : cube.getGreysCodes()) {
            code.calculateHemmingDistance();
        }
        try {
            cube.printTableKDWithSpectors();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*строит коды для N+1 куба*/
    private static void buildNplus1(int N){
        LittleCube cube = new LittleCube(N-1);
        long starttime = System.currentTimeMillis();
        cube.GamiltonCycle();
        System.out.println(cube.getCountGamiltonov());
        System.out.print("Time: ");
        System.out.println(System.currentTimeMillis() - starttime);

        LittleCube cubeBig = new LittleCube(N);
        cubeBig.buildNplus1(cube);
        System.out.print("Time: ");
        System.out.println(System.currentTimeMillis() - starttime);

        for (Code code : cubeBig.getGreysCodes()) {
            code.calculateHemmingDistance();
        }
        try {
            cubeBig.printHemmingList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*выводит все гамильтоновы циклы в данном кубе*/
    private static void printCycles(int N){
        LittleCube cube = new LittleCube(N);
        long starttime = System.currentTimeMillis();
        cube.GamiltonCycle();
        System.out.println(cube.getCountGamiltonov());
        System.out.print("Time: ");
        System.out.println(System.currentTimeMillis() - starttime);

        try {
            cube.printCycles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) {
        Integer N = new Integer(args[0]);

        Cube cube = new Cube(N);
//        //LittleCube cube = new LittleCube(N);
        long starttime = System.currentTimeMillis();
//
//        //cube.GamiltonCycleToFile();
//        //cube.getClassesFromFile();
//        /*try {
//            cube.makeSpectors5();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }*/

        //cube.setIsMatchesCount();
        //cube.setIsSpectorsCount();
        cube.setIsCycles();
        cube.GamiltonCycle();
        //printTableKDWithCategoriesForLittleCube(N);

        System.out.println(cube.getCountGamiltonov());
        System.out.print("Time: ");
        System.out.println(System.currentTimeMillis() - starttime);
    }
}
