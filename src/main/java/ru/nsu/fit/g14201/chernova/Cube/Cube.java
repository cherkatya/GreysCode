package ru.nsu.fit.g14201.chernova.Cube;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * Created by Катя on 02.10.2016.
 */
public class Cube {
    protected int N;
    protected int countVertexs;
    private long countGamiltonov;
    private long countCodes;

    private Map<int[], Integer> matches = new HashMap<int[], Integer>();
    private Map<int[], Integer> spectors = new HashMap<>();

    private boolean isMatchesCount = false;
    private boolean isCycles = false;
    private boolean isSpectorsCount = false;
    //TODO: чистить все коды, выделяя только классы, читая все коды из файла

    public Cube(int N){
        this.N = N;
        countVertexs = exponentInt(2, N);
        countGamiltonov = 0;
        countCodes = 0;
    }

    public long getCountGamiltonov(){
        return countGamiltonov;
    }

    private int exponentInt(int number, int expNum){
        int res = 1;

        for(int i = 0; i < expNum; i++)
            res *= number;

        return res;
    }

    protected boolean isGamiltonov(int[] jumps, int[] counter){
        for(int i = jumps.length - 1; i >= 0; i--)
            counter[jumps[i] - 1] = (counter[jumps[i] - 1] + 1) % 2;

        if(goodSequence(counter)) {
            for(int i = 0; i < N; i++)
                counter[i] = 0;
            return false;
        }

        for(int i = 0; i < N; i++)
            counter[i] = 0;

        return true;
    }
    protected boolean goodSequence(int[] counter){
        for(int i = 0; i < N; i++)
            if(counter[i] == 1)
                return true;
        return false;
    }
    protected boolean checkGamilton(int[] jumps, int[] counter){
        int size = 0;
        for(int i = 0; i < jumps.length; i++)
            if(jumps[i] == -1) {
                size = i;
                break;
            }

        for(int i = size - 1; i >= 0; i--) {
            counter[jumps[i] - 1] = (counter[jumps[i] - 1] + 1) % 2;
            if(!goodSequence(counter)) {
                for(int j = 0; j < N; j++)
                    counter[j] = 0;
                return false;
            }
        }

        for(int i = 0; i < N; i++)
            counter[i] = 0;
        return true;
    }
    protected void recursion(int depth, int[] jumps, int[] counter){
        if(depth == countVertexs){
            for(int i = 0; i < N; i++) {
                jumps[depth - 1] = i+1;
                if(isGamiltonov(jumps, counter)) {
                    countGamiltonov++;
                    doWork(jumps);
                }
            }
        }
        else {
            for (int i = 0; i < N; i++) {
                jumps[depth - 1] = i + 1;
                if (checkGamilton(jumps, counter))
                    recursion(depth + 1, jumps, counter);
            }
        }

        jumps[depth - 1] = -1;
    }
    public void GamiltonCycle(){
        int depth = 0;
        int[] jumps = new int[countVertexs];
        int[] counter = new int[N];
        for(int i = 0; i < countVertexs; i++)
            jumps[i] = -1;
        for(int i = 0; i < N; i++)
            counter[i] = 0;

        jumps[0] = 1;
        jumps[1] = 2;
        depth += 2;

        recursion(depth + 1, jumps, counter);

        countGamiltonov = countGamiltonov * N * (N-1);
        doRes();
    }


    private void doWork(int[] jumps){
        if(isMatchesCount)
            recountMatches(jumps);
        if(isCycles)
            try {
                printCycle(jumps);
            } catch (IOException e) {
                e.printStackTrace();
            }
        if(isSpectorsCount)
            recountSpectors(jumps);
    }
    private void doRes(){
        if(isMatchesCount)
            try {
                printMatchesCount();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        if(isSpectorsCount)
            try {
                printSpectorsCount();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }


    private void quicksort(int[] match){
        ArrayList<int[]> array = new ArrayList<>();
        for(int i = 0; i < N; i++){
            int[] para = new int[2];
            para[0] = match[i];
            para[1] = match[i + N];
            array.add(para);
        }
        Comparator<int[]> comparator = new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                if(o1[0] < o2[0] || (o1[0] == o2[0] && o1[1] < o2[1]))
                    return -1;
                if(o1[0] > o2[0] || (o1[0] == o2[0] && o1[1] > o2[1]))
                    return 1;
                return 0;
            }
        };

        array.sort(comparator);

        for(int i = 0; i < N; i++){
            match[i] = array.get(i)[0];
            match[i + N] = array.get(i)[1];
        }
    }
    private void sortMatch(int[] match){
        //сначала половина с максимальным элементом
        int max = match[0];
        int index = 0;
        for(int i = 1; i < match.length; i++)
            if(max < match[i]) {
                max = match[i];
                index = i;
            }
        if(index >= N){
            for(int j = 0; j < N; j++){
                int t = match[j];
                match[j] = match[N + j];
                match[N + j] = t;
            }
        }

        //sorting
        quicksort(match);
    }
    private int[] getMatch(int[] code){
        int[] match = new int[N * 2]; //1, 2, 3, 4...
        for(int i = 0; i < code.length / 2; i++) {
            match[code[i * 2] - 1]++;
            match[code[i * 2 + 1] + N - 1]++;
        }

        sortMatch(match);
        return match;
    }
    private void recountMatches(int[] jumps){
        int[] match = getMatch(jumps);

        int count = 0;
        for(Iterator<Map.Entry<int[], Integer>> iter = matches.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<int[], Integer> entry = iter.next();
            if(Arrays.equals(entry.getKey(), match)){
                count = entry.getValue();
                iter.remove();
                break;
            }
        }

        matches.put(match, count + 1);
    }

    public void sortSpector(int[] spector){
        ArrayList<Integer> array = new ArrayList<>();
        for (int el : spector) {
            array.add(el);
        }
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if(o1 < o2)
                    return -1;
                if(o1 > o2)
                    return 1;
                return 0;
            }
        };

        array.sort(comparator);

        for(int i = 0; i < spector.length; i++){
            spector[i] = array.get(i);
        }
    }
    private int[] getSpector(int[] jumps){
        int[] spector = new int[N]; //1, 2, 3, 4...
        for(int i = 0; i < jumps.length; i++) {
            spector[jumps[i] - 1]++;
        }

        sortSpector(spector);
        return spector;
    }
    private void recountSpectors(int[] jumps){
        int[] spector = getSpector(jumps);

        int count = 0;
        for(Iterator<Map.Entry<int[], Integer>> iter = spectors.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<int[], Integer> entry = iter.next();
            if(Arrays.equals(entry.getKey(), spector)){
                count = entry.getValue();
                iter.remove();
                break;
            }
        }

        spectors.put(spector, count + 1);
    }
    private void printCycle(int[] jumps) throws IOException {
        int[] match = getMatch(jumps);
        int[] spector = getSpector(jumps);

        String spectorStr = "";
        for (int el : spector) {
            spectorStr += el + "_";
        }
        spectorStr = spectorStr.substring(0, spectorStr.length() - 1);

        String matchStr = "";
        for (int el : match) {
            matchStr += el + "_";
        }
        matchStr = matchStr.substring(0, matchStr.length() - 1);

        Path path1 = Paths.get("N" + ((Integer)N).toString() + "/spector" + spectorStr);
        if(!Files.exists(path1)){
            new File("N" + ((Integer)N).toString() + "/spector" + spectorStr).mkdir();
        }
        Path path2 = Paths.get("N" + ((Integer)N).toString() + "/spector" + spectorStr + "/match" + matchStr);
        if(!Files.exists(path2)){
            new File("N" + ((Integer)N).toString() + "/spector" + spectorStr + "/match" + matchStr).mkdir();
        }
        Path path3 = Paths.get("N" + ((Integer)N).toString() + "/spector" + spectorStr + "/match" + matchStr + "/all");
        if(!Files.exists(path3)){
            new File("N" + ((Integer)N).toString() + "/spector" + spectorStr + "/match" + matchStr + "/all").mkdir();
        }


        String filePath = "N" + ((Integer)N).toString() + "/spector" + spectorStr + "/match" + matchStr +
                "/all/spector" + spectorStr + "_match" + matchStr + "_all.txt";
        File file = new File(filePath);
        if(!file.exists()){
            file.createNewFile();
        }

        String text = "";
        for (int jump : jumps) {
            text += jump + " ";
        }
        text += "\n";

        Files.write(Paths.get(filePath), text.getBytes(), StandardOpenOption.APPEND);
    }

    private void printMatchesCount() throws FileNotFoundException {
        PrintStream file = new PrintStream("N" + ((Integer)N).toString() + "/matches" + ((Integer)N).toString() + ".txt");

        for (Map.Entry<int[], Integer> entry : matches.entrySet()) {
            for(int i = 0; i < N; i++)
                file.print(entry.getKey()[i] + " ");
            file.print("_ ");
            for(int i = N; i < 2 * N; i++)
                file.print(entry.getKey()[i] + " ");
            file.println(entry.getValue() * N * (N-1));
        }
    }
    private void printSpectorsCount() throws FileNotFoundException {
        PrintStream file = new PrintStream("N" + ((Integer)N).toString() + "/spectors" + ((Integer)N).toString() + ".txt");

        for (Map.Entry<int[], Integer> entry : spectors.entrySet()) {
            for(int i = 0; i < entry.getKey().length; i++)
                file.print(entry.getKey()[i] + " ");
            file.println(entry.getValue() * N * (N-1));
        }
    }


    public void setIsMatchesCount(){
        isMatchesCount = true;
    }
    public void setIsCycles(){ isCycles = true; }
    public void setIsSpectorsCount() { isSpectorsCount = true; }


    public void distinguishClasses(){
        //получить имя читаемого файла
        //прочитать все коды, формируя банк
        //сформировать имя будущего файла
        //записать все коды в новый файл
        String oldFileName = getReadFileName();
        ArrayList<int[]> codes = readCodes(oldFileName);
        String newFileName = getWriteFileName(oldFileName);
        printClasses(newFileName);
    }
    private String getReadFileName(){
        return "";
    }
    private ArrayList<int[]> readCodes(String fileName){
        ArrayList<int[]> codes = new ArrayList<>();


        return codes;
    }
    private String getWriteFileName(String oldFileName){
        return "";
    }
    private void printClasses(String newFileName) throws IOException {
        int[] match = getMatch(jumps);
        int[] spector = getSpector(jumps);

        String spectorStr = "";
        for (int el : spector) {
            spectorStr += el + "_";
        }
        spectorStr = spectorStr.substring(0, spectorStr.length() - 1);

        String matchStr = "";
        for (int el : match) {
            matchStr += el + "_";
        }
        matchStr = matchStr.substring(0, matchStr.length() - 1);

        Path path1 = Paths.get("N" + ((Integer)N).toString() + "/spector" + spectorStr);
        if(!Files.exists(path1)){
            new File("N" + ((Integer)N).toString() + "/spector" + spectorStr).mkdir();
        }
        Path path2 = Paths.get("N" + ((Integer)N).toString() + "/spector" + spectorStr + "/match" + matchStr);
        if(!Files.exists(path2)){
            new File("N" + ((Integer)N).toString() + "/spector" + spectorStr + "/match" + matchStr).mkdir();
        }


        String filePath = "N" + ((Integer)N).toString() + "/spector" + spectorStr + "/match" + matchStr +
                "/spector" + spectorStr + "_match" + matchStr + ".txt";
        File file = new File(filePath);
        if(!file.exists()){
            file.createNewFile();
        }

        String text = "";
        for (int jump : jumps) {
            text += jump + " ";
        }
        text += "\n";

        Files.write(Paths.get(filePath), text.getBytes(), StandardOpenOption.APPEND);
    }




    public void printingKDcount(){
        int k = 16;
        int d = 5;

        countKDforBigCube(k, d);
        System.out.println(countCodes);

    }
    public boolean checkKDtotal(int[] jumps, int[] counter, int K, int D){
        int h1 = 0;
        int h2 = 0;

        for(int i = 0; i < jumps.length; i++){
            for(int j = 1; j <= K; j++)
               counter[jumps[(jumps.length + i - j) % jumps.length] - 1] = (counter[jumps[(jumps.length + i - j) % jumps.length] - 1] + 1) % 2;
            for(int l = 0; l < N; l++) {
                h1 += counter[l];
                counter[l] = 0;
            }

            for(int j = 1; j <= K; j++)
                counter[jumps[(i + j - 1) % jumps.length] - 1] = (counter[jumps[(i + j - 1) % jumps.length] - 1] + 1) % 2;
            for(int l = 0; l < N; l++) {
                h2 += counter[l];
                counter[l] = 0;
            }

            if(D != h1 && D != h2)
                return false;

            h1 = 0;
            h2 = 0;
        }

        return true;
    }
    public boolean checkKD(int[] jumps, int[] counter, int K, int D){
        int size = 0;
        int h1 = 0;
        int h2 = 0;
        for(int i = 0; i < jumps.length; i++)
            if(jumps[i] == -1) {
                size = i - 1;
                break;
            }

        if(size + 1 < 2*K)
            return true;


        for(int i = 0; i < K; i++)
            counter[jumps[size - K - i] - 1] = (counter[jumps[size - K - i] - 1] + 1) % 2;
        for(int i = 0; i < N; i++)
            h1 += counter[i];

        for (int i = 0; i < N; i++)
            counter[i] = 0;

        for(int i = size - K + 1; i <= size; i++)
            counter[jumps[i] - 1] = (counter[jumps[i] - 1] + 1) % 2;
        for(int i = 0; i < N; i++)
            h2 += counter[i];

        for (int i = 0; i < N; i++)
            counter[i] = 0;

        if(D != h1 && D != h2)
            return false;

        return true;
    }
    public void shortRecursion(int depth, int[] jumps, int[] counter, int K, int D){//проверка на гамильтоновость; проверка на кд
        if(depth == countVertexs){
            for(int i = 0; i < N; i++) {
                jumps[depth - 1] = i+1;
                if(isGamiltonov(jumps, counter) && checkKDtotal(jumps, counter, K, D))
                    countCodes++;
            }
        }
        else {
            for (int i = 0; i < N; i++) {
                jumps[depth - 1] = i + 1;
                if (checkGamilton(jumps, counter) && checkKD(jumps, counter, K, D))
                    shortRecursion(depth + 1, jumps, counter, K, D);
            }
        }

        jumps[depth - 1] = -1;
    }
    public void countKDforBigCube(int K, int D){
        int depth = 2;
        countCodes = 0;
        int[] jumps = new int[countVertexs];
        int[] counter = new int[N];
        for(int i = 0; i < countVertexs; i++)
            jumps[i] = -1;
        for(int i = 0; i < N; i++)
            counter[i] = 0;

        jumps[0] = 1;
        jumps[1] = 2;

        shortRecursion(depth + 1, jumps, counter, K, D);

        countCodes = countCodes * N * (N-1);//this is for printing to file
    }



    private void printCode(int[] jumps){
        for(int i = 0; i < jumps.length; i++)
            System.out.print(jumps[i]);
        System.out.println("");
    }

    public void gamiltonClasses4(){
        int depth = 0;
        int[] jumps = new int[countVertexs];
        int[] counter = new int[N];
        for(int i = 0; i < countVertexs; i++)
            jumps[i] = -1;
        for(int i = 0; i < N; i++)
            counter[i] = 0;

        jumps[0] = 1;
        jumps[1] = 2;
        jumps[2] = 3;
        jumps[3] = 4;
        depth = 4;
        recursion(depth + 1, jumps, counter);
        System.out.println(countGamiltonov);


        jumps[0] = 1;
        jumps[1] = 2;
        jumps[2] = 1;
        jumps[3] = 3;
        jumps[4] = 4;
        depth = 5;
        recursion(depth + 1, jumps, counter);
        System.out.println(countGamiltonov);

        jumps[0] = 1;
        jumps[1] = 2;
        jumps[2] = 3;
        jumps[3] = 1;
        jumps[4] = 4;
        recursion(depth + 1, jumps, counter);
        System.out.println(countGamiltonov);

        jumps[0] = 1;
        jumps[1] = 2;
        jumps[2] = 3;
        jumps[3] = 2;
        jumps[4] = 4;
        recursion(depth + 1, jumps, counter);
        System.out.println(countGamiltonov);


        jumps[0] = 1;
        jumps[1] = 2;
        jumps[2] = 1;
        jumps[3] = 3;
        jumps[4] = 2;
        jumps[5] = 4;
        depth = 6;
        recursion(depth + 1, jumps, counter);
        System.out.println(countGamiltonov);

        jumps[0] = 1;
        jumps[1] = 2;
        jumps[2] = 3;
        jumps[3] = 1;
        jumps[4] = 2;
        jumps[5] = 4;
        recursion(depth + 1, jumps, counter);
        System.out.println(countGamiltonov);

        jumps[0] = 1;
        jumps[1] = 2;
        jumps[2] = 3;
        jumps[3] = 2;
        jumps[4] = 1;
        jumps[5] = 4;
        recursion(depth + 1, jumps, counter);
        System.out.println(countGamiltonov);

        jumps[0] = 1;
        jumps[1] = 2;
        jumps[2] = 3;
        jumps[3] = 1;
        jumps[4] = 3;
        jumps[5] = 4;
        recursion(depth + 1, jumps, counter);
        System.out.println(countGamiltonov);

        jumps[0] = 1;
        jumps[1] = 2;
        jumps[2] = 1;
        jumps[3] = 3;
        jumps[4] = 1;
        jumps[5] = 4;
        recursion(depth + 1, jumps, counter);
        System.out.println(countGamiltonov);


        jumps[0] = 1;
        jumps[1] = 2;
        jumps[2] = 3;
        jumps[3] = 1;
        jumps[4] = 2;
        jumps[5] = 1;
        jumps[6] = 4;
        depth = 7;
        recursion(depth + 1, jumps, counter);
        System.out.println(countGamiltonov);

        jumps[0] = 1;
        jumps[1] = 2;
        jumps[2] = 1;
        jumps[3] = 3;
        jumps[4] = 1;
        jumps[5] = 2;
        jumps[6] = 4;
        recursion(depth + 1, jumps, counter);
        System.out.println(countGamiltonov);

        jumps[0] = 1;
        jumps[1] = 2;
        jumps[2] = 1;
        jumps[3] = 3;
        jumps[4] = 2;
        jumps[5] = 1;
        jumps[6] = 4;
        recursion(depth + 1, jumps, counter);
        System.out.println(countGamiltonov);


        jumps[0] = 1;
        jumps[1] = 2;
        jumps[2] = 1;
        jumps[3] = 3;
        jumps[4] = 1;
        jumps[5] = 2;
        jumps[6] = 1;
        jumps[7] = 4;
        depth = 8;
        recursion(depth + 1, jumps, counter);
        System.out.println(countGamiltonov);

        jumps[0] = 1;
        jumps[1] = 2;
        jumps[2] = 1;
        jumps[3] = 3;
        jumps[4] = 2;
        jumps[5] = 1;
        jumps[6] = 2;
        jumps[7] = 4;
        recursion(depth + 1, jumps, counter);
        System.out.println(countGamiltonov);

        jumps[0] = 1;
        jumps[1] = 2;
        jumps[2] = 3;
        jumps[3] = 2;
        jumps[4] = 1;
        jumps[5] = 2;
        jumps[6] = 3;
        jumps[7] = 4;
        recursion(depth + 1, jumps, counter);
        System.out.println(countGamiltonov);

        countGamiltonov = countGamiltonov * 24;
    }
}
