package gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logic.DAO.UserDAO;
import logic.DTO.Role;
import logic.DTO.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class GUI_ManageAcademicController implements Initializable {

    private static final Logger logger = LogManager.getLogger(GUI_ManageAcademicController.class);

    @FXML
    private TextField fieldNumberOfStaff, fieldNames, fieldSurnames, fieldUserName, fieldPassword;

    @FXML
    private ChoiceBox<Role> roleBox;

    @FXML
    private Label statusLabel;

    private UserDTO academic;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roleBox.setItems(FXCollections.observableArrayList(Role.values()));
    }

    public void setAcademicData(UserDTO academic) {
        if (academic == null) {
            logger.error("El objeto UserDTO es nulo.");
            return;
        }

        this.academic = academic;

        fieldNumberOfStaff.setText(academic.getStaffNumber() != null ? academic.getStaffNumber() : "");
        fieldNames.setText(academic.getNames() != null ? academic.getNames() : "");
        fieldSurnames.setText(academic.getSurnames() != null ? academic.getSurnames() : "");
        fieldUserName.setText(academic.getUserName() != null ? academic.getUserName() : "");
        fieldPassword.setText(academic.getPassword() != null ? academic.getPassword() : "");
        roleBox.setValue(academic.getRole() != null ? academic.getRole() : Role.ACADEMICO);
    }

    @FXML
    private void handleSaveChanges() {
        try {
            UserDAO userDAO = new UserDAO();

            if (!areFieldsFilled()) {
                throw new IllegalArgumentException("Todos los campos deben estar llenos.");
            }

            String numberOffStaff = fieldNumberOfStaff.getText();
            String names = fieldNames.getText();
            String surnames = fieldSurnames.getText();
            String userName = fieldUserName.getText();
            String password = fieldPassword.getText();
            Role role = roleBox.getValue();

            academic.setStaffNumber(numberOffStaff);
            academic.setNames(names);
            academic.setSurnames(surnames);
            academic.setUserName(userName);
            academic.setPassword(password);
            academic.setRole(role);

            boolean success = userDAO.updateUser(academic);
            if (success) {
                statusLabel.setText("¡Académico actualizado exitosamente!");
                statusLabel.setTextFill(javafx.scene.paint.Color.GREEN);
            } else {
                throw new Exception("No se pudo actualizar el académico.");
            }
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
            statusLabel.setTextFill(javafx.scene.paint.Color.RED);
            logger.error("Error: {}", e.getMessage(), e);
        }
    }

    private boolean areFieldsFilled() {
        return !fieldNumberOfStaff.getText().isEmpty() &&
                !fieldNames.getText().isEmpty() &&
                !fieldSurnames.getText().isEmpty() &&
                !fieldUserName.getText().isEmpty() &&
                !fieldPassword.getText().isEmpty() &&
                roleBox.getValue() != null;
    }

    private Role getRoleFromText(String text) {
        switch (text) {
            case "Académico":
                return Role.ACADEMICO;
            case "Académico Evaluador":
                return Role.ACADEMICO_EVALUADOR;
            case "Coordinador":
                return Role.COORDINADOR;
            default:
                throw new IllegalArgumentException("Rol no válido: " + text);
        }
    }
}