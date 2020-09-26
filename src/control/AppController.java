package control;

import application.Main;
import control.clientes.ClientesController;
import control.fornecedores.FornecedoresController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    @FXML
    private HBox appbar;

    @FXML
    private Button minimize_button;

    @FXML
    private Button maximize_button;

    @FXML
    private Button close_button;

    @FXML
    private Button button_clientes;

    @FXML
    private Button button_fornecedores;

    @FXML
    private Button button_funcionarios;


    @FXML
    private Pane title_pane;

    @FXML
    private Label title;

    @FXML
    private BorderPane section_pane;

    private double xOffSet = 0;
    private double yOffSet = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeStageDraggable();
    }

    private void makeStageDraggable(){
        appbar.setOnMousePressed((event) -> {
            xOffSet = event.getSceneX();
            yOffSet = event.getSceneY();

        });

        appbar.setOnMouseDragged((event) -> {
            if(!(minimize_button.isPressed() || maximize_button.isPressed() || close_button.isPressed())){
                if(Main.stage.isMaximized()){
                    Main.stage.setMaximized(false);
                }
                    Main.stage.setX(event.getScreenX() - xOffSet);
                    Main.stage.setY(event.getScreenY() - yOffSet);
            }
        });
    }

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void maximize(ActionEvent event) {
        Main.maximize();
    }

    @FXML
    void minimize(ActionEvent event) {
        Main.stage.setIconified(true);
    }

    @FXML
    void clientes(ActionEvent event) {
        select(button_clientes);
        unselect(button_fornecedores);
        unselect(button_funcionarios);

        title_pane.setBackground(new Background(new BackgroundFill(Color.web("#683FB5"), CornerRadii.EMPTY, Insets.EMPTY)));
        title.setText("Clientes");

        Parent root = null;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/section.fxml"));
            loader.setController(new ClientesController());
            root = loader.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao carregar layout.\n\nErro: " +e, ButtonType.OK);
            alert.setTitle("Erro");
            alert.show();
        }

        section_pane.setCenter(root);

    }

    @FXML
    void fornecedores(ActionEvent event) {
        unselect(button_clientes);
        select(button_fornecedores);
        unselect(button_funcionarios);

        title_pane.setBackground(new Background(new BackgroundFill(Color.web("#A33FB5"), CornerRadii.EMPTY, Insets.EMPTY)));
        title.setText("Fornecedores");

        Parent root = null;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/section.fxml"));
            loader.setController(new FornecedoresController());
            root = loader.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao carregar layout.\n\nErro: " +e, ButtonType.OK);
            alert.setTitle("Erro");
            alert.show();
        }

        section_pane.setCenter(root);

    }

    @FXML
    void funcionarios(ActionEvent event) {
        unselect(button_clientes);
        unselect(button_fornecedores);
        select(button_funcionarios);

        title_pane.setBackground(new Background(new BackgroundFill(Color.web("#B53F51"), CornerRadii.EMPTY, Insets.EMPTY)));
        title.setText("Funcionários");

        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("../view/section.fxml"));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao carregar layout.\n\nErro: " +e, ButtonType.OK);
            alert.setTitle("Erro");
            alert.show();
        }

        section_pane.setCenter(root);

    }

    void select(Button button){
        button.getStyleClass().removeAll("unselected_button");
        button.getStyleClass().add("selected_button");
    }

    void unselect(Button button){
        button.getStyleClass().removeAll("selected_button");
        button.getStyleClass().add("unselected_button");
    }

}


