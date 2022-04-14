package com.asgteach.modelview;

import com.asgteach.modelview.LetterStyle.DisplayType;
import static com.asgteach.modelview.LetterStyle.DisplayType.PLAIN;
import static com.asgteach.modelview.LetterStyle.MATCHING_PSEUDO_CLASS;
import static com.asgteach.modelview.LetterStyle.NOMATCH_PSEUDO_CLASS;
import static com.asgteach.modelview.LetterStyle.PARTIALMATCH_PSEUDO_CLASS;
import static com.asgteach.modelview.LetterStyle.PLAIN_PSEUDO_CLASS;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;

public class KeyButton extends Button {

    public KeyButton() {
        super();
        getStyleClass().add("matching-letter");
        getStyleClass().add("nomatch-letter");
        getStyleClass().add("partialmatch-letter");
        getStyleClass().add("plain-letter");
    }
    
    private final ObjectProperty<DisplayType> letterDisplay = new SimpleObjectProperty<LetterStyle.DisplayType>(PLAIN) {
        @Override
        public void invalidated() {
            // Turn off all of the custom PseudoClasses in the LetterLabel, 
            // and then uses a switch statement to turn on a 
            // specific PseudoClass depending on the value 
            // in the labelDisplay property
            pseudoClassStateChanged(PLAIN_PSEUDO_CLASS, false);
            pseudoClassStateChanged(NOMATCH_PSEUDO_CLASS, false);
            pseudoClassStateChanged(PARTIALMATCH_PSEUDO_CLASS, false);
            pseudoClassStateChanged(MATCHING_PSEUDO_CLASS, false);
            switch (get()) {
                case PLAIN -> pseudoClassStateChanged(PLAIN_PSEUDO_CLASS, true);
                case NOMATCH -> pseudoClassStateChanged(NOMATCH_PSEUDO_CLASS, true);
                case PARTIALMATCH -> pseudoClassStateChanged(PARTIALMATCH_PSEUDO_CLASS, true);
                case MATCHING -> pseudoClassStateChanged(MATCHING_PSEUDO_CLASS, true);
                default -> pseudoClassStateChanged(PLAIN_PSEUDO_CLASS, true);
            }
        }
    };
    
    public void setLetterDisplay(LetterStyle.DisplayType labelType) {
        // this invokes invalided()
        this.letterDisplay.set(labelType);
    }

    public LetterStyle.DisplayType getLetterDisplay() {
        return letterDisplay.get();
    }
    

}
