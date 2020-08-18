package by.academy.it.mysql;

import by.academy.it.ClientDao;
import by.academy.it.ClientDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ClientDaoImpl implements ClientDao {

    private Connection connection;
    private boolean isTestInstance;
    private static Logger log = Logger.getLogger(ClientDaoImpl.class.getName());

    public ClientDaoImpl() throws SQLException {
        this.isTestInstance = false;
        connect();
    }

    public ClientDaoImpl(boolean isTestInstance) throws SQLException {
        this.isTestInstance = isTestInstance;
        connect();
    }

    private void connect() throws SQLException {
        if (isTestInstance) {
            this.connection = MySqlDataSource.getTestConnection();
        } else {
            this.connection = MySqlDataSource.getConnection();
        }
    }

    @Override
    public int create(ClientDto clientDto) throws SQLException {
        log.info("Creating new client: " + clientDto);
        PreparedStatement preparedStatement = null;
        if (!isTestInstance) {
            preparedStatement
                    = connection.prepareStatement("insert into clients " +
                    "values (?,?,?,?,?,?) ");
        } else {
            preparedStatement = connection.prepareStatement("insert into client_test.clients " +
                    "values (?,?,?,?,?,?) ");
        }

        preparedStatement.setInt(1, clientDto.getId());
        preparedStatement.setString(2, clientDto.getName());
        preparedStatement.setString(3, clientDto.getSecondName());
        preparedStatement.setString(4, clientDto.getEmail());
        preparedStatement.setDate(5, clientDto.getDateOfBirth());
        preparedStatement.setString(6, String.valueOf(clientDto.getGender()));

        boolean result = preparedStatement.execute();
        preparedStatement.close();
        if (result) {
            return clientDto.getId();
        } else return -1;
    }

    @Override
    public ClientDto read(int id) throws SQLException {
        if(connection.isClosed()){
            connect();
        }
        log.info("Reading client info for the following id: " + id);
        ClientDto clientDto = new ClientDto();

        clientDto.setId(id);
        PreparedStatement preparedStatement = connection.prepareStatement("select * from clients " +
                "where id=?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<ClientDto> clientDtos = parseResultSet(resultSet);
        preparedStatement.close();
        return clientDtos.size() > 0 ? clientDtos.get(0) : null;
    }

    private List<ClientDto> parseResultSet(ResultSet resultSet) throws SQLException {
        List<ClientDto> clients = new ArrayList<>();
        while (resultSet.next()) {
            ClientDto client = new ClientDto();
            client.setId(resultSet.getInt(1));
            client.setName(resultSet.getString(2));
            client.setSecondName(resultSet.getString(3));
            client.setEmail(resultSet.getString(4));
            client.setDateOfBirth(resultSet.getDate(5));
            client.setGender(resultSet.getString(6).charAt(0));
            clients.add(client);
        }
        return clients;
    }

    @Override
    public List<ClientDto> readAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from clients");
        List<ClientDto> clientDtos = parseResultSet(resultSet);
        statement.close();
        return clientDtos;
    }

    @Override
    public void update(ClientDto clientDto) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "update clients set name=?, second_name=?, email=?, date_of_birth=?, gender=? " +
                        "where id=?");
        preparedStatement.setString(1, clientDto.getName());
        preparedStatement.setString(2, clientDto.getSecondName());
        preparedStatement.setString(3, clientDto.getEmail());
        preparedStatement.setDate(4, clientDto.getDateOfBirth());
        preparedStatement.setString(5, String.valueOf(clientDto.getGender()));
        preparedStatement.setInt(6, clientDto.getId());

        preparedStatement.execute();
        preparedStatement.close();
    }

    @Override
    public boolean delete(ClientDto clientDto) throws SQLException {
        return delete(clientDto.getId());
    }

    @Override
    public boolean delete(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "delete from clients where id=?");
        preparedStatement.setInt(1, id);
        boolean result = preparedStatement.execute();
        preparedStatement.close();
        return result;
    }

    @Override
    public int getMaxId() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select max(id) from clients");
        int id = 0;
        if (resultSet.next()) id = resultSet.getInt(1);
        statement.close();
        return id;
    }
}
