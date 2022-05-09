package com.asgteach;

import com.asgteach.model.GameStatus;
import com.asgteach.model.LetterState;
import com.asgteach.model.WordStats;
import com.asgteach.model.WordTally;
import com.asgteach.modelview.KeyButton;
import com.asgteach.modelview.LetterLabel;
import com.asgteach.modelview.LetterStyle;
import com.asgteach.modelview.LetterStyle.DisplayType;
import static com.asgteach.modelview.LetterStyle.DisplayType.MATCHING;
import static com.asgteach.modelview.LetterStyle.DisplayType.NOMATCH;
import static com.asgteach.modelview.LetterStyle.DisplayType.PARTIALMATCH;
import com.asgteach.modelview.WordPopup;
import com.asgteach.service.WordData;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class WordishController {

    @FXML
    private Button resetButton;
    @FXML
    private Button statsButton;
    @FXML
    private Button infoButton;
    @FXML
    private KeyButton enterButton;
    @FXML
    private KeyButton deleteButton;
    @FXML
    private TilePane letterTilePane;
    @FXML
    private FlowPane buttonFlowPane;

    private List<LetterLabel> letters;
    private Map<String, KeyButton> keyLetters;
    private final List<String> messages = Arrays.asList(
            "W O W . . . Did you cheat?",
            "G E N I U S",
            "I M P R E S S I V E",
            "S P L E N D I D",
            "G R E A T",
            "P H E W");
    private final int ROW_FILLED = 5;
    private int rownum = 0;
    private final IntegerProperty squarenum = new SimpleIntegerProperty(0);
    private final BooleanProperty processingWord = new SimpleBooleanProperty(false);
    private final BooleanProperty gameOver = new SimpleBooleanProperty(false);
    private final BooleanProperty gameReset = new SimpleBooleanProperty(true);

    private static final WordData wordData = WordData.getInstance();
    private static final GameStatus gameStatus = GameStatus.getInstance();
    WordTally wordTally = new WordTally();
    private int rowSize;

    public void initialize() {
        // manually configure the grphic elements for the buttons
//        resetButton.setText(new String(Character.toChars(Integer.parseInt("F0459", 16))));
//        resetButton.setStyle("-fx-font-family: \"Material Design Icons\"; -fx-font-size: 28");
        
        //<FontIcon iconColor="darkblue" iconLiteral="mdi2i-information-variant" iconSize="28" />
//        infoButton.setText(new String(Character.toChars(Integer.parseInt("F064E", 16))));
//        infoButton.setStyle("-fx-font-family: \"Material Design Icons\"; -fx-font-size: 28");
        
        //<FontIcon iconColor="darkblue" iconLiteral="mdi2c-chart-box-outline" iconSize="28" />
//        statsButton.setText(new String(Character.toChars(Integer.parseInt("F154E", 16))));
//        statsButton.setStyle("-fx-font-family: \"Material Design Icons\"; -fx-font-size: 28");
        
        //<FontIcon iconLiteral="mdi2b-backspace-outline" iconSize="24" />
//        deleteButton.setText(new String(Character.toChars(Integer.parseInt("F0B5C", 16))));
//        deleteButton.setStyle("-fx-font-family: \"Material Design Icons\"; -fx-font-size: 24");
        
        letterTilePane.setAlignment(Pos.TOP_CENTER);
        letterTilePane.setPrefRows(6);
        letters = letterTilePane.getChildren()
                .stream()
                .map(LetterLabel.class::cast)
                .collect(Collectors.toList());
        rowSize = letterTilePane.getPrefColumns();
        keyLetters = buttonFlowPane.getChildren()
                .stream()
                .map(KeyButton.class::cast)
                .filter(button -> button.getText().length() == 1)
                .collect(Collectors.toMap(KeyButton::getText, Function.identity()));
        
        //resetButton.disableProperty().bind(gameReset.not());
        statsButton.disableProperty().bind(gameReset.not());
        infoButton.disableProperty().bind(gameReset.not());
        
        enterButton.disableProperty().bind(squarenum.lessThan(ROW_FILLED)
                .or(gameOver));
        deleteButton.disableProperty().bind(squarenum.isEqualTo(0)
                .or(gameOver));
        keyLetters.values()
                .stream()
                .forEach(button -> button.disableProperty()
                .bind(processingWord
                        .or(squarenum.isEqualTo(ROW_FILLED))
                        .or(gameOver)));
        
        if (gameStatus.getWordStats().getGamesPlayed() > 0
                && gameReset.get()) {
            restoreGame();
        } else {
            doResetGame();
        }
    }

// TODO:    
//    private void saveAsPng() {
//        letters.stream().forEach(ll -> ll.setText(""));
//        WritableImage image = letterTilePane.snapshot(new SnapshotParameters(), null);
//
//        // TODO: probably use a file chooser here
//        File file = new File("wordish.png");
//
//        try {
//            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//        } catch (IOException e) {
//            System.out.println("File " + file.getAbsolutePath() + " || " + e.getLocalizedMessage());
//        }
//    }

    private void updateGameState(boolean win) {
        gameOver.set(true);
        
        WordStats ws = gameStatus.getWordStats();
        ws.setGamesPlayed(ws.getGamesPlayed() + 1);
        if (win) {
            System.out.println("WIN!");
            ws.setTotalWins(ws.getTotalWins() + 1);
            ws.setCurrentStreak(ws.getCurrentStreak() + 1);
            ws.setMaxStreak((ws.getCurrentStreak() > ws.getMaxStreak())
                    ? ws.getCurrentStreak() : ws.getMaxStreak());
            Integer key = rownum + 1;
            System.out.println("Guess win is " + key + " tries.");
            // Only store the guess distribution when you win
            ws.getGuessDistribution().put(key, ws.getGuessDistribution().get(key) + 1);
            ws.setThisGameGuesses(key);
        } else {
            System.out.println("LOSE!");
            ws.setCurrentStreak(0);
            ws.setThisGameGuesses(0);
        }

        // update with scene graph nodes
        gameStatus.setLetterState(letters.stream()
                .map(ll -> new LetterState(ll.getLetterDisplay(), ll.getText()))
                .collect(Collectors.toList()));
        gameStatus.setKeyBoardState(keyLetters.values().stream()
                .collect(Collectors.toMap(
                        KeyButton::getText, KeyButton::getLetterDisplay)));
        gameReset.set(true);
    }

    @FXML
    private void resetGame(ActionEvent event) {
        gameReset.set(false);
        doResetGame();
    }

    private void restoreGame() {
        List<LetterState> savedLetters = gameStatus.getLetterState();
        Map<String, LetterStyle.DisplayType> savedKeys = gameStatus.getKeyBoardState();
        IntStream.range(0, savedLetters.size())
                .forEach(i -> {
                    letters.get(i).setText(savedLetters.get(i).letter());
                    letters.get(i).setMatchResult(savedLetters.get(i).displayType());
                    letters.get(i).setLetterDisplay(savedLetters.get(i).displayType());
                });

        savedKeys.keySet().forEach(key -> {
            keyLetters.get(key).setLetterDisplay(savedKeys.get(key));
        });
        //gameOver.set(true);
        processingWord.set(true);
        squarenum.set(0);
    }

    private void doResetGame() {
        // before resetting the game, save it to wordish.png
//        if (WordishApp.getGameStatus().getWordStats().getGamesPlayed() > 0) {
//            System.out.println("Saving to file");
//            saveAsPng();
//        }

        wordData.setNewWord();
        gameOver.set(false);
        processingWord.set(false);
        squarenum.set(0);
        rownum = 0;
        letters.stream()
                .forEach(ll -> {
                    ll.setText("");
                    ll.setLetterDisplay(DisplayType.PLAIN);
                });
        keyLetters.values()
                .stream()
                .forEach(ll -> {
                    ll.setLetterDisplay(DisplayType.DISPLAYING);
                });
        // update with scene graph nodes
        gameStatus.setLetterState(letters.stream()
                .map(ll -> new LetterState(ll.getLetterDisplay(), ll.getText()))
                .collect(Collectors.toList()));
        gameStatus.setKeyBoardState(keyLetters.values().stream()
                .collect(Collectors.toMap(
                        KeyButton::getText, KeyButton::getLetterDisplay)));
    }

    @FXML
    private void switchToStats() throws IOException {
        WordishApp.setRoot("stats");
    }

    @FXML
    private void selectLetter(ActionEvent event) {
        String letter = ((KeyButton) event.getSource()).getText();
        doSelectLetter(letter);
    }

    private void doSelectLetter(String letter) {
        gameReset.set(false);
        int index = squarenum.get() + (rowSize * rownum);
        keyLetters.get(letter).requestFocus();
        letters.get(index).setText(letter);
        letters.get(index).setLetterDisplay(DisplayType.DISPLAYING);
        squarenum.set(squarenum.get() + 1);
        if (squarenum.get() >= ROW_FILLED) {
            infoButton.getScene().getRoot().requestFocus();
        }
    }

    @FXML
    private void processEnter(ActionEvent event) {
        doProcessEnter();
    }

    private void doProcessEnter() {
        processingWord.set(true);
        gameReset.set(false);
        List<LetterLabel> workingLetters
                = letters.stream()
                        .skip(rownum * rowSize)
                        .limit(rowSize)
                        .collect(Collectors.toList());
        processWord(workingLetters);
    }

    private void processWord(List<LetterLabel> list) {
        // Is it a valid word?
        String guess = list.stream().map(e -> e.getText()).reduce("", String::concat);
        wordTally.setGuess(guess);
        wordTally.setTarget(wordData.getTheWord());

        if (!wordData.isAWord(guess)) {
            // bad word
            WordPopup.show("Word is not in the valid word list.", enterButton);
            animateBadWord(list);
            processingWord.set(false);
            return;
        }
        doProcessMatching(list);
        doProcessPartial(list);
        doProcessNoMatch(list);
        animateLabelGroup(list);
    }

    private void animateBadWord(List<LetterLabel> list) {
        ParallelTransition para = new ParallelTransition();
        list.stream()
                .forEach(ll -> {
                    TranslateTransition translate1 = new TranslateTransition(
                            Duration.millis(100), ll);
                    translate1.setByX(20);
                    translate1.setAutoReverse(true);
                    translate1.setCycleCount(6);
                    para.getChildren().add(translate1);
                });
        para.play();
    }

    private void doProcessMatching(List<LetterLabel> list) {
        list.stream()
                .filter(ll -> wordTally.getGuess().charAt(list.indexOf(ll)) == wordTally.getTarget().charAt(list.indexOf(ll)))
                .forEach(ll -> {
                    ll.setMatchResult(MATCHING);
                    int index = list.indexOf(ll);                   
                    wordTally.setGuess(wordTally.getGuess().substring(0, index) + 
                            "-" + wordTally.getGuess().substring(index + 1));
                    wordTally.setTarget(wordTally.getTarget().substring(0, index) + 
                            "-" + wordTally.getTarget().substring(index + 1));
                });        
    }

    private void doProcessPartial(List<LetterLabel> list) {
        list.stream()
                .filter(ll -> wordTally.getGuess().charAt(list.indexOf(ll)) != '-')
                .forEach(ll -> {
                    char c = wordTally.getGuess().charAt(list.indexOf(ll));
                    if (wordTally.getTarget().contains(Character.toString(c))) {    
                        ll.setMatchResult(PARTIALMATCH);
                        int index = wordTally.getTarget().indexOf(c);
                        int index2 = list.indexOf(ll);
                        wordTally.setTarget(wordTally.getTarget().substring(0, index) + 
                                "-" + wordTally.getTarget().substring(index + 1));
                        wordTally.setGuess(wordTally.getGuess().substring(0, index2) + 
                                "-" + wordTally.getGuess().substring(index2 + 1));
                    }
                });        
    }

    private void doProcessNoMatch(List<LetterLabel> list) {
        list.stream()
                .filter(ll -> wordTally.getGuess().charAt(list.indexOf(ll)) != '-')
                .forEach(ll -> ll.setMatchResult(NOMATCH));
    }

    private void animateLabelGroup(List<LetterLabel> list) {
        SequentialTransition seq = new SequentialTransition();
        list.stream()
                .forEach(ll -> {
                    String letterText = ll.getText();
                    FadeTransition fade = new FadeTransition(
                            Duration.millis(100), ll);
                    fade.setAutoReverse(true);
                    fade.setCycleCount(2);
                    fade.setToValue(0);

                    fade.setDelay(Duration.millis(20));
                    fade.setOnFinished(x -> {
                        ll.setText("");
                        ll.setLetterDisplay(ll.getMatchResult());
                    });
                    
                    RotateTransition rotate = new RotateTransition(
                            Duration.millis(400), ll);
                    rotate.setAxis(Rotate.X_AXIS);
                    rotate.setByAngle(180);
                    rotate.setOnFinished(x -> {
                        ll.setRotate(0);
                        ll.setText(letterText);
                    });
                    
                    ParallelTransition para = new ParallelTransition();
                    para.getChildren().addAll(fade, rotate);
                    seq.getChildren().add(para);
                });
        seq.setOnFinished(x -> gameHouseKeeping(list));
        seq.play();
    }

    private void gameHouseKeeping(List<LetterLabel> list) {
        processingWord.set(false);
        list.stream()
                .forEach(ll -> 
                    keyLetters.get(ll.getText()).setLetterDisplay(ll.getMatchResult())
                );

        if ((list.stream()
                .filter(c -> !c.getMatchResult().equals(MATCHING))
                .count() == 0)) {   //match!
            System.out.println("Winner! You guessed the word " + wordData.getTheWord());
            WordPopup.show(messages.get(rownum), enterButton);
            animateSuccessGroup(list);
            updateGameState(true);
        } else if (rownum + 1 >= letterTilePane.getPrefRows()) {
            // You took too many guesses
            WordPopup.show("Sorry! You lose this time!\nThe word is " + wordData.getTheWord(), enterButton);
            updateGameState(false);
        } else // Update letter grid
        if (squarenum.get() >= rowSize) {
            squarenum.set(0);
            rownum++;
            if (rownum >= letterTilePane.getPrefRows()) {
                rownum = 0;
            }
        }
    }

    private void animateSuccessGroup(List<LetterLabel> list) {
        SequentialTransition seq = new SequentialTransition();
        list.stream()
                .forEach(ll -> {
                    RotateTransition rotate = new RotateTransition(
                            Duration.millis(100), ll);
                    rotate.setAxis(Rotate.Z_AXIS);
                    rotate.setByAngle(60);
                    rotate.setAutoReverse(true);
                    rotate.setCycleCount(4);
                    seq.getChildren().add(rotate);
                });
        seq.setDelay(Duration.millis(100));
        seq.play();
    }

    @FXML
    private void processDelete(ActionEvent event) {
        doProcessDelete();
    }

    private void doProcessDelete() {
        int index = squarenum.get() - 1 + (rowSize * rownum);
        squarenum.set(squarenum.get() - 1);
        infoButton.getScene().getRoot().requestFocus();
        letters.get(index).setText("");
        letters.get(index).setLetterDisplay(DisplayType.PLAIN);
    }

    // allow keyboard input if available
    @FXML
    private void handleKeyPressed(KeyEvent event) {
        // ignore key input if certain buttons are disabled
        if (event.getCode().isLetterKey()) {
            if (keyLetters.get("A").isDisabled()) {
                return;
            }
            // process letter
            doSelectLetter(event.getCode().getChar());
        } else if (event.getCode().equals(KeyCode.ENTER)) {
            if (enterButton.isDisabled()) {
                return;
            }
            // process Enter
            doProcessEnter();
        } else if (event.getCode().equals(KeyCode.DELETE) || event.getCode().equals(KeyCode.BACK_SPACE)) {
            if (deleteButton.isDisabled()) {
                return;
            }
            // process Delete
            doProcessDelete();
        } else {
            System.out.println("Something else: " + event.getCode().getChar());
            return;
        }
    }

    @FXML
    private void displayInfo() throws IOException {
        WordishApp.setRoot("howto");
    }

}
