package dao;

import db.ConnectionHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Endereco;
import model.Fornecedor;

import java.sql.*;
import java.util.ArrayList;

public abstract class FornecedorDAO {

    public static void create(Fornecedor fornecedor){
        Connection connection = ConnectionHandler.getConnection();
        PreparedStatement statement = null;
        int idEndereco = EnderecoDAO.create(fornecedor.getEndereco());

        if(idEndereco != 0){
            try {
                statement = connection.prepareStatement("INSERT INTO fornecedores (nome, id_endereco, cnpj) VALUES (?, ?, ?)");
                statement.setString(1, fornecedor.getNome());
                statement.setInt(2, idEndereco);
                statement.setLong(3, fornecedor.getCnpj());

                statement.executeUpdate();
            } catch (SQLException throwables) {
                Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao adicionar ao banco de dados.\n\nErro: " +throwables, ButtonType.OK);
                alert.setTitle("Erro");
                alert.show();
            }finally {
                ConnectionHandler.closeConnection(connection, statement);
            }
        }
    }

    public static ArrayList<Fornecedor> read(){
        Connection connection = ConnectionHandler.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ArrayList<Fornecedor> fornecedores = new ArrayList<>();

        try {
            statement = connection.prepareStatement("SELECT * FROM fornecedores");
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                Endereco endereco = EnderecoDAO.read(resultSet.getInt("id_endereco"));
                if(endereco == null){
                    return null;
                }
                Fornecedor fornecedor = new Fornecedor(
                        resultSet.getInt("id"),
                        resultSet.getString("nome"),
                        endereco,
                        resultSet.getLong("cnpj")
                );

                fornecedores.add(fornecedor);
            }
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao consultar o banco de dados.\n\nErro: " +throwables, ButtonType.OK);
            alert.setTitle("Erro");
            alert.show();
        }finally {
            ConnectionHandler.closeConnection(connection, statement, resultSet);
        }

        return fornecedores;
    }

    public static ArrayList<Fornecedor> read(String search){

        if(search.isEmpty() || search == null){
            return read();
        }else{
            Connection connection = ConnectionHandler.getConnection();
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            ArrayList<Fornecedor> fornecedores = new ArrayList<>();

            try {
                statement = connection.prepareStatement("SELECT * FROM fornecedores WHERE nome LIKE ?");
                statement.setString(1, "%" +search +"%");
                resultSet = statement.executeQuery();

                while (resultSet.next()){
                    Endereco endereco = EnderecoDAO.read(resultSet.getInt("id_endereco"));
                    if(endereco == null){
                        return null;
                    }
                    Fornecedor cliente = new Fornecedor(
                            resultSet.getInt("id"),
                            resultSet.getString("nome"),
                            endereco,
                            resultSet.getLong("cnpj")
                    );

                    fornecedores.add(cliente);
                }
            } catch (SQLException throwables) {
                Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao consultar o banco de dados.\n\nErro: " +throwables, ButtonType.OK);
                alert.setTitle("Erro");
                alert.show();
            }finally {
                ConnectionHandler.closeConnection(connection, statement, resultSet);
            }

            return fornecedores;
        }

    }

    public static void update(Fornecedor fornecedor){
        Connection connection = ConnectionHandler.getConnection();
        PreparedStatement statement = null;

        try {
            EnderecoDAO.update(fornecedor.getEndereco());

            statement = connection.prepareStatement("UPDATE fornecedores SET nome = ?, cnpj = ? WHERE id = ?");
            statement.setString(1, fornecedor.getNome());
            statement.setLong(2, fornecedor.getCnpj());
            statement.setInt(3, fornecedor.getId());

            statement.executeUpdate();
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao atualizar o banco de dados.\n\nErro: " +throwables, ButtonType.OK);
            alert.setTitle("Erro");
            alert.show();
        }finally {
            ConnectionHandler.closeConnection(connection, statement);
        }
    }

    public static void delete(int id, int idEndereco){
        Connection connection = ConnectionHandler.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("DELETE FROM fornecedores WHERE id = ?");
            statement.setInt(1, id);

            statement.executeUpdate();
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao acessar o banco de dados.\n\nErro: " +throwables, ButtonType.OK);
            alert.setTitle("Erro");
            alert.show();
        }finally {
            ConnectionHandler.closeConnection(connection, statement);
        }

        EnderecoDAO.delete(idEndereco);
    }
}
