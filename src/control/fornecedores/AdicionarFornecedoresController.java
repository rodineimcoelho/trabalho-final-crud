package control.fornecedores;

import dao.ClienteDAO;
import dao.FornecedorDAO;
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
import javafx.util.converter.BigIntegerStringConverter;
import model.Cliente;
import model.Endereco;
import model.Fornecedor;

import java.math.BigInteger;
import java.net.URL;
import java.util.ResourceBundle;

public class AdicionarFornecedoresController implements Initializable {

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
    private TextField txt_cnpj;

    @FXML
    private Label label;

    TableView<Fornecedor> tableView;

    public AdicionarFornecedoresController(TableView<Fornecedor> tableView){
        this.tableView = tableView;
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

        makeStageDraggable();
        addTextLimiter(txt_cep, 8);
        addTextLimiter(txt_cnpj, 14);
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
        String cepString = txt_cep.getText().trim();
        String cnpjString = txt_cnpj.getText().trim();
        int cep;
        long cnpj;

        if(!checkEmpty(nome, txt_nome, "Nome não pode estar vazio", label)){
            if(!checkEmpty(rua, txt_rua, "Rua não pode estar vazia", label)){
                if(!checkEmpty(uf, box_uf, "Selecione uma UF", label)){
                    if(!checkEmpty(cidade, txt_cidade, "Cidade não pode estar vazia", label)){
                        if(!(checkEmpty(cepString, txt_cep, "CEP não pode estar vazio", label))){
                            if(!cepString.matches("^[0-9]*$")){
                                label.setText("CEP deve conter apenas números");
                                label.setTextFill(Paint.valueOf("red"));
                                txt_cep.setStyle(
                                        "-fx-background-color: red, #FFFFFF ;\n" +
                                                "    -fx-background-insets: 0, 0 0 1 0 ;\n" +
                                                "    -fx-background-radius: 0");
                            }else if(cepString.length() < 8 ){
                                label.setText("CEP deve conter 8 dígitos");
                                label.setTextFill(Paint.valueOf("red"));
                                txt_cep.setStyle(
                                        "-fx-background-color: red, #FFFFFF ;\n" +
                                                "    -fx-background-insets: 0, 0 0 1 0 ;\n" +
                                                "    -fx-background-radius: 0");
                            }else {
                                cep = Integer.parseInt(cepString);
                                if(!checkEmpty(cnpjString, txt_cnpj, "CNPJ não pode estar vazio", label)){
                                    if(!cnpjString.matches("^[0-9]*$")){
                                        label.setText("CENPJ deve conter apenas números");
                                        label.setTextFill(Paint.valueOf("red"));
                                        txt_cnpj.setStyle(
                                                "-fx-background-color: red, #FFFFFF ;\n" +
                                                        "    -fx-background-insets: 0, 0 0 1 0 ;\n" +
                                                        "    -fx-background-radius: 0");
                                    }else if(cnpjString.length() < 14 ) {
                                        label.setText("CNPJ deve conter 14 dígitos");
                                        label.setTextFill(Paint.valueOf("red"));
                                        txt_cnpj.setStyle(
                                                "-fx-background-color: red, #FFFFFF ;\n" +
                                                        "    -fx-background-insets: 0, 0 0 1 0 ;\n" +
                                                        "    -fx-background-radius: 0");
                                    }else {
                                        cnpj = Long.parseLong(cnpjString);
                                        Endereco endereco = new Endereco(rua, uf, cidade, cep);
                                        FornecedorDAO.create(new Fornecedor(nome, endereco, cnpj));
                                        label.setText("Fornecedor adicionado com sucesso");
                                        label.setTextFill(Paint.valueOf("green"));

                                        tableView.setItems(FXCollections.observableArrayList(FornecedorDAO.read()));

                                        txt_nome.setStyle(style);
                                        txt_rua.setStyle(style);
                                        box_uf.setStyle(style);
                                        txt_cidade.setStyle(style);
                                        txt_cep.setStyle(style);
                                        txt_cnpj.setStyle(style);
                                    }
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
}
