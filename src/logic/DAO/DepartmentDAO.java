package logic.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import data_access.ConnectionDataBase;
import logic.DTO.DepartmentDTO;
import logic.interfaces.IDeparmentDAO;

public class DepartmentDAO implements IDeparmentDAO {

    private static final String SQL_INSERT = "INSERT INTO departamento (nombre, descripcion, idOrganizacion) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE departamento SET nombre = ?, descripcion = ?, idOrganizacion = ? WHERE idDepartamento = ?";
    private static final String SQL_DELETE = "DELETE FROM departamento WHERE idDepartamento = ?";
    private static final String SQL_SELECT = "SELECT * FROM departamento WHERE idDepartamento = ?";
    private static final String SQL_SELECT_ORGANIZACION_ID_BY_DEPARTMENT_ID = "SELECT idOrganizacion FROM departamento WHERE idDepartamento = ?";
    private static final String SQL_SELECT_BY_ORGANIZATION_ID = "SELECT * FROM departamento WHERE idOrganizacion = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM departamento";

    public boolean insertDepartment(DepartmentDTO department) throws SQLException {
        try (ConnectionDataBase connectionDataBase = new ConnectionDataBase();
             Connection connection = connectionDataBase.connectDB();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
            statement.setString(1, department.getName());
            statement.setString(2, department.getDescription());
            statement.setInt(3, department.getOrganizationId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean updateDepartment(DepartmentDTO department) throws SQLException {
        try (ConnectionDataBase connectionDataBase = new ConnectionDataBase();
             Connection connection = connectionDataBase.connectDB();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, department.getName());
            statement.setString(2, department.getDescription());
            statement.setInt(3, department.getOrganizationId());
            statement.setInt(4, department.getDepartmentId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteDepartment(int departmentId) throws SQLException {
        try (ConnectionDataBase connectionDataBase = new ConnectionDataBase();
             Connection connection = connectionDataBase.connectDB();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setInt(1, departmentId);
            return statement.executeUpdate() > 0;
        }
    }

    public DepartmentDTO searchDepartmentById(int departmentId) throws SQLException {
        DepartmentDTO department = new DepartmentDTO(-1, "N/A", "N/A", -1);
        try (ConnectionDataBase connectionDataBase = new ConnectionDataBase();
             Connection connection = connectionDataBase.connectDB();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT)) {
            statement.setInt(1, departmentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    department = new DepartmentDTO(
                            resultSet.getInt("idDepartamento"),
                            resultSet.getString("nombre"),
                            resultSet.getString("descripcion"),
                            resultSet.getInt("idOrganizacion")
                    );
                }
            }
        }
        return department;
    }

    public int getOrganizationIdByDepartmentId(int departmentId) throws SQLException {
        int organizationId = -1; // Default value if not found
        try (ConnectionDataBase connectionDataBase = new ConnectionDataBase();
             Connection connection = connectionDataBase.connectDB();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ORGANIZACION_ID_BY_DEPARTMENT_ID)) {
            statement.setInt(1, departmentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    organizationId = resultSet.getInt("idOrganizacion");
                }
            }
        }
        return organizationId;
    }

    public List<DepartmentDTO> getAllDepartmentsByOrganizationId(int organizationId) throws SQLException {
        List<DepartmentDTO> departments = new ArrayList<>();
        try (ConnectionDataBase connectionDataBase = new ConnectionDataBase();
             Connection connection = connectionDataBase.connectDB();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ORGANIZATION_ID)) {
            statement.setInt(1, organizationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    departments.add(new DepartmentDTO(
                            resultSet.getInt("idDepartamento"),
                            resultSet.getString("nombre"),
                            resultSet.getString("descripcion"),
                            resultSet.getInt("idOrganizacion")
                    ));
                }
            }
        }
        return departments;
    }

    public List<DepartmentDTO> getAllDepartments() throws SQLException {
        List<DepartmentDTO> departments = new ArrayList<>();
        try (ConnectionDataBase connectionDataBase = new ConnectionDataBase();
             Connection connection = connectionDataBase.connectDB();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                departments.add(new DepartmentDTO(
                        resultSet.getInt("idDepartamento"),
                        resultSet.getString("nombre"),
                        resultSet.getString("descripcion"),
                        resultSet.getInt("idOrganizacion")
                ));
            }
        }
        return departments;
    }
}