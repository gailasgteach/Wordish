package com.asgteach.model;

import com.asgteach.modelview.LetterStyle.DisplayType;

//public class LetterState {
//    private DisplayType displayType;
//    private String letter;
//    
//    public LetterState(DisplayType displayType, String letter) {
//        this.displayType = displayType;
//        this.letter = letter;        
//    }
//
//    public DisplayType displayType() {
//        return displayType;
//    }
//
//    public void setDisplayType(DisplayType displayType) {
//        this.displayType = displayType;
//    }
//
//    public String letter() {
//        return letter;
//    }
//
//    public void setLetter(String letter) {
//        this.letter = letter;
//    }
//    
//    
//}

public record LetterState(DisplayType displayType, String letter) {
    
}
