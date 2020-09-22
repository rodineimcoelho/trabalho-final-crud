package db;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;

public abstract class ConnectionHandler {

    public static Connection getConnection(){
        try {
            return DriverManager.getConnection("jdbc:../../db/crud_database.db");
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Erro na conexão com o banco de dados.\n\nErro: " +throwables, ButtonType.OK);
            alert.setTitle("Erro");
            alert.show();
        }
        return null;
    }

    public static void closeConnection(Connection connection){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException throwables) {
                Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao encerrar conexão com o banco de dados.\n\nErro: " +throwables, ButtonType.OK);
                alert.setTitle("Erro");
                alert.show();
            }
        }
    }

    public static void closeConnection(Connection connection, PreparedStatement statement){
        closeConnection(connection);

        if(statement != null){
            try {
                statement.close();
            } catch (SQLException throwables) {
                Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao encerrar conexão com o banco de dados.\n\nErro: " +throwables, ButtonType.OK);
                alert.setTitle("Erro");
                alert.show();
            }
        }
    }

    public static void closeConnection(Connection connection, PreparedStatement statement, ResultSet resultSet){
        closeConnection(connection, statement);

        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException throwables) {
                Alert alert = new Alert(Alert.AlertType.NONE, "Erro ao encerrar conexão com o banco de dados.\n\nErro: " +throwables, ButtonType.OK);
                alert.setTitle("Erro");
                alert.show();
            }
        }
    }
}
