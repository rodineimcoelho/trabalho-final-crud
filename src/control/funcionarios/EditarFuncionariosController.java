package control.funcionarios;

import dao.FornecedorDAO;
import dao.FuncionarioDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import model.Endereco;
import model.Fornecedor;
import model.Funcionario;

import java.net.URL;
import java.util.ResourceBundle;

public class EditarFuncionariosController implements Initializable {

    @FXML
    private HBox appbar;

    @FXML
    private Button close_button;

    private double xOffSet;
    private double yOffSet;


    @FXML
    private TextField txt_nome;

    @FXML
    private TextField txt_rua;

    @FXML
    private ComboBox<String> box_uf;

    @FXML
    private TextField txt_cidade;

    @FXML
    private TextField txt_cep;

    @FXML
    private TextField txt_cargo;

    @FXML
    private Label label;

    @FXML
    private Button button;

    private TableView<Funcionario> tableView;

    private Funcionario funcionario;

    private int id;



    public EditarFuncionariosController(TableView<Funcionario> tableView, Funcionario funcionario){
        this.tableView = tableView;
        this.funcionario = funcionario;
        this.id = funcionario.getId();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        box_uf.getItems().addAll(
                "AC",
                "AL",
                "AP",
                "AM",
                "BA",
                "CE",
                "DF",
                "ES",
                "GO",
                "MA",
                "MT",
                "MS",
                "MG",
                "PA",
                "PB",
                "PR",
                "PE",
                "PI",
                "RJ",
                "RN",
                "RS",
                "RO",
                "RR",
                "SC",
                "SP",
                "SE",
                "TO");

        button.setText("Editar");

        makeStageDraggable();
        addTextLimiter(txt_cep, 8);

        loadTexts();
    }

    private void makeStageDraggable() {

        appbar.setOnMousePressed((event) -> {
            xOffSet = event.getSceneX();
            yOffSet = event.getSceneY();

        });

        appbar.setOnMouseDragged((event) -> {
            if (!close_button.isPressed()) {
                Stage stage = (Stage) appbar.getScene().getWindow();
                stage.setX(event.getScreenX() - xOffSet);
                stage.setY(event.getScreenY() - yOffSet);
            }
        });
    }

    @FXML
    void close(ActionEvent event) {
        Stage stage = (Stage) close_button.getScene().getWindow();
        stage.close();
    }

    @FXML
    void action(ActionEvent event) {
        String style =
                ".text-field{\n" +
                        "     -fx-background-color: -fx-text-box-border, #FFFFFF ;\n" +
                        "     -fx-background-insets: 0, 0 0 1 0 ;\n" +
                        "     -fx-background-radius: 0 ;\n" +
                        "}\n" +
                        ".text-field: focused{\n" +
                        "    -fx-background-color: #3949AB, #FFFFFF ;\n" +
                        "}" +
                        ".choice-box{\n" +
                        "     -fx-background-color: -fx-text-box-border, #FFFFFF ;\n" +
                        "     -fx-background-insets: 0, 0 0 1 0 ;\n" +
                        "     -fx-background-radius: 0 ;\n" +
                        "}\n" +
                        ".choice-box: focused{\n" +
                        "    -fx-background-color: #3949AB, #FFFFFF ;\n" +
                        "}";

        String nome = txt_nome.getText().trim();
        String rua = txt_rua.getText().trim();
        String uf = box_uf.getSelectionModel().getSelectedItem();
        String cidade = txt_cidade.getText();
        String cep = txt_cep.getText().trim();
        String cargo = txt_cargo.getText().trim();

        if(!checkEmpty(nome, txt_nome, "Nome não pode estar vazio", label)){
            if(!checkEmpty(rua, txt_rua, "Rua não pode estar vazia", label)){
                if(!checkEmpty(uf, box_uf, "Selecione uma UF", label)){
                    if(!checkEmpty(cidade, txt_cidade, "Cidade não pode estar vazia", label)){
                        if(!checkEmpty(cep, txt_cep, "CEP não pode estar vazio", label)){
                            if(!cep.matches("^[0-9]*$")){
                                label.setText("CEP deve conter apenas números");
                                label.setTextFill(Paint.valueOf("red"));
                                txt_cep.setStyle(
                                        "-fx-background-color: red, #FFFFFF ;\n" +
                                                "    -fx-background-insets: 0, 0 0 1 0 ;\n" +
                                                "    -fx-background-radius: 0");
                            }else if(cep.length() < 8 ){
                                label.setText("CEP deve conter 8 dígitos");
                                label.setTextFill(Paint.valueOf("red"));
                                txt_cep.setStyle(
                                        "-fx-background-color: red, #FFFFFF ;\n" +
                                                "    -fx-background-insets: 0, 0 0 1 0 ;\n" +
                                                "    -fx-background-radius: 0");
                            }else if (!checkEmpty(cargo, txt_cargo, "Cargo não pode estar vazio", label)){
                                Endereco endereco = new Endereco(funcionario.getEndereco().getId(), rua, uf, cidade, cep);

                                Alert alert = new Alert(Alert.AlertType.NONE, "Tem certeza?", ButtonType.NO, ButtonType.YES);
                                alert.setTitle("Confirmação");
                                alert.showAndWait();

                                if(alert.getResult() == ButtonType.YES){
                                    FuncionarioDAO.update(new Funcionario(id, nome, endereco, cargo));
                                    tableView.setItems(FXCollections.observableArrayList(FuncionarioDAO.read()));
                                    close(new ActionEvent());
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private boolean checkEmpty(String string, TextField textField, String error, Label label){
        if(string.isBlank()){
            label.setText(error);
            label.setTextFill(Paint.valueOf("red"));
            textField.setStyle(
                    "-fx-background-color: red, #FFFFFF ;\n" +
                            "    -fx-background-insets: 0, 0 0 1 0 ;\n" +
                            "    -fx-background-radius: 0");
            return true;
        }
        return false;
    }

    private boolean checkEmpty(String string, ComboBox<String> comboBox, String error, Label label){
        if(string.isBlank()){
            label.setText(error);
            label.setTextFill(Paint.valueOf("red"));
            comboBox.setStyle(
                    "-fx-background-color: red, #FFFFFF ;\n" +
                            "    -fx-background-insets: 0, 0 0 1 0 ;\n" +
                            "    -fx-background-radius: 0");
            return true;
        }
        return false;
    }

    public void addTextLimiter(TextField textField, int maxLenght){
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(textField.getText().length() > maxLenght){
                    s = textField.getText().substring(0, maxLenght);
                    textField.setText(s);
                }
            }
        });
    }

    private void loadTexts(){
        txt_nome.setText(funcionario.getNome());
        txt_rua.setText(funcionario.getEndereco().getRua());
        box_uf.getSelectionModel().select(funcionario.getEndereco().getUf());
        txt_cidade.setText(funcionario.getEndereco().getCidade());
        txt_cep.setText(funcionario.getEndereco().getCep());
        txt_cargo.setText(funcionario.getCargo());
    }
}
