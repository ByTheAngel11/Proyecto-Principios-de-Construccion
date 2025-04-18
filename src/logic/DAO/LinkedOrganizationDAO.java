package logic.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.DTO.LinkedOrganizationDTO;
import logic.interfaces.ILinkedOrganizationDAO;

public class LinkedOrganizationDAO implements ILinkedOrganizationDAO {
    private final static String SQL_INSERT = "INSERT INTO organizacion_vinculada (idOrganizacion, nombre, direccion) VALUES (?, ?, ?)";
    private final static String SQL_UPDATE = "UPDATE organizacion_vinculada SET nombre = ?, direccion = ? WHERE idOrganizacion = ?";
    private final static String SQL_DELETE = "DELETE FROM organizacion_vinculada WHERE idOrganizacion = ?";
    private final static String SQL_SELECT = "SELECT * FROM organizacion_vinculada WHERE idOrganizacion = ?";
    private final static String SQL_SELECT_ALL = "SELECT * FROM organizacion_vinculada";

    public boolean insertLinkedOrganization(LinkedOrganizationDTO organization, Connection connection) throws SQLException {
        LinkedOrganizationDTO existingOrganization = getLinkedOrganization(organization.getIddOrganization(), connection);
        if (existingOrganization != null) {
            return organization.getIddOrganization().equals(existingOrganization.getIddOrganization());
        }

        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
            statement.setString(1, organization.getIddOrganization());
            statement.setString(2, organization.getName());
            statement.setString(3, organization.getAdddress());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean updateLinkedOrganization(LinkedOrganizationDTO organization, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, organization.getName());
            statement.setString(2, organization.getAdddress());
            statement.setString(3, organization.getIddOrganization());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteLinkedOrganization(String iddOrganization, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setString(1, iddOrganization);
            return statement.executeUpdate() > 0;
        }
    }

    public LinkedOrganizationDTO getLinkedOrganization(String iddOrganization, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT)) {
            statement.setString(1, iddOrganization);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new LinkedOrganizationDTO(resultSet.getString("idOrganizacion"), resultSet.getString("nombre"), resultSet.getString("direccion"));
                }
            }
        }
        return null;
    }

    public List<LinkedOrganizationDTO> getAllLinkedOrganizations(Connection connection) throws SQLException {
        List<LinkedOrganizationDTO> organizations = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                organizations.add(new LinkedOrganizationDTO(resultSet.getString("idOrganizacion"), resultSet.getString("nombre"), resultSet.getString("direccion")));
            }
        }
        return organizations;
    }
}
