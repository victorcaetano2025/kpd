package com.example.kpd.utils;

public class Tempo {
    private long start;

    public void start() {
        start = System.nanoTime();
    }

    public double stops() {
    long end = System.nanoTime();
     return (end - start) / 1_000_000_000.0;
    }
  }   
