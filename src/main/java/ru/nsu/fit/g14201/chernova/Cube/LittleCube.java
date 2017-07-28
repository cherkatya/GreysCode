package ru.nsu.fit.g14201.chernova.Cube;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * N <= 4
 */
public class LittleCube extends Cube{
    private ArrayList<Code> greysCode = new ArrayList<Code>();
    private long countGamiltonov;
    ArrayList<Spector> spectors = new ArrayList<Spector>();

    private boolean renamingRes = false;


    public LittleCube(int N){
        super(N);
    }

    public ArrayList<Code> getGreysCodes(){
        return greysCode;
    }

    @Override
    protected void recursion(int depth, int[] jumps, int[] counter){
        if(depth == countVertexs){
            for(int i = 0; i < N; i++) {
                jumps[depth - 1] = i+1;
                if(isGamiltonov(jumps, counter)) {
                    countGamiltonov++;

                    if(!isShift(jumps))
                        greysCode.add(new Code(N, jumps));
                }
            }
        }
        else {
            if(depth == 5)
                System.out.println(100 / 25 * (jumps[2] * jumps[3] - 1));

            for (int i = 0; i < N; i++) {
                jumps[depth - 1] = i + 1;
                if (checkGamilton(jumps, counter))
                    recursion(depth + 1, jumps, counter);
            }
        }

        jumps[depth - 1] = -1;
    }









    public void printHemmingList() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter K and D.");
        int k = scanner.nextInt();
        int d = scanner.nextInt();
        //int num = 1;

        PrintStream file = new PrintStream("paramsHemming" + ((Integer)N).toString() + "K" + ((Integer)k).toString() + "D" + ((Integer)d).toString() + ".txt");
        for (Code code : getGreysCodes()) {
            if(code.getParamsK().get(k).getD1() == d || code.getParamsK().get(k).getD2() == d) {
                //file.print(num + ". ");
                for (int i = 0; i < code.getCode().length; i++)
                    file.print(code.getCode()[i]);
                file.println("");
                //num++;
            }

        }
    }
    public void printTableKD() throws FileNotFoundException {
        PrintStream file = new PrintStream("KDtable" + ((Integer)N).toString() + ".txt");

        for(int i = 0; i <= N; i++)
            file.print(String.format("%d      ", i));
        file.println("D");

        int counter = 0;

        for(int k = 1; k < countVertexs; k++) {
            file.print(String.format("%d    ", k));
            for (int d = 1; d <= N; d++) {
                for (Code code : greysCode) {
                    if (code.getParamsK().get(k).getD1() == d || code.getParamsK().get(k).getD2() == d)
                        counter++;
                }
                file.print(String.format("%d     ", counter));
                counter = 0;
            }
            file.println();
        }
        file.print("K");
    }
    public void printTableKDWithCategories() throws FileNotFoundException{
        if(N != 4){
            printTableKD();
            return;
        }

        PrintStream file = new PrintStream("KD4table" + ((Integer)N).toString() + ".txt");

        for (Code code : greysCode) {
            for(int i = 0; i < countVertexs; i++)
                file.print(code.getCode()[i]);
            file.println();

            file.print("0     ");
            for (int i = 1; i <= N; i++)
                file.print(String.format("  %d     ", i));
            file.println("D");

            for (int k = 1; k <= countVertexs / 2; k++) {
                file.print(String.format("%d       ", k));
                for (int d = 1; d <= N; d++) {
                    if (code.getParamsK().get(k).getD1() == d || code.getParamsK().get(k).getD2() == d)
                        file.print(String.format("%d       ", code.getCount()));
                    else
                        file.print("0       ");
                }
                file.println();
            }
            file.println("K");

            file.println();
            file.println();
            file.println();
        }
    }
    public void printTableKDWithSpectors() throws FileNotFoundException{
        if(N != 4){
            printTableKD();
            return;
        }

        PrintStream file = new PrintStream("KD4table" + ((Integer)N).toString() + ".txt");

        for (Code code : greysCode) {
            for(int i = 0; i < countVertexs; i++)
                file.print(code.getCode()[i]);
            file.println();

            int[] spector = new int[N];
            for(int i = 0; i < code.getCode().length; i++){
                spector[code.getCode()[i] - 1] += 1;
            }
            Arrays.sort(spector);
            file.print(String.format("Spector: (%d", spector[0]));
            for(int i = 1; i < N; i++)
                file.print(" " + spector[i]);
            file.println(")");

            //пектр паросочетаний


            file.print("0     ");
            for (int i = 1; i <= N; i++)
                file.print(String.format("  %d     ", i));
            file.println("D");

            for (int k = 1; k <= countVertexs / 2; k++) {
                file.print(String.format("%d       ", k));
                for (int d = 1; d <= N; d++) {
                    if (code.getParamsK().get(k).getD1() == d || code.getParamsK().get(k).getD2() == d)
                        file.print(String.format("%d       ", code.getCount()));
                    else
                        file.print("0       ");
                }
                file.println();
            }
            file.println("K");

            file.println();
            file.println();
            file.println();
        }
    }





    public void algorithm4GamiltonovCycle(){
        int[] g3Cycle1 = { 1, 2, 1, 3, 1, 2, 1, 3};

        int[] cycle1 = new int[countVertexs];

        for(int i = 0; i < countVertexs / 2 - 1; i++){
            cycle1[i] = g3Cycle1[i];
        }
        cycle1[countVertexs / 2 - 1] = N;
        for(int i = countVertexs / 2; i < countVertexs - 1; i++){
            cycle1[i] = cycle1[countVertexs - i - 2];
        }
        cycle1[countVertexs - 1] = N;
        greysCode.add(new Code(N, cycle1));

        for(int i = 0; i < countVertexs; i +=2){
            cycle1[i] = g3Cycle1[i / 2];
            cycle1[i+1] = N;
        }
        greysCode.add(new Code(N, cycle1));

        for(int i = 1; i < countVertexs; i +=2){
            cycle1[i] = g3Cycle1[(i - 1) / 2];
            cycle1[i-1] = N;
        }

        int startSize = greysCode.size();
        for(int i = 0; i < startSize; i++)
            findNewCycles(greysCode.get(i).getCode());
    }
    public void findNewCycles(int[] code){
        int a;
        for(int i = 1; i < countVertexs; i++){
            a = code[i - 1];
            for(int j = 0; j < countVertexs - 1; j++)//сдвиг влево (<<)
                code[j] = code[(j+1) % countVertexs];
            code[countVertexs - 1] = a;

            if(isNewCycle(code))
                greysCode.add(new Code(N, code));
        }

    }
    public boolean isNewCycle(int[] code){
        int[] nums = new int[N];
        for(int i = 0; i < N; i++)
            nums[i] = 0;
        int[] newCode = new int[countVertexs];
        for(int i = 0; i < countVertexs; i++)
            newCode[i] = 0;
        shortRecursion(1, nums, code, newCode, false, true);

        return false;
    }
    private boolean isExists(int[] code){
        int c = 0;
        for (Code code1 : greysCode) {
            for(int i = 0; i < code.length; i++)
                if(code1.getCode()[i] == code[i])
                    c++;
            if(c == code.length)
                return true;
        }
        return false;
    }
    private void shortRecursion(int depth, int[] nums, int[] code, int[] newCode, boolean res, boolean a){
        if(depth > N){
            for(int i = 0; i < code.length; i++)
                newCode[i] = nums[code[i] - 1];
            if(!isExists(newCode))
                greysCode.add(new Code(N, newCode));
        }
        else {
            for (int i = 1; i <= N; i++) {
                for(int j = 0; j < depth - 1; j++)
                    if(nums[j] == i){
                        a = false;
                        break;
                    }
                if(a){
                    nums[depth - 1] = i;
                    shortRecursion(depth + 1, nums, code, newCode, true, false);
                }
            }
        }
        nums[depth - 1] = 0;
    }

    public void printCycles() throws FileNotFoundException {
        PrintStream file = new PrintStream(String.format("cycles%d.txt", N));
        for (Code code : getGreysCodes()) {
            for (int i = 0; i < code.getCode().length; i++)
                file.print(code.getCode()[i]);
            file.println("");
        }
    }


    private void rename(int[] changedCode){
        for (Code code : greysCode) {
            if(Arrays.equals(changedCode, code.getCode())){
                code.addCode();
                renamingRes = true;
                return;
            }
        }
    }
    private void prepareRenaming(int[] changedCode, int depth, int[] values){
        boolean breakFlag = false;

        depth++;

        if(depth == N){
            for(int i = 0; i < changedCode.length; i++)
                changedCode[i] = values[changedCode[i] - 1];

            rename(changedCode);

            if(!renamingRes){
                for(int i = 0; i < changedCode.length; i++){
                    for(int j = 0; j < N; j++){
                        if(changedCode[i] == values[j]) {
                            changedCode[i] = j + 1;
                            break;
                        }
                    }
                }
            }

        }
        else {
            if(values[depth] != 0){
                prepareRenaming(changedCode, depth, values);
                return;
            }

            for (int i = 3; i <= N; i++) {
                for (int j = 0; j < depth; j++) {
                    if (values[j] == i) {
                        breakFlag = true;
                        break;
                    }
                }

                if (breakFlag)
                    breakFlag = false;
                else {
                    values[depth] = i;
                    prepareRenaming(changedCode, depth, values);
                    values[depth] = 0;
                }

                if (renamingRes)
                    return;
            }
        }
    }
    private boolean isRenaming(int[] changedCode){
        int[] values = new int[N];

        //т.к. у нас все коды начинаются с 12, сделаем так, что у нас переименование всегда будет делать коды, начинающиеся с 1 и 2
        //индекс - старое значение, значение - новое значение

        values[changedCode[0] - 1] = 1;
        values[changedCode[1] - 1] = 2;

        if(values[0] != 0) { //true
            prepareRenaming(changedCode, 0, values);

            if (renamingRes) {
                renamingRes = false;
                return true;
            }
            return false;
        }

        for (int i = 3; i <= N; i++) {
            values[0] = i;
            prepareRenaming(changedCode, 0, values);
            if (renamingRes) {
                renamingRes = false;
                return true;
            }
        }

        return false;
    }
    private boolean isShift(int[] newCode){
        int[] changedCode = new int[newCode.length];
        for(int i = 0; i < newCode.length; i++)
            changedCode[i] = newCode[i];

        if(isRenaming(changedCode))
            return true;

        for(int i = 1; i < newCode.length; i++){
            for(int j = 0; j < newCode.length; j++){
                changedCode[j] = newCode[(j + i) % newCode.length];
            }

            if(isRenaming(changedCode))
                return true;
        }

        /*inverse*/
        for(int i = 0; i < newCode.length; i++){
            for(int j = 0; j < newCode.length; j++){
                changedCode[j] = newCode[((newCode.length - j - 1) + i) % newCode.length];
            }

            if(isRenaming(changedCode))
                return true;
        }

        return false;
    }


    public void makeSpectors5() throws FileNotFoundException {
        int[] spector1 = {2, 2, 4, 8, 16};
        spectors.add(new Spector(spector1));
        int[] spector2 = {2, 2, 4, 10, 14};
        spectors.add(new Spector(spector2));
        int[] spector3 = {2, 2, 4, 12, 12};
        spectors.add(new Spector(spector3));
        int[] spector4 = {2, 2, 6, 6, 16};
        spectors.add(new Spector(spector4));
        int[] spector5 = {2, 2, 6, 8, 14};
        spectors.add(new Spector(spector5));
        int[] spector6 = {2, 2, 6, 10, 12};
        spectors.add(new Spector(spector6));
        int[] spector7 = {2, 2, 8, 8, 12};
        spectors.add(new Spector(spector7));
        int[] spector8 = {2, 2, 8, 10, 10};
        spectors.add(new Spector(spector8));
        int[] spector9 = {2, 4, 4, 6, 16};
        spectors.add(new Spector(spector9));
        int[] spector10 = {2, 4, 4, 8, 14};
        spectors.add(new Spector(spector10));
        int[] spector11 = {2, 4, 4, 10, 12};
        spectors.add(new Spector(spector11));
        int[] spector12 = {2, 4, 6, 6, 14};
        spectors.add(new Spector(spector12));
        int[] spector13 = {2, 4, 6, 8, 12};
        spectors.add(new Spector(spector13));
        int[] spector14 = {2, 4, 6, 10, 10};
        spectors.add(new Spector(spector14));
        int[] spector15 = {2, 4, 8, 8, 10};
        spectors.add(new Spector(spector15));
        int[] spector16 = {2, 6, 6, 6, 12};
        spectors.add(new Spector(spector16));
        int[] spector17 = {2, 6, 6, 8, 10};
        spectors.add(new Spector(spector17));
        int[] spector18 = {2, 6, 8, 8, 8};
        spectors.add(new Spector(spector18));
        int[] spector19 = {4, 4, 4, 4, 16};
        spectors.add(new Spector(spector19));
        int[] spector20 = {4, 4, 4, 6, 14};
        spectors.add(new Spector(spector20));
        int[] spector21 = {4, 4, 4, 8, 12};
        spectors.add(new Spector(spector21));
        int[] spector22 = {4, 4, 4, 10, 10};
        spectors.add(new Spector(spector22));
        int[] spector23 = {4, 4, 6, 6, 12};
        spectors.add(new Spector(spector23));
        int[] spector24 = {4, 4, 6, 8, 10};
        spectors.add(new Spector(spector24));
        int[] spector25 = {4, 4, 8, 8, 8};
        spectors.add(new Spector(spector25));
        int[] spector26 = {4, 6, 6, 6, 10};
        spectors.add(new Spector(spector26));
        int[] spector27 = {4, 6, 6, 8, 8};
        spectors.add(new Spector(spector27));
        int[] spector28 = {6, 6, 6, 6, 8};
        spectors.add(new Spector(spector28));
    }
    public void makeSpectors4() throws FileNotFoundException {
        int[] spector1 = {2, 2, 4, 8};
        spectors.add(new Spector(spector1));
        int[] spector2 = {2, 4, 4, 6};
        spectors.add(new Spector(spector2));
        int[] spector3 = {2, 2, 6, 6};
        spectors.add(new Spector(spector3));
        int[] spector4 = {4, 4, 4, 4};
        spectors.add(new Spector(spector4));
    }
    public void makeSpectors5Old() throws FileNotFoundException {
        int[] spector = new int[N];

        spector[0] = 2;
        spector[1] = 2;
        for(int i = 2; i < spector.length; i++) {
            spector[i] = spector[i - 1] * 2;
        }
        spectors.add(new Spector(spector));



        int mod = countVertexs % N;
        boolean breakFlag = true;
        int sum = 0;
        int count_fits = 0;

        while(breakFlag){
            for(int i = N - 1; i > 0; i--){
                while(spector[i] - spector[i - 1] > 2){
                    spector[i] -= 2;
                    spector[i - 1] += 2;

                    for(int j = 1; j <= spector.length; j++){
                        sum += spector[j - 1];
                        if(Math.pow(2, j) >= sum)
                            break;
                        else{
                            if(j == spector.length)
                                spectors.add(new Spector(spector));
                        }
                    }
                    sum = 0;
                }
            }

            for(int i = 0; i < mod; i++)
                if(spector[i] < countVertexs / N)
                    count_fits++;
            for(int i = mod; i < spector.length; i++)
                if(spector[i] >= countVertexs / N)
                    count_fits++;
            if(count_fits == spector.length)
                breakFlag = false;
            count_fits = 0;
        }
    }
    private void recursionToFile(int depth, int[] jumps, int[] counter, int[] spector, String code){
        if(depth == countVertexs){
            for(int i = 0; i < N; i++) {
                jumps[depth - 1] = i+1;
                if(isGamiltonov(jumps, counter)) {
                    countGamiltonov++;
                    for(int j = 0; j < spector.length; j++)
                        spector[j] = 0;
                    for(int k = 0; k < jumps.length; k++){
                        spector[jumps[k] - 1]++;
                        code = code + Integer.toString(jumps[k]);
                    }
                    Arrays.sort(spector);
                    for (Spector spectorValue : spectors) {
                        if(Arrays.equals(spector, spectorValue.getSpectorValues())) {
                            spectorValue.getFile().println(code);
                        }
                    }
                }
            }
        }
        else {
            for (int i = 0; i < N; i++) {
                jumps[depth - 1] = i + 1;
                if (checkGamilton(jumps, counter))
                    recursionToFile(depth + 1, jumps, counter, spector, code);
            }
        }

        jumps[depth - 1] = -1;
    }
    public void GamiltonCycleToFile(){
        try {
            if(N == 5)
                makeSpectors5();
            if(N == 4)
                makeSpectors4();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        int depth = 0;
        int[] jumps = new int[countVertexs];
        int[] counter = new int[N];
        int[] spector = new int[N];
        String codeStr = "";
        for(int i = 0; i < countVertexs; i++)
            jumps[i] = -1;
        for(int i = 0; i < N; i++)
            counter[i] = 0;

        jumps[0] = 1;
        jumps[1] = 2;
        depth += 2;

        recursionToFile(depth + 1, jumps, counter, spector, codeStr);

        countGamiltonov = countGamiltonov * N * (N-1);
    }


    private void parseFile(String fileInName) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(fileInName));
        int[] code = new int[countVertexs];
        String codeStr;
        String[] line;
        String name = fileInName.split(".txt")[0];
        PrintStream file = new PrintStream(name + "_codes.txt");
        while(scanner.hasNextLine()){
            codeStr = scanner.nextLine();
            line = codeStr.split("");
            for(int i = 0; i < countVertexs; i++)
                code[i] = Integer.decode(line[i]);
            if(!isRenaming(code) && !isShift(code)) {
                greysCode.add(new Code(N, code));
                file.println(codeStr);
            }
        }
        greysCode = null;
        greysCode = new ArrayList<Code>();
    }
    public void getClassesFromFile(){
        String[] filesIn = null;

        if(N == 5) {
            String[] files = { //"2_2_4_8_16.txt",
                                //"2_2_4_10_14.txt",
                                //"2_2_4_12_12.txt",
                               // "2_2_6_6_16.txt",
                                //"2_2_6_8_14.txt",
                               // "2_2_6_10_12.txt",
                               // "2_2_8_8_12.txt",
                                //"2_2_8_10_10.txt",
                                //"2_4_4_6_16.txt",
                                //"2_4_4_8_14.txt",
                                //"2_4_4_10_12.txt",
                                //"2_4_6_6_14.txt",
                                //"2_4_6_8_12.txt",
                                //"2_4_6_10_10.txt",
                                //"2_4_8_8_10.txt",
                                //"2_6_6_6_12.txt",
                                "2_6_6_8_10.txt",
                                "2_6_8_8_8.txt",
                                "4_4_4_4_16.txt",
                                "4_4_4_6_14.txt",
                                "4_4_4_8_12.txt",
                                "4_4_4_10_10.txt",
                                "4_4_6_6_12.txt",
                                "4_4_6_8_10.txt",
                                "4_4_8_8_8.txt",
                                "4_6_6_6_10.txt",
                                "4_6_6_8_8.txt",
                                "6_6_6_6_8.txt" };
            filesIn = files;
        }
        if(N == 4) {
            String[] files = { "2_2_4_8.txt",
                               "2_2_6_6.txt",
                               "2_4_4_6.txt",
                               "4_4_4_4.txt" };
            filesIn = files;
        }

        if(filesIn != null) {
            for (String s : filesIn) {
                try {
                    parseFile(s);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private int[] build2way(int[] code){ // b1 N b2 N b3 N b4 N ... но начинается с 12
        int[] newCode = new int[countVertexs];
        for(int i = 0;  i < countVertexs; i++){
            if(i % 2 == 0) {
                if(code[i / 2] == 2)
                    newCode[i] = N;
                else
                    newCode[i] = code[i / 2];
            }
            else
                newCode[i] = 2;
        }

        return newCode;
    }
    private int[] build1way(int[] code){ //B N ~B N
        int[] newCode = new int[countVertexs];
        for(int i = 0; i < countVertexs / 2 - 1; i++){
            newCode[i] = code[i];
        }
        newCode[countVertexs / 2 - 1] = N;
        for(int i = countVertexs / 2; i < countVertexs - 1; i++){
            newCode[i] = newCode[countVertexs - i - 2];
        }
        newCode[countVertexs - 1] = N;

        return newCode;
    }
    public void buildNplus1(LittleCube cubeN){
        int[] bigCode;
        int[] littleCode;
        int[] littleCodeForWrite = new int[countVertexs / 2];

        for (Code code : cubeN.getGreysCodes()) {
            littleCode = code.getCode();

            bigCode = build1way(littleCode);
            if(!isShift(bigCode))
                greysCode.add(new Code(N, bigCode));


            for(int i = 1; i < littleCode.length; i++){
                for(int j = 0; j < littleCode.length; j++){
                    littleCodeForWrite[j] = littleCode[(j + i) % littleCode.length];
                }

                bigCode = build1way(littleCodeForWrite);
                if(!isShift(bigCode))
                    greysCode.add(new Code(N, bigCode));
            }

            for(int i = 0; i < littleCode.length; i++){
                for(int j = 0; j < littleCode.length; j++){
                    littleCodeForWrite[j] = littleCode[((littleCode.length - j - 1) + i) % littleCode.length];
                }

                bigCode = build1way(littleCodeForWrite);
                if(!isShift(bigCode))
                    greysCode.add(new Code(N, bigCode));
            }


            bigCode = build2way(littleCode);
            if(!isShift(bigCode))
               greysCode.add(new Code(N, bigCode));
        }
    }
}
//сдвигаем, переименовываем, сравниваем
//ручками составь проверки для табличек
//потом в клетках категории(после этой таблички)
//потом к половинчатая
