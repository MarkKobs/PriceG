package com.example.guessprice;

import java.util.Random;

public class Guess {
    private Random random;
    public Guess(){super();random=new Random();}
//    public int getRandomNum(){
//        return random.nextInt(100);
//    }
    public int judge(int n,int num){
        if(n>num){
            return 1;
        }
        else if(n==num){
            return 0;
        }
        else{
            return -1;
        }
    }
    public String play(int num,int n){
        if(judge(num,n)==0){
            return "猜对了!";
        }
        else if(judge(num,n)==1){
            return "猜小了";
        }
        else{
            return "猜大了";
        }
    }
}
