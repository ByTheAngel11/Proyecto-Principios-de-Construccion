package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logic.DTO.GroupDTO;
import logic.DAO.GroupDAO;
import logic.DAO.UserDAO;
import logic.DAO.PeriodDAO;
import logic.DTO.UserDTO;
import logic.DTO.Role;
import logic.DTO.PeriodDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class GUI_RegisterGroupController {

    private static final Logger logger = LogManager.getLogger(GUI_RegisterGroupController.class);

    @FXML
    private Label statusLabel;

    @FXML
    private TextField fieldNRC, fieldName;

    @FXML
    private ChoiceBox<UserDTO> choiceBoxAcademico;

    @FXML
    private ChoiceBox<PeriodDTO> choiceBoxPeriodo;

    @FXML
    private Button registerButton;

    private ObservableList<UserDTO> academicosList = FXCollections.observableArrayList();
    private ObservableList<PeriodDTO> periodosList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cargarAcademicos();
        cargarPeriodos();
        registerButton.setOnAction(event -> handleRegisterGroup());
    }

    private void cargarAcademicos() {
        try {
            UserDAO userDAO = new UserDAO();
            List<UserDTO> academicos = userDAO.getAllUsers().stream()
                    .filter(user -> user.getRole() == Role.ACADEMICO)
                    .collect(Collectors.toList());
            academicosList.setAll(academicos);
            choiceBoxAcademico.setItems(academicosList);
            choiceBoxAcademico.setConverter(new javafx.util.StringConverter<UserDTO>() {
                @Override
                public String toString(UserDTO user) {
                    return user == null ? "" : user.getNames() + " " + user.getSurnames();
                }
                @Override
                public UserDTO fromString(String string) {
                    return null;
                }
            });
        } catch (Exception e) {
            logger.error("Error al cargar académicos: {}", e.getMessage(), e);
            statusLabel.setText("Error al cargar académicos.");
        }
    }

    private void cargarPeriodos() {
        try {
            PeriodDAO periodDAO = new PeriodDAO();
            List<PeriodDTO> periodos = periodDAO.getAllPeriods();
            periodosList.setAll(periodos);
            choiceBoxPeriodo.setItems(periodosList);
            choiceBoxPeriodo.setConverter(new javafx.util.StringConverter<PeriodDTO>() {
                @Override
                public String toString(PeriodDTO period) {
                    return period == null ? "" : period.getName();
                }
                @Override
                public PeriodDTO fromString(String string) {
                    return null;
                }
            });
        } catch (SQLException e) {
            logger.error("Error de base de datos al cargar periodos: {}", e.getMessage(), e);
            statusLabel.setText("Error de base de datos al cargar periodos.");
        } catch (Exception e) {
            logger.error("Error al cargar periodos: {}", e.getMessage(), e);
            statusLabel.setText("Error al cargar periodos.");
        }
    }

    @FXML
    private void handleRegisterGroup() {
        try {
            if (!areFieldsFilled()) {
                statusLabel.setText("Todos los campos deben estar llenos.");
                statusLabel.setTextFill(javafx.scene.paint.Color.RED);
                return;
            }

            UserDTO academicoSeleccionado = choiceBoxAcademico.getValue();
            PeriodDTO periodoSeleccionado = choiceBoxPeriodo.getValue();

            GroupDTO group = new GroupDTO(
                    fieldNRC.getText(),
                    fieldName.getText(),
                    academicoSeleccionado.getIdUser(),
                    periodoSeleccionado.getIdPeriod()
            );

            GroupDAO groupDAO = new GroupDAO();
            boolean success = groupDAO.insertGroup(group);

            if (success) {
                statusLabel.setText("¡Grupo registrado exitosamente!");
                statusLabel.setTextFill(javafx.scene.paint.Color.GREEN);
            } else {
                statusLabel.setText("No se pudo registrar el grupo.");
                statusLabel.setTextFill(javafx.scene.paint.Color.RED);
            }
        } catch (SQLException e) {
            logger.error("Error de base de datos al registrar el grupo: {}", e.getMessage(), e);
            statusLabel.setText("Error de base de datos al registrar el grupo.");
            statusLabel.setTextFill(javafx.scene.paint.Color.RED);
        } catch (NullPointerException e) {
            logger.error("Referencia nula al registrar el grupo: {}", e.getMessage(), e);
            statusLabel.setText("Error interno al registrar el grupo.");
            statusLabel.setTextFill(javafx.scene.paint.Color.RED);
        } catch (Exception e) {
            logger.error("Error al registrar el grupo: {}", e.getMessage(), e);
            statusLabel.setText("Ocurrió un error inesperado. Intente más tarde.");
            statusLabel.setTextFill(javafx.scene.paint.Color.RED);
        }
    }

    private boolean areFieldsFilled() {
        return !fieldNRC.getText().isEmpty() &&
                !fieldName.getText().isEmpty() &&
                choiceBoxAcademico.getValue() != null &&
                choiceBoxPeriodo.getValue() != null;
    }
}