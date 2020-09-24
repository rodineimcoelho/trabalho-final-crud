package control.clientes;

import dao.ClienteDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import model.Cliente;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientesController implements Initializable {

    @FXML
    private TextField search_field;

    @FXML
    private TableView<Cliente> table;

    @FXML
    private Button add_button;

    private ObservableList<Cliente> clientes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createColumns();
        clientes = FXCollections.observableArrayList(ClienteDAO.read());
        table.setItems(clientes);

        add_button.setText("Adicionar Cliente");
        table.setPlaceholder(new Label("Tabela vazia"));
    }

    private void createColumns(){
        TableColumn columnId = new TableColumn<Cliente, Integer>("ID");
        TableColumn columnNome = new TableColumn<Cliente, String>("Nome");
        TableColumn columnEndereco = new TableColumn<Cliente, String>("Endereço");
        TableColumn columnNomeUsuario = new TableColumn<Cliente, String>("Nome de usuário");
        TableColumn columnSenha = new TableColumn<Cliente, String>("Senha");

        columnId.setEditable(false);
        columnNome.setEditable(false);
        columnEndereco.setEditable(false);
        columnNomeUsuario.setEditable(false);
        columnSenha.setEditable(false);

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        columnEndereco.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures<Cliente, String> cellDataFeatures) {
                return new SimpleStringProperty(cellDataFeatures.getValue().getEndereco().toString());
            }
        });
        columnNomeUsuario.setCellValueFactory(new PropertyValueFactory<>("nomeUsuario"));
        columnSenha.setCellValueFactory(new PropertyValueFactory<>("senha"));

        table.getColumns().addAll(columnId, columnNome, columnEndereco, columnNomeUsuario, columnSenha);
    }

    @FXML
    void add(ActionEvent event) {
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/adicionar_cliente.fxml"));
            loader.setController(new AdicionarClientesController(table));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(add_button.getScene().getWindow());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao abrir nova janela.\n\nErro: " +e, ButtonType.OK);
            alert.setTitle("Erro");
            alert.show();
        }

    }

    @FXML
    void search(ActionEvent event) {
        ObservableList<Cliente> search = FXCollections.observableArrayList(ClienteDAO.read(search_field.getText().trim()));
        if(clientes.size() == 0){
            table.setPlaceholder(new Label("Tabela vazia"));
        }else if(search.size() == 0){
            table.setPlaceholder(new Label("Sem correspondências para a busca"));
        }
        table.setItems(search);
    }
}
