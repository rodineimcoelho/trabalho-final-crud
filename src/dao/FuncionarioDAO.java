package dao;

import db.ConnectionHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Endereco;
import model.Funcionario;

import java.sql.*;
import java.util.ArrayList;

public abstract class FuncionarioDAO {

    public static void create(Funcionario funcionario){
        Connection connection = ConnectionHandler.getConnection();
        PreparedStatement statement = null;
        int idEndereco = EnderecoDAO.create(funcionario.getEndereco());

        if(idEndereco != 0){
            try {
                statement = connection.prepareStatement("INSERT INTO funcionarios (nome, id_endereco, cargo) VALUES (?, ?, ?)");
                statement.setString(1, funcionario.getNome());
                statement.setInt(2, idEndereco);
                statement.setString(3, funcionario.getCargo());

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

    public static ArrayList<Funcionario> read(){
        Connection connection = ConnectionHandler.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ArrayList<Funcionario> funcionarios = new ArrayList<>();

        try {
            statement = connection.prepareStatement("SELECT * FROM funcionarios");
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                Endereco endereco = EnderecoDAO.read(resultSet.getInt("id_endereco"));
                if(endereco == null){
                    return null;
                }
                Funcionario funcionario = new Funcionario(
                        resultSet.getInt("id"),
                        resultSet.getString("nome"),
                        endereco,
                        resultSet.getString("cargo")
                );

                funcionarios.add(funcionario);
            }
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao consultar o banco de dados.\n\nErro: " +throwables, ButtonType.OK);
            alert.setTitle("Erro");
            alert.show();
        }finally {
            ConnectionHandler.closeConnection(connection, statement, resultSet);
        }

        return funcionarios;
    }

    public static ArrayList<Funcionario> read(String search){

        if(search.isEmpty() || search == null){
            return read();
        }else{
            Connection connection = ConnectionHandler.getConnection();
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            ArrayList<Funcionario> funcionarios = new ArrayList<>();

            try {
                statement = connection.prepareStatement("SELECT * FROM funcionarios WHERE nome LIKE ?");
                statement.setString(1, "%" +search +"%");
                resultSet = statement.executeQuery();

                while (resultSet.next()){
                    Endereco endereco = EnderecoDAO.read(resultSet.getInt("id_endereco"));
                    if(endereco == null){
                        return null;
                    }
                    Funcionario funcionario = new Funcionario(
                            resultSet.getInt("id"),
                            resultSet.getString("nome"),
                            endereco,
                            resultSet.getString("cargo")
                    );

                    funcionarios.add(funcionario);
                }
            } catch (SQLException throwables) {
                Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao consultar o banco de dados.\n\nErro: " +throwables, ButtonType.OK);
                alert.setTitle("Erro");
                alert.show();
            }finally {
                ConnectionHandler.closeConnection(connection, statement, resultSet);
            }

            return funcionarios;
        }

    }

    public static void update(Funcionario funcionario){
        Connection connection = ConnectionHandler.getConnection();
        PreparedStatement statement = null;

        try {
            EnderecoDAO.update(funcionario.getEndereco());

            statement = connection.prepareStatement("UPDATE funcionarios SET nome = ?, cargo = ? WHERE id = ?");
            statement.setString(1, funcionario.getNome());
            statement.setString(2, funcionario.getCargo());
            statement.setInt(3, funcionario.getId());

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
            statement = connection.prepareStatement("DELETE FROM funcionarios WHERE id = ?");
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
