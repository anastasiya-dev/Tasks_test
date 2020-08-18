package by.academy.it;

import java.sql.SQLException;
import java.util.List;

public interface ClientDao {
    //int - it will return id
    //create can return boolean (true/false) or void (nothing) - depends on the purpose
    int create(ClientDto clientDto) throws SQLException;

    ClientDto read(int id) throws SQLException;

    List<ClientDto> readAll() throws SQLException;

    void update(ClientDto clientDto) throws SQLException;

    boolean delete(ClientDto clientDto) throws SQLException;

    boolean delete(int id) throws SQLException;

    int getMaxId() throws SQLException;
}
