package com.crawler.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LVY on 2015/10/27.
 */
public class Steper {

    private static List<Integer> stepers = new ArrayList<Integer>();

    public static void InitStepers(int num){
        for (int i = 0; i < num ; i++) {
           stepers.add(-1);
        }
    }

    public static int getNext(int index){
        synchronized (Steper.class){
            if (index > -1 && index < stepers.size()){
                int num = stepers.get(index);
                num++;
                stepers.set(index,num);
                return num;
            }
            return -1;
        }
    }

    public static void Reset(int index, int start){
        synchronized (Steper.class){
            if (index > -1 && index < stepers.size()){
                stepers.set(index,start);
            }
        }
    }



}
