
package com.asgteach.model;

import com.asgteach.modelview.LetterStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameStatus {
    
    private static GameStatus instance = null;
    
    private List<LetterState>letterState = new ArrayList<>();
    private Map<String, LetterStyle.DisplayType> keyBoardState = new HashMap<>();
    private final WordStats wordStats = new WordStats();
    
    private GameStatus() {}
    
    public static GameStatus getInstance() {
        if (instance == null) {
            instance = new GameStatus();
        }
        return instance;
    }

    public List<LetterState> getLetterState() {
        return letterState;
    }

    public Map<String, LetterStyle.DisplayType> getKeyBoardState() {
        return keyBoardState;
    }
    
    public WordStats getWordStats() {
        return this.wordStats;
    }

    public void setLetterState(List<LetterState> letterState) {
        this.letterState = letterState;
    }

    public void setKeyBoardState(Map<String, LetterStyle.DisplayType> keyBoardState) {
        this.keyBoardState = keyBoardState;
    }
       
}


