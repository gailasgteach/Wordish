package com.asgteach.model;

public class WordTally {
    
    String guess;
    String target;
    
    public WordTally() {
        this.guess = "";
        this.target = "";
    }

    public WordTally(String guess, String target) {
        this.guess = guess;
        this.target = target;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }    
}
