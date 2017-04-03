package com.mariolopezjr.tetris.controller;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Provides a class a tick at the specified time interval
 */
public class Ticker implements Runnable {

    private static Logger log = LoggerFactory.getLogger(Ticker.class);

    private AtomicLong intervalInMilliseconds = new AtomicLong();;
    private AtomicBoolean shouldContinueRunning = new AtomicBoolean(false);
    private TickListener tickListener;
    private long numberOfTicks;
    private long maxNumberOfTicks = 1000000000;
    private Thread thread;

    /**
     * Constructor
     */
    public Ticker() {
        // do nothing
    }

    public Ticker setTickListener(final TickListener tickListener) {
        this.tickListener = tickListener;
        return this;  // convenience
    }

    /**
     * Set the tick interval in milliseconds
     * @param milliseconds long
     */
    public Ticker setInterval(final long milliseconds) {
        log.debug("Setting interval to: {}", milliseconds);
        this.intervalInMilliseconds.set(milliseconds);
        return this;  // convenience
    }

    public void start() {
        if (this.tickListener == null || this.intervalInMilliseconds.get() == 0) {
            throw new RuntimeException("Attempting to start uninitialized ticker.");
        }

        log.debug("Starting ticker");
        shouldContinueRunning.set(true);
        numberOfTicks = 0;
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        if (thread != null) thread.interrupt();
        shouldContinueRunning.set(false);
    }

    public boolean isRunning() {
        return shouldContinueRunning.get();
    }

    public void run() {
        log.debug("Ticker running");
        while (shouldContinueRunning.get()) {
            try {
                this.tickListener.tick();
                numberOfTicks++;

                /*if (numberOfTicks > maxNumberOfTicks) {
                    String stackTrace = Arrays.toString(
                            Thread.getAllStackTraces().get(Thread.currentThread()));

                    log.warn("Number of ticks exceeded max.  Runaway ticker?\n" + stackTrace);
                    numberOfTicks = 0;
                }*/

                Thread.sleep(this.intervalInMilliseconds.get());
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }
}
