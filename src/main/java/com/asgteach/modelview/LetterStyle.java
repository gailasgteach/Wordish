
package com.asgteach.modelview;

import javafx.css.PseudoClass;

public class LetterStyle {
    public static final PseudoClass PLAIN_PSEUDO_CLASS = PseudoClass.getPseudoClass("plain");
    public static final PseudoClass DISPLAYING_PSEUDO_CLASS = PseudoClass.getPseudoClass("displaying");
    public static final PseudoClass NOMATCH_PSEUDO_CLASS = PseudoClass.getPseudoClass("nomatch");
    public static final PseudoClass PARTIALMATCH_PSEUDO_CLASS = PseudoClass.getPseudoClass("partialmatch");
    public static final PseudoClass MATCHING_PSEUDO_CLASS = PseudoClass.getPseudoClass("matching");

    public enum DisplayType {
        PLAIN, DISPLAYING, NOMATCH, PARTIALMATCH, MATCHING;
    }
    
}
