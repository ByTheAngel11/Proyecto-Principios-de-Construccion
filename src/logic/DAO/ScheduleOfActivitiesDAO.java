package logic.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.DTO.ScheduleOfActivitiesDTO;
import logic.interfaces.IScheduleOfActivitiesDAO;

public class ScheduleOfActivitiesDAO implements IScheduleOfActivitiesDAO {
    private final static String SQL_INSERT = "INSERT INTO cronograma_de_actividades (idCronograma, hito, fechaEstimada, matricula, idEvidencia) VALUES (?, ?, ?, ?, ?)";
    private final static String SQL_UPDATE = "UPDATE cronograma_de_actividades SET hito = ?, fechaEstimada = ?, matricula = ?, idEvidencia = ? WHERE idCronograma = ?";
    private final static String SQL_DELETE = "DELETE FROM cronograma_de_actividades WHERE idCronograma = ?";
    private final static String SQL_SELECT = "SELECT * FROM cronograma_de_actividades WHERE idCronograma = ?";
    private final static String SQL_SELECT_ALL = "SELECT * FROM cronograma_de_actividades";

    public boolean insertScheduleOfActivities(ScheduleOfActivitiesDTO schedule, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
            statement.setString(1, schedule.getIdSchedule());
            statement.setString(2, schedule.getMilestone());
            statement.setTimestamp(3, schedule.getEstimatedDate());
            statement.setString(4, schedule.getTuiton());
            statement.setString(5, schedule.getIdEvidence());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean updateScheduleOfActivities(ScheduleOfActivitiesDTO schedule, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, schedule.getMilestone());
            statement.setTimestamp(2, schedule.getEstimatedDate());
            statement.setString(3, schedule.getTuiton());
            statement.setString(4, schedule.getIdEvidence());
            statement.setString(5, schedule.getIdSchedule());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteScheduleOfActivities(ScheduleOfActivitiesDTO schedule, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setString(1, schedule.getIdSchedule());
            return statement.executeUpdate() > 0;
        }
    }

    public ScheduleOfActivitiesDTO searchScheduleOfActivitiesById(String idSchedule, Connection connection) throws SQLException {
        ScheduleOfActivitiesDTO schedule = new ScheduleOfActivitiesDTO(
                "N/A",
                "N/A",
                java.sql.Timestamp.valueOf("0404-01-01 00:00:00"),
                "N/A",
                "N/A"
        );
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT)) {
            statement.setString(1, idSchedule);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    schedule = new ScheduleOfActivitiesDTO(
                        resultSet.getString("idCronograma"),
                        resultSet.getString("hito"),
                        resultSet.getTimestamp("fechaEstimada"),
                        resultSet.getString("matricula"),
                        resultSet.getString("idEvidencia")
                    );
                }
            }
        }
        return schedule;
    }

    public List<ScheduleOfActivitiesDTO> getAllSchedulesOfActivities(Connection connection) throws SQLException {
        List<ScheduleOfActivitiesDTO> schedules = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                schedules.add(new ScheduleOfActivitiesDTO(
                    resultSet.getString("idCronograma"),
                    resultSet.getString("hito"),
                    resultSet.getTimestamp("fechaEstimada"),
                    resultSet.getString("matricula"),
                    resultSet.getString("idEvidencia")
                ));
            }
        }
        return schedules;
    }

    //TODO hacer metodo para verificar si existe un cronograma de actividades
}
