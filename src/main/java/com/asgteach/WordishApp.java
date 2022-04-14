package com.asgteach;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.text.Font;

public class WordishApp extends Application {

    private static Scene scene;
//    private static GameStatus gameStatus = GameStatus.getInstance();

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("wordish"), 414, 736);
        
        stage.setScene(scene);
        stage.show();
    }
    
    static {
        try {
            Font.loadFont(WordishApp.class.getResource("materialdesignicons-webfont.ttf").toExternalForm(), 16);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    static void setRoot(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WordishApp.class.getResource(fxml + ".fxml"));
        //scene.setRoot(loadFXML(fxml));
        Parent root = fxmlLoader.load();
        scene.setRoot(root);
    }
    

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WordishApp.class.getResource(fxml + ".fxml"));                
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}