package dao;

import db.ConnectionHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Cliente;
import model.Endereco;

import java.sql.*;

public abstract class EnderecoDAO {

    public static int create(Endereco endereco){
        Connection connection = ConnectionHandler.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int idEndereco = 0;

        try {
            statement = connection.prepareStatement("INSERT INTO enderecos (rua, uf, cidade, cep) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, endereco.getRua());
            statement.setString(2, endereco.getUf());
            statement.setString(3, endereco.getCidade());
            statement.setInt(4, endereco.getCep());

            statement.executeUpdate();

            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            idEndereco = resultSet.getInt(1);

        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao adicionar ao banco de dados.\n\nErro: " +throwables, ButtonType.OK);
            alert.setTitle("Erro");
            alert.show();
        }finally {
            ConnectionHandler.closeConnection(connection, statement, resultSet);
        }
            return idEndereco;
    }

    public static Endereco read(int id){
        Connection connection = ConnectionHandler.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Endereco endereco = null;

        try {
            statement = connection.prepareStatement("SELECT * FROM enderecos WHERE id = ?");
            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            resultSet.next();
            endereco = new Endereco(
                    resultSet.getInt("id"),
                    resultSet.getString("rua"),
                    resultSet.getString("uf"),
                    resultSet.getString("cidade"),
                    resultSet.getInt("cep")
            );
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao consultar o banco de dados.\n\nErro: " +throwables, ButtonType.OK);
            alert.setTitle("Erro");
            alert.show();
        }finally {
            ConnectionHandler.closeConnection(connection, statement, resultSet);
        }

        return endereco;
    }

    public static void update(Endereco endereco){
        Connection connection = ConnectionHandler.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("UPDATE enderecos SET rua = ?, uf = ?, cidade = ?, cep = ?WHERE id = ?");
            statement.setString(1, endereco.getRua());
            statement.setString(2, endereco.getUf());
            statement.setString(3, endereco.getCidade());
            statement.setInt(4, endereco.getCep());
            statement.setInt(5, endereco.getId());

            statement.executeUpdate();
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao atualizar o banco de dados.\n\nErro: " +throwables, ButtonType.OK);
            alert.setTitle("Erro");
            alert.show();
        }finally {
            ConnectionHandler.closeConnection(connection, statement);
        }
    }

    public static void delete(int id){
        Connection connection = ConnectionHandler.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("DELETE FROM enderecos WHERE id = ?");
            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao acessar o banco de dados.\n\nErro: " +throwables, ButtonType.OK);
            alert.setTitle("Erro");
            alert.show();
        }finally {
            ConnectionHandler.closeConnection(connection, statement);
        }
    }
}
