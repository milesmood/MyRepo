package com.example.learningenglish.models;

import java.util.BitSet;
import java.util.Random;


public class RandomNumb {

        private final BitSet input;
        private final Random rnd;
        private final int count;
        private int genCount=0;

        public RandomNumb(int in)
        {
            count=in;
            rnd=new Random(in);
            input = new BitSet(in);

        }
        public void resetInput(){
            input.clear();
        }

        public int generate() {
            if (genCount>= this.count)
                return -1;
            int next;
            do {
                next = rnd.nextInt(this.count)+1;
            }
            while (input.get(next));
            input.set(next);
            genCount++;
            return next;
        }
}
