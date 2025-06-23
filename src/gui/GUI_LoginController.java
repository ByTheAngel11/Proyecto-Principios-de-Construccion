package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import logic.DTO.Role;
import logic.DTO.StudentDTO;
import logic.DTO.UserDTO;
import logic.exceptions.InactiveUser;
import logic.exceptions.InvalidCredential;
import logic.services.LoginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class GUI_LoginController {

    private static final Logger LOGGER = LogManager.getLogger(GUI_LoginController.class);

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordVisibleField;

    @FXML
    private Button togglePasswordVisibilityButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label statusLabel;

    private boolean isPasswordVisible = false;

    private LoginService loginService;

    @FXML
    private void initialize() {
        try {
            this.loginService = new LoginService();
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            if (sqlState != null && sqlState.equals("08001")) {
                LOGGER.error("Error de conexión a la base de datos: {}", e.getMessage(), e);
                statusLabel.setText("Error de conexión a la base de datos.");
                statusLabel.setTextFill(Color.RED);
            } else if (sqlState != null && sqlState.equals("08S01")) {
                LOGGER.error("Error de conexión interrumpida a la base de datos: {}", e.getMessage(), e);
                statusLabel.setText("Error de conexión interrumpida a la base de datos.");
                statusLabel.setTextFill(Color.RED);
            } else if (sqlState != null && sqlState.equals("42S02")) {
                LOGGER.error("Tabla de usuarios no encontrada: {}", e.getMessage(), e);
                statusLabel.setText("Tabla de usuarios no encontrada.");
                statusLabel.setTextFill(Color.RED);
            } else if (sqlState != null && sqlState.equals("42S22")) {
                LOGGER.error("Columna de usuario no encontrada: {}", e.getMessage(), e);
                statusLabel.setText("Columna de usuario no encontrada.");
                statusLabel.setTextFill(Color.RED);
            } else if (sqlState != null && sqlState.equals("HY000")) {
                LOGGER.error("Error general de la base de datos: {}", e.getMessage(), e);
                statusLabel.setText("Error general de la base de datos.");
                statusLabel.setTextFill(Color.RED);
            } else if (sqlState != null && sqlState.equals("42000")) {
                LOGGER.error("Base de datos desconocida: {}", e.getMessage(), e);
                statusLabel.setText("Base de datos desconocida.");
                statusLabel.setTextFill(Color.RED);
            } else if (sqlState != null && sqlState.equals("28000")) {
                LOGGER.error("Acceso denegado a la base de datos: {}", e.getMessage(), e);
                statusLabel.setText("Acceso denegado a la base de datos.");
                statusLabel.setTextFill(Color.RED);
            } else {
                LOGGER.error("Error de base de datos al inicializar LoginService: {}", e.getMessage(), e);
                statusLabel.setText("Error de base de datos al inicializar el servicio de inicio de sesión.");
                statusLabel.setTextFill(Color.RED);
            }
        } catch (Exception e) {
            LOGGER.error("Error inesperado al inicializar LoginService: {}", e.getMessage(), e);
            statusLabel.setText("Error inesperado al conectar con la base de datos.");
            statusLabel.setTextFill(Color.RED);
        }
        configurePasswordVisibility();
    }

    private void configurePasswordVisibility() {
        togglePasswordVisibilityButton.setText("🙈");
        togglePasswordVisibilityButton.setOnAction(event -> togglePasswordVisibility());
    }

    @FXML
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordField.setText(passwordVisibleField.getText());
            passwordVisibleField.setVisible(false);
            passwordVisibleField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            togglePasswordVisibilityButton.setText("🙈");
        } else {
            passwordVisibleField.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            passwordVisibleField.setVisible(true);
            passwordVisibleField.setManaged(true);
            togglePasswordVisibilityButton.setText("👁");
        }
        isPasswordVisible = !isPasswordVisible;
    }

    @FXML
    private void handleLogin() {
        if (loginService == null) {
            LOGGER.error("LoginService no ha sido inicializado.");
            statusLabel.setText("Error interno. Intente más tarde.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        String username = usernameField.getText();
        String password = isPasswordVisible ? passwordVisibleField.getText() : passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Por favor, completa todos los campos.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            Object user = loginService.login(username, password);

            if (user instanceof StudentDTO) {
                StudentDTO student = (StudentDTO) user;
                statusLabel.setText("Bienvenido estudiante, " + student.getNames() + "!");
                statusLabel.setStyle("-fx-text-fill: green;");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_MenuStudent.fxml"));
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));

                GUI_MenuStudentController controller = loader.getController();
                controller.setStudent(student);
                controller.setStudentName(student.getNames());
                controller.setProfileImage();
                controller.setStudentService(new logic.services.StudentService());

                stage.setTitle("Menú Estudiante");
                stage.show();

            } else if (user instanceof UserDTO) {
                UserDTO generalUser = (UserDTO) user;
                Role role = generalUser.getRole();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_MenuUser.fxml"));
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));

                GUI_MenuUserController controller = loader.getController();
                controller.setUserName(generalUser.getNames());
                controller.setUserRole(role);
                controller.setActualUserId(Integer.parseInt(generalUser.getIdUser()));

                stage.setTitle("Menú Usuario");
                stage.show();
            }
        } catch (InvalidCredential e) {
            LOGGER.warn("Credenciales inválidas: {}", e.getMessage());
            statusLabel.setText(e.getMessage());
            statusLabel.setTextFill(Color.RED);
        } catch (InactiveUser e) {
            LOGGER.warn("Usuario inactivo o dado de baja: {}", e.getMessage());
            statusLabel.setText(e.getMessage());
            statusLabel.setTextFill(Color.RED);
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            if (sqlState != null && sqlState.equals("08001")) {
                LOGGER.error("Error de conexión a la base de datos: {}", e.getMessage(), e);
                statusLabel.setText("Error de conexión a la base de datos.");
                statusLabel.setTextFill(Color.RED);
            } else if (sqlState != null && sqlState.equals("08S01")) {
                LOGGER.error("Error de conexión interrumpida a la base de datos: {}", e.getMessage(), e);
                statusLabel.setText("Error de conexión interrumpida a la base de datos.");
                statusLabel.setTextFill(Color.RED);
            } else if (sqlState != null && sqlState.equals("42S02")) {
                LOGGER.error("Tabla de usuarios no encontrada: {}", e.getMessage(), e);
                statusLabel.setText("Tabla de usuarios no encontrada.");
                statusLabel.setTextFill(Color.RED);
            } else if (sqlState != null && sqlState.equals("42S22")) {
                LOGGER.error("Columna de usuario no encontrada: {}", e.getMessage(), e);
                statusLabel.setText("Columna de usuario no encontrada.");
                statusLabel.setTextFill(Color.RED);
            } else if (sqlState != null && sqlState.equals("HY000")) {
                LOGGER.error("Error general de la base de datos: {}", e.getMessage(), e);
                statusLabel.setText("Error general de la base de datos.");
                statusLabel.setTextFill(Color.RED);
            } else if (sqlState != null && sqlState.equals("42000")) {
                LOGGER.error("Base de datos desconocida: {}", e.getMessage(), e);
                statusLabel.setText("Base de datos desconocida.");
                statusLabel.setTextFill(Color.RED);
            } else if (sqlState != null && sqlState.equals("28000")) {
                LOGGER.error("Acceso denegado a la base de datos: {}", e.getMessage(), e);
                statusLabel.setText("Acceso denegado a la base de datos.");
                statusLabel.setTextFill(Color.RED);
            } else {
                LOGGER.error("Error de base de datos al iniciar sesión: {}", e.getMessage(), e);
                statusLabel.setText("Error al iniciar sesión. Intente más tarde.");
                statusLabel.setTextFill(Color.RED);
            }
        } catch (IOException e) {
            LOGGER.error("Error al cargar la interfaz de usuario: {}", e.getMessage(), e);
            statusLabel.setText("Error al cargar la interfaz de usuario.");
            statusLabel.setTextFill(Color.RED);
        } catch (Exception e) {
            LOGGER.error("Error inesperado: {}", e.getMessage(), e);
            statusLabel.setText("Ocurrió un error inesperado. Intente más tarde.");
            statusLabel.setTextFill(Color.RED);
        }
    }
    
}