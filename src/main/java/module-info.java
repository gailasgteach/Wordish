module wordish {
    requires javafx.controls;
    requires javafx.fxml; 
    
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;

    opens com.asgteach to javafx.fxml;
    opens com.asgteach.modelview to javafx.fxml;

    exports com.asgteach;
}
