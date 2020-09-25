package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static Stage stage = new Stage();
    private static Screen screen = Screen.getPrimary();
    private static Rectangle2D bounds = screen.getVisualBounds();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/app_screen.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../style/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Trabalho Final CRUD");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static void maximize(){
        if(stage.isMaximized()){
            stage.setMaximized(false);
        }else {
            stage.setMaximized(true);
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
