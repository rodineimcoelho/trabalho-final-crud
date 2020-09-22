package dao;

import db.ConnectionHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Cliente;
import model.Endereco;

import java.sql.*;
import java.util.ArrayList;

public abstract class ClienteDAO {

    public static void create(Cliente cliente){
        Connection connection = ConnectionHandler.getConnection();
        PreparedStatement statement = null;
        int idEndereco = EnderecoDAO.create(cliente.getEndereco());

        if(idEndereco != 0){
            try {
                statement = connection.prepareStatement("INSERT INTO clientes (nome, id_endereco, nome_usuario, senha) VALUES (?, ?, ?, ?)");
                statement.setString(1, cliente.getNome());
                statement.setInt(2, idEndereco);
                statement.setString(3, cliente.getNomeUsuario());
                statement.setString(4, cliente.getSenha());

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

    public static ArrayList<Cliente> read(){
        Connection connection = ConnectionHandler.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ArrayList<Cliente> clientes = new ArrayList<>();

        try {
            statement = connection.prepareStatement("SELECT * FROM clientes");
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                Endereco endereco = EnderecoDAO.read(resultSet.getInt("id_endereco"));
                if(endereco == null){
                    return null;
                }
                Cliente cliente = new Cliente(
                        resultSet.getInt("id"),
                        resultSet.getString("nome"),
                        endereco,
                        resultSet.getString("nome_usuario"),
                        resultSet.getString("senha")
                );

                clientes.add(cliente);
            }
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao consultar o banco de dados.\n\nErro: " +throwables, ButtonType.OK);
            alert.setTitle("Erro");
            alert.show();
        }finally {
            ConnectionHandler.closeConnection(connection, statement, resultSet);
        }

        return clientes;
    }

    public static ArrayList<Cliente> read(String search){

        if(search.isEmpty() || search == null){
            return read();
        }else{
            Connection connection = ConnectionHandler.getConnection();
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            ArrayList<Cliente> clientes = new ArrayList<>();

            try {
                statement = connection.prepareStatement("SELECT * FROM clientes WHERE nome LIKE ?");
                statement.setString(1, "%" +search +"%");
                resultSet = statement.executeQuery();

                while (resultSet.next()){
                    Endereco endereco = EnderecoDAO.read(resultSet.getInt("id_endereco"));
                    if(endereco == null){
                        return null;
                    }
                    Cliente cliente = new Cliente(
                            resultSet.getInt("id"),
                            resultSet.getString("nome"),
                            endereco,
                            resultSet.getString("nome_usuario"),
                            resultSet.getString("senha")
                    );

                    clientes.add(cliente);
                }
            } catch (SQLException throwables) {
                Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao consultar o banco de dados.\n\nErro: " +throwables, ButtonType.OK);
                alert.setTitle("Erro");
                alert.show();
            }finally {
                ConnectionHandler.closeConnection(connection, statement, resultSet);
            }

            return clientes;
        }

    }

    public static void update(Cliente cliente){
        Connection connection = ConnectionHandler.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("UPDATE clientes SET nome = ?, nome_usuario = ?, senha = ? WHERE id = ?");
            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getNomeUsuario());
            statement.setString(3, cliente.getSenha());
            statement.setInt(4, cliente.getId());

            statement.executeUpdate();
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao atualizar o banco de dados.\n\nErro: " +throwables, ButtonType.OK);
            alert.setTitle("Erro");
            alert.show();
        }finally {
            ConnectionHandler.closeConnection(connection, statement);
        }

        EnderecoDAO.update(cliente.getEndereco());
    }

    public static void delete(int id, int idEndereco){
        Connection connection = ConnectionHandler.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("DELETE FROM clientes WHERE id = ?");
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
