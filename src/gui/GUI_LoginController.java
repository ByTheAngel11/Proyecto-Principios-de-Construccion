package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.DTO.Role;
import logic.DTO.StudentDTO;
import logic.DTO.UserDTO;
import logic.exceptions.InvalidCredential;
import logic.services.LoginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GUI_LoginController {

    private static final Logger logger = LogManager.getLogger(GUI_LoginController.class);

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label statusLabel;

    private LoginService loginService;

    @FXML
    private void initialize() {
        try {
            this.loginService = new LoginService();
        } catch (Exception e) {
            logger.error("Error al inicializar LoginService: {}", e.getMessage(), e);
            statusLabel.setText("Error al conectar con la base de datos.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleLogin() {
        if (loginService == null) {
            logger.error("LoginService no ha sido inicializado.");
            statusLabel.setText("Error interno. Intente más tarde.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        String username = usernameField.getText();
        String password = passwordField.getText();

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
                controller.setUserRole(role); // Ahora pasas el enum

                stage.setTitle("Menú Usuario");
                stage.show();
            }
        } catch (InvalidCredential e) {
            logger.warn("Credenciales inválidas: {}", e.getMessage());
            statusLabel.setText(e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            logger.error("Error inesperado: {}", e.getMessage());
            statusLabel.setText("Ocurrió un error inesperado. Intente más tarde.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void goToRegisterProjectRequest(StudentDTO student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_CheckListOfProjects.fxml"));
            Scene scene = new Scene(loader.load());
            GUI_RegisterProjectRequestController controller = loader.getController();
            controller.setStudent(student);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Registrar Solicitud de Proyecto");
            stage.show();
        } catch (Exception e) {
            logger.error("Error al abrir la ventana de registro de solicitud de proyecto: {}", e.getMessage(), e);
            statusLabel.setText("No se pudo abrir la ventana de registro.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
}