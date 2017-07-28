package ru.nsu.fit.g14201.chernova.Cube;

import java.util.ArrayList;

/**
 * Created by Катя on 11.10.2016.
 */
public class Code {
    private int N;
    private int[] code;
    private ArrayList<Data> paramsK = new ArrayList<Data>();
    private int count = 1;

    class Data{
        private int k;
        private int d1;
        private int d2;

        public Data(int k, int d1, int d2){
            this.k = k;
            this.d1 = d1;
            this.d2 = d2;
        }
        public void setK(int other){
            k = other;
        }
        public  void setD(int other1, int other2){
            d1 = other1;
            d2 = other2;
        }
        public int getK(){
            return k;
        }
        public int getD1(){
            return d1;
        }
        public int getD2(){
            return d2;
        }
    }

    public Code(int N, int[] jumps){
        this.N = N;

        int[] nums = new int[N];
        code = new int[jumps.length];
        for(int i = 0; i < jumps.length; i++) {
            code[i] = jumps[i];
            nums[jumps[i] - 1]++;
        }
    }

    public int[] getCode(){
        return code;
    }
    public ArrayList<Data> getParamsK(){ return paramsK; }

    private int[] checkHemming(int d1, int d2, int val1, int val2){
        int[] d = new int[2];
        if(d1 == -2){ //d1, d2 not define
            d[0] = val1;
            d[1] = val2;
        }
        else { // d1, d2 are values
                if(val1 != d1 && val2 != d1)
                    d[0] = -1;
                else
                    d[0] = d1;
                if(val1 != d2 && val2 != d2)
                    d[1] = -1;
                else
                    d[1] = d2;

        }
        return d;
    }
    private Data searchHemming(int k){
        int[] d = new int[2];
        d[0] = -2;
        d[1] = -2; // -2 - init, -1 - not exist
        int val1 = 0, val2 = 0;
        int pos = 0;
        int[] nums1 = new int[N];
        int[] nums2 = new int[N];
        for(int i = 0; i < N; i++) {
            nums1[i] = 0;
            nums2[i] = 0;
        }

        for(int i = 0; i < code.length; i++){//идем по всем кодам
            for(int j = 1; j <= k; j++){
                pos = code[(i + j - 1)%code.length] - 1;//вперед
                nums1[pos] = (nums1[pos] + 1) % 2;
                pos = code[(i-j + code.length)%code.length] - 1;//назад
                nums2[pos] = (nums2[pos] + 1) % 2;
            }
            for(int m = 0; m < N; m++) {
                val1 = val1 + nums1[m];
                val2 = val2 + nums2[m];
                nums1[m] = 0;
                nums2[m] = 0;
            }
            d = checkHemming(d[0], d[1], val1, val2);
            if(d[0] == -1 && d[1] == -1){
                break;
            }
            val1 = 0;
            val2 = 0;
        }

        return new Data(k, d[0], d[1]);
    }
    public void calculateHemmingDistance(){
        paramsK.add(new Data(0, 0, 0));
        paramsK.add(new Data(1, 1, 1));
        paramsK.add(new Data(2, 2, 2));

/*
        for(int i = 3; i <= code.length; i++){
            paramsK.add(searchHemming(i));
        }
*/

        for(int i = 3; i <= code.length / 2; i++){
            paramsK.add(searchHemming(i));
        }

        for(int i = code.length / 2 + 1; i <= code.length; i++)
            paramsK.add(new Data(i, paramsK.get(code.length - i).d1, paramsK.get(code.length - i).d2));
    }
    public void printParams(){
        for(int i = 0; i < code.length; i++)
            System.out.print(code[i]);
        System.out.println("");

        for (Data data : paramsK) {
            System.out.printf("K: %d; D1: %s; D2: %s\n", data.k, (data.d1 == -1 ? "-" : ((Integer)data.d1).toString()), (data.d2 == -1 ? "-" : ((Integer)data.d2).toString()));
        }
    }

    public int getCount(){ return count; }
    public void addCode(){ count++; }
    public void setCount(int newCount) { count = newCount; }



    private Data searchHemmingFromVolodya(int k){
        int[] d = new int[2];
        d[0] = -2;
        d[1] = -2; // -2 - init, -1 - not exist
        int val1 = 0, val2 = 0;

        /*
         *  v1 : [0, 0, 0]    vk : [0, 1, 0]
         *  posJump1          posJumpk
         *          hemmingDistance
         *  jump1 = jumps[posJump1], jumpk = jumps[posJumpk]
         *
         *  jump1 == jumpk && v1[jump1] == vk[jumpk]
         *                      : newHemming = hemmingDistance
         *  jump1 == jumpk && v1[jump1] != vk[jumpk]
         *                      : newHemming = hemmingDistance
         *
         *  jump1 != jumpk && a1 == ak && b1 == bk
         *                      : newHemming = hemmingDistance + 2
         *  jump1 != jumpk && a1 == ak && b1 != bk
         *                      : newHemming = hemmingDistance
         *  jump1 != jumpk && a1 != ak && b1 == bk
         *                      : newHemming = hemmingDistance
         *  jump1 != jumpk && a1 != ak && b1 != bk
         *                      : newHemming = hemmingDistance - 2
         *
         *  v1, vk, hemmingDistance;
         * --------------------------------------------------------
         *  posJump1 = i;
         *  posJumpk = (i + k) % n;
         *
         *  m0 = (jump1 != jumpk) ? 1 : 0;
         *  m1 = (v1[jump1] == vk[jump1]) ? 1 : 0;      // a1 == ak
         *  m2 = (v1[jumpk] == vk[jumpk]) ? 1 : 0;      // b1 == bk
         *  m3 = ((m1 == m2) ? 1 : 0) * (m1 + m2 - 1);
         *  hemmingDistance = hemmingDistance + m0 * m3 * 2;
         *
         *  v1[jump1] = (v1[jump1] + 1) % 2;
         *  vk[jumpk] = (vk[jumpk] + 1) % 2;
         */

        for(int i = 0; i < code.length; i++){//идем по всем кодам
         /*   for(int j = 1; j <= k; j++){
                nums1[code[(i+j)%code.length] - 1] = (nums1[code[(i+j)%code.length] - 1] + 1)%2;
                nums2[code[(i-j + code.length)%code.length] - 1] = (nums2[code[(i-j + code.length)%code.length] - 1] + 1)%2;
            }
            for(int m = 0; m < N; m++) {
                val1 = val1 + nums1[m];
                val2 = val2 + nums2[m];
                nums1[m] = 0;
                nums2[m] = 0;
            }*/
            d = checkHemming(d[0], d[1], val1, val2);
            if(d[0] == -1 && d[1] == -1){
                break;
            }
            val1 = 0;
            val2 = 0;
        }

        return new Data(k, d[0], d[1]);
    }
}
