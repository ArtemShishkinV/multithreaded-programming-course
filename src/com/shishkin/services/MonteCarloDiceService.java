package com.shishkin.services;

import com.shishkin.handlers.MonteCarloDiceThread;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class MonteCarloDiceService {
    private final List<MonteCarloDiceThread> controlThreads = new ArrayList<>();
    private int lastId;


    public void await(String arg) throws NoSuchElementException {
        int id = Integer.parseInt(arg);
        MonteCarloDiceThread controlThread = getThreadById(id);
        try {
            controlThread.join(TimeUnit.MINUTES.toMillis(1));
        } catch (InterruptedException e) {
            System.out.println("join " + controlThread.getThreadId() + " interrupted");
            controlThread.interrupt();
        }
    }

    public void exit() {
        controlThreads.forEach(Thread::interrupt);
    }

    public void stop(String arg) throws NoSuchElementException {
        int id = Integer.parseInt(arg);
        MonteCarloDiceThread t = getThreadById(id);
        t.interrupt();
        controlThreads.remove(t);
    }

    public int start(String arg) {
        double eps = Double.parseDouble(arg);
        MonteCarloDiceThread t = new MonteCarloDiceThread(lastId, eps);
        controlThreads.add(t);
        t.start();
        lastId++;
        return t.getThreadId();
    }

    private MonteCarloDiceThread getThreadById(int id) throws NoSuchElementException{
        return controlThreads.stream().filter(t -> t.getThreadId() == id)
                .findAny()
                .orElseThrow();
    }
}
