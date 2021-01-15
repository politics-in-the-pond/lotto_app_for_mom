package com.myapp.lotto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    final int N = 624; // MT19937_32 initialization
    final int M = 397;
    final long MATRIX_A = 0x9908b0dfL;
    final long UMASK = 0x80000000L;
    final long LMASK = 0x7fffffffL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity MA = new MainActivity();
        TextView Textv = (TextView) findViewById(R.id.textfield);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] numbers = MA.make_numbers();
                String number_string = numbers[0] + "  " + numbers[1] + "  " + numbers[2] + "  " + numbers[3] + "  " + numbers[4] + "  " + numbers[5];
                Textv.setText(number_string);
            }
        };

        Button btn_gen = (Button) findViewById(R.id.numgen);
        btn_gen.setOnClickListener(listener);
    }

    private int[] make_numbers(){
        int[] numbers = new int[6];
        int count = 0;
        boolean chk = false;
        int i;
        long seed = System.currentTimeMillis();
        for(i = 0; i < 6; i++)
            numbers[i] = 0;
        while(true){
            if(count == 6) break;
            chk = false;
            if(numbers[count]==0){
                int random = (int) MT19937_long(seed);
                seed = random;
                if(random<0) random*=-1;
                int random45 = random%45+1;
                for(i = 0; i < 6; i++){
                    if(numbers[i]==0) break;
                    if(numbers[i]==random45){
                        chk = true;
                        break;
                    }
                }
                if(chk == true) continue;
                numbers[count] = random45;
                count++;
            }
        }
        Arrays.sort(numbers);
        return numbers;
    }

    /*
     * Copyright (C) 1997 - 2002, Makoto Matsumoto and Takuji Nishimura, All rights
     * reserved. Redistribution and use in source and binary forms, with or without
     * modification, are permitted provided that the following conditions are met:
     * 1. Redistributions of source code must retain the above copyright notice,
     * this list of conditions and the following disclaimer. 2. Redistributions in
     * binary form must reproduce the above copyright notice, this list of
     * conditions and the following disclaimer in the documentation and/or other
     * materials provided with the distribution. 3. The names of its contributors
     * may not be used to endorse or promote products derived from this software
     * without specific prior written permission. THIS SOFTWARE IS PROVIDED BY THE
     * COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
     * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
     * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
     * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
     * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
     * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
     * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
     * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
     * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
     * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
     */

    public long MT19937_long(long seed) { // 메르센 트위스터 구현부. unsigned형이 없어 원본의 MT19937과는 결과값이 다를 수도 있을 것으로 예상.

        long mt[] = new long[N];
        int mti = N + 1;

        long s = seed;
        mt[0] = s & 0xFFFFFFFF;
        for (mti = 1; mti < N; mti++) {
            mt[mti] = (0x6C078965 * (mt[mti - 1] ^ (mt[mti - 1] >> 30)) + mti);
        }

        long y;
        long mag01[] = new long[2];
        mag01[0] = 0x0L;
        mag01[1] = MATRIX_A;

        if (mti >= N) {
            int kk;

            for (kk = 0; kk < N - M; kk++) {
                y = (mt[kk] & UMASK) | (mt[kk + 1] & LMASK);
                mt[kk] = mt[kk + M] ^ (y >> 1) ^ mag01[(int) (y & 0x1L)];
            }

            for (; kk < N - 1; kk++) {
                y = (mt[kk] & UMASK) | (mt[kk + 1] & LMASK);
                mt[kk] = mt[kk + (M - N)] ^ (y >> 1) ^ mag01[(int) (y & 0x1L)];
            }

            y = (mt[N - 1] & UMASK) | (mt[0] & LMASK);
            mt[N - 1] = mt[M - 1] ^ (y >> 1) ^ mag01[(int) (y & 0x1L)];

            mti = 0;
        }

        y = mt[mti++];

        y ^= (y >> 11);
        y ^= (y >> 7) & 0x9D2C6780L;
        y ^= (y >> 15) & 0xEFC60000L;
        y ^= (y >> 18);

        return y;
    }
}