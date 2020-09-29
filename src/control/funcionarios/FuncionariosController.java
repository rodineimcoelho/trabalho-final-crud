package control.funcionarios;

import dao.FornecedorDAO;
import dao.FuncionarioDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import model.Fornecedor;
import model.Funcionario;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FuncionariosController implements Initializable {

    @FXML
    private TextField search_field;

    @FXML
    private TableView<Funcionario> table;

    @FXML
    private Button add_button;

    private ObservableList<Funcionario> funcionarios;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createColumns();

        add_button.setText("Adicionar Funcionário");
        table.setPlaceholder(new Label("Tabela vazia"));

        addContextMenu();

        refreshTable();
    }

    private void createColumns(){
        TableColumn columnId = new TableColumn<Funcionario, Integer>("ID");
        TableColumn columnNome = new TableColumn<Funcionario, String>("Nome");
        TableColumn columnEndereco = new TableColumn<Funcionario, String>("Endereço");
        TableColumn columnCargo = new TableColumn<Funcionario, String>("CNPJ");

        columnId.setEditable(false);
        columnNome.setEditable(false);
        columnEndereco.setEditable(false);
        columnCargo.setEditable(false);

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        columnEndereco.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Funcionario, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures<Funcionario, String> cellDataFeatures) {
                return new SimpleStringProperty(cellDataFeatures.getValue().getEndereco().toString());
            }
        });
        columnCargo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Funcionario, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures<Funcionario, String> cellDataFeatures) {
                return new SimpleStringProperty(cellDataFeatures.getValue().getCargo());
            }
        });

        table.getColumns().addAll(columnId, columnNome, columnEndereco, columnCargo);
    }

    @FXML
    void add(ActionEvent event) {
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/adicionar_funcionario.fxml"));
            loader.setController(new AdicionarFuncionariosController(table));
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

    void editar(Funcionario funcionario){
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/adicionar_funcionario.fxml"));
            loader.setController(new EditarFuncionariosController(table, funcionario));
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
        ObservableList<Funcionario> search = FXCollections.observableArrayList(FuncionarioDAO.read(search_field.getText().trim()));
        if(funcionarios.size() == 0){
            table.setPlaceholder(new Label("Tabela vazia"));
        }else if(search.size() == 0){
            table.setPlaceholder(new Label("Sem correspondências para a busca"));
        }
        table.setItems(search);
    }

    public void refreshTable(){
        funcionarios = FXCollections.observableArrayList(FuncionarioDAO.read());
        table.setItems(funcionarios);
    }

    void addContextMenu(){
        table.setRowFactory(new Callback<TableView<Funcionario>, TableRow<Funcionario>>() {
            @Override
            public TableRow<Funcionario> call(TableView<Funcionario> tableView) {
                TableRow<Funcionario> tableRow = new TableRow<>();

                ContextMenu contextMenu = new ContextMenu();

                MenuItem editarItem = new MenuItem("Editar");
                MenuItem removerItem = new MenuItem("Remover");

                editarItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        editar(tableRow.getItem());
                    }
                });

                removerItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Alert alert = new Alert(Alert.AlertType.NONE, "Tem certeza?", ButtonType.NO, ButtonType.YES);
                        alert.setTitle("Confirmação");
                        alert.showAndWait();

                        if(alert.getResult() == ButtonType.YES){
                            FuncionarioDAO.delete(tableRow.getItem().getId(), tableRow.getItem().getEndereco().getId());
                            table.setPlaceholder(new Label("Tabela vazia"));
                            refreshTable();
                        }

                    }
                });

                contextMenu.getItems().addAll(editarItem, removerItem);

                tableRow.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(!tableRow.isEmpty()){
                            tableRow.setContextMenu(contextMenu);
                        }else{
                            tableRow.setContextMenu(null);
                        }
                    }
                });

                return tableRow;
            }
        });
    }
}

