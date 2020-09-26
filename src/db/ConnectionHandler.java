package db;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;

public abstract class ConnectionHandler {

    public static Connection getConnection(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:crud_database.db");
            Statement statement = connection.createStatement();
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS enderecos (\n" +
                            "    id     INTEGER     PRIMARY KEY AUTOINCREMENT,\n" +
                            "    rua    VARCHAR,\n" +
                            "    uf     VARCHAR (2),\n" +
                            "    cidade VARCHAR,\n" +
                            "    cep    INTEGER (8) \n" +
                            ");"
            );
            statement.execute("" +
                    "CREATE TABLE IF NOT EXISTS clientes (\n" +
                    "    id           INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "    nome         VARCHAR,\n" +
                    "    id_endereco  INTEGER REFERENCES enderecos (id),\n" +
                    "    nome_usuario VARCHAR,\n" +
                    "    senha        VARCHAR\n" +
                    ");");

            statement.execute("" +
                    "CREATE TABLE IF NOT EXISTS fornecedores (\n" +
                    "    id           INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "    nome         VARCHAR,\n" +
                    "    id_endereco  INTEGER REFERENCES enderecos (id),\n" +
                    "    cnpj INTEGER(14)\n" +
                    ");");
            return connection;
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
