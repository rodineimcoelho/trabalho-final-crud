package control.fornecedores;

import dao.FornecedorDAO;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FornecedoresController implements Initializable {

    @FXML
    private TextField search_field;

    @FXML
    private TableView<Fornecedor> table;

    @FXML
    private Button add_button;

    private ObservableList<Fornecedor> fornecedores;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createColumns();

        add_button.setText("Adicionar Fornecedor");
        table.setPlaceholder(new Label("Tabela vazia"));

        addContextMenu();

        refreshTable();
    }

    private void createColumns(){
        TableColumn columnId = new TableColumn<Fornecedor, Integer>("ID");
        TableColumn columnNome = new TableColumn<Fornecedor, String>("Nome");
        TableColumn columnEndereco = new TableColumn<Fornecedor, String>("Endereço");
        TableColumn columnCNPJ = new TableColumn<Fornecedor, String>("CNPJ");

        columnId.setEditable(false);
        columnNome.setEditable(false);
        columnEndereco.setEditable(false);
        columnCNPJ.setEditable(false);

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        columnEndereco.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Fornecedor, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures<Fornecedor, String> cellDataFeatures) {
                return new SimpleStringProperty(cellDataFeatures.getValue().getEndereco().toString());
            }
        });
        columnCNPJ.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Fornecedor, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures<Fornecedor, String> cellDataFeatures) {
                return new SimpleStringProperty(cellDataFeatures.getValue().getFormattedCnpj());
            }
        });

        table.getColumns().addAll(columnId, columnNome, columnEndereco, columnCNPJ);
    }

    @FXML
    void add(ActionEvent event) {
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/adicionar_fornecedor.fxml"));
            loader.setController(new AdicionarFornecedoresController(table));
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

    void editar(Fornecedor fornecedor){
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/adicionar_fornecedor.fxml"));
            loader.setController(new EditarFornecedoresController(table, fornecedor));
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
        ObservableList<Fornecedor> search = FXCollections.observableArrayList(FornecedorDAO.read(search_field.getText().trim()));
        if(fornecedores.size() == 0){
            table.setPlaceholder(new Label("Tabela vazia"));
        }else if(search.size() == 0){
            table.setPlaceholder(new Label("Sem correspondências para a busca"));
        }
        table.setItems(search);
    }

    public void refreshTable(){
        fornecedores = FXCollections.observableArrayList(FornecedorDAO.read());
        table.setItems(fornecedores);
    }

    void addContextMenu(){
        table.setRowFactory(new Callback<TableView<Fornecedor>, TableRow<Fornecedor>>() {
            @Override
            public TableRow<Fornecedor> call(TableView<Fornecedor> tableView) {
                TableRow<Fornecedor> tableRow = new TableRow<>();

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
                        FornecedorDAO.delete(tableRow.getItem().getId(), tableRow.getItem().getEndereco().getId());
                        table.setPlaceholder(new Label("Tabela vazia"));
                        refreshTable();
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
