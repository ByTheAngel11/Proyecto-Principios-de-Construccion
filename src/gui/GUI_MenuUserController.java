package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import logic.DTO.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class GUI_MenuUserController {

    private static final Logger logger = LogManager.getLogger(GUI_MenuUserController.class);

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button buttonViewStudentList;

    @FXML
    private Button buttonCheckPresentationGrade;

    @FXML
    private Button buttonEvaluatePresentation;

    @FXML
    private Button buttonViewAcademicList;

    @FXML
    private Button buttonViewOrganizationList;

    @FXML
    private Button buttonViewRepresentativeList;

    @FXML
    private Button buttonViewProjectList;

    @FXML
    private Button handleViewProjectRequest;

    @FXML
    private Button buttonViewPeriodList;

    @FXML
    private Button buttonViewGroupList;

    @FXML
    private Button buttonManageAssessmentCriteria;

    @FXML
    private Button buttonManageSelfAssessmentCriteria;

    @FXML
    private Button buttonManageActivity;

    @FXML
    private Button buttonLogout;

    private String userRole;

    public void setUserName(String userName) {
        welcomeLabel.setText("Hola, " + userName);
    }

    void setButtonVisibility(Button btn, boolean visible) {
        if (btn != null) {
            btn.setVisible(visible);
            btn.setManaged(visible);
        }
    }

    public void setUserRole(Role role) {
        this.userRole = role.name();

        if (role == Role.ACADEMICO_EVALUADOR) {
            setButtonVisibility(buttonViewStudentList, false);
            setButtonVisibility(buttonViewAcademicList, false);
            setButtonVisibility(buttonViewOrganizationList, false);
            setButtonVisibility(buttonViewRepresentativeList, false);
            setButtonVisibility(buttonViewProjectList, false);
            setButtonVisibility(handleViewProjectRequest, false);
            setButtonVisibility(buttonCheckPresentationGrade, true);
            setButtonVisibility(buttonEvaluatePresentation, true);
            setButtonVisibility(buttonViewPeriodList, false);
            setButtonVisibility(buttonViewGroupList, false);
            setButtonVisibility(buttonManageAssessmentCriteria, true);
            setButtonVisibility(buttonManageSelfAssessmentCriteria, true);
            setButtonVisibility(buttonManageActivity, true);

        } else if (role == Role.ACADEMICO) {
            setButtonVisibility(buttonViewStudentList, true);
            setButtonVisibility(buttonCheckPresentationGrade, true);
            setButtonVisibility(buttonEvaluatePresentation, false);
            setButtonVisibility(buttonViewAcademicList, false);
            setButtonVisibility(buttonViewOrganizationList, false);
            setButtonVisibility(buttonViewRepresentativeList, false);
            setButtonVisibility(buttonViewProjectList, false);
            setButtonVisibility(handleViewProjectRequest, false);
            setButtonVisibility(buttonViewPeriodList, true);
            setButtonVisibility(buttonViewGroupList, true);
            setButtonVisibility(buttonManageAssessmentCriteria, false);
            setButtonVisibility(buttonManageSelfAssessmentCriteria, false);
            setButtonVisibility(buttonManageActivity, false);

        } else if (role == Role.COORDINADOR) {
            setButtonVisibility(buttonViewStudentList, true);
            setButtonVisibility(buttonCheckPresentationGrade, true);
            setButtonVisibility(buttonEvaluatePresentation, false);
            setButtonVisibility(buttonViewAcademicList, true);
            setButtonVisibility(buttonViewOrganizationList, true);
            setButtonVisibility(buttonViewRepresentativeList, true);
            setButtonVisibility(buttonViewProjectList, true);
            setButtonVisibility(handleViewProjectRequest, true);
            setButtonVisibility(buttonViewPeriodList, true);
            setButtonVisibility(buttonViewGroupList, true);
            setButtonVisibility(buttonManageAssessmentCriteria, true);
            setButtonVisibility(buttonManageSelfAssessmentCriteria, true);
            setButtonVisibility(buttonManageActivity, true);
        }
    }

    @FXML
    private void handleCheckPresentationGrade() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_CheckPresentationGrade.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Consultar Calificaciones de Presentación");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            logger.error("Error al cargar el archivo FXML: {}", e.getMessage(), e);
        } catch (NullPointerException e) {
            logger.error("Recurso FXML no encontrado: {}", e.getMessage(), e);
        } catch (IllegalStateException e) {
            logger.error("Error en el estado de JavaFX: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error inesperado al abrir la ventana de calificaciones: {}", e.getMessage(), e);
        }
    }

    @FXML
    private void handleEvaluatePresentation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_CheckListOfPresentations.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Calificar Presentación");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            logger.error("Error al cargar el archivo FXML: {}", e.getMessage(), e);
        } catch (NullPointerException e) {
            logger.error("Recurso FXML no encontrado: {}", e.getMessage(), e);
        } catch (IllegalStateException e) {
            logger.error("Error en el estado de JavaFX: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error inesperado al abrir la ventana de evaluación: {}", e.getMessage(), e);
        }
    }

    @FXML
    private void handleViewStudentList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_CheckListOfStudents.fxml"));
            Parent root = loader.load();

            GUI_CheckListOfStudentsController controller = loader.getController();
            controller.setUserRole(Role.valueOf(userRole));

            Stage stage = new Stage();
            stage.setTitle("Lista de Estudiantes");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            logger.error("Error al cargar el archivo FXML: {}", e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            logger.error("Error al convertir el rol de usuario: {}", e.getMessage(), e);
        } catch (NullPointerException e) {
            logger.error("Recurso FXML o controlador no encontrado: {}", e.getMessage(), e);
        } catch (IllegalStateException e) {
            logger.error("Error en el estado de JavaFX: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error inesperado al abrir la ventana de lista de estudiantes: {}", e.getMessage(), e);
        }
    }

    @FXML
    private void handleViewAcademicList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_CheckAcademicList.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Lista de Académicos");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            logger.error("Error al cargar el archivo FXML: {}", e.getMessage(), e);
        } catch (NullPointerException e) {
            logger.error("Recurso FXML no encontrado: {}", e.getMessage(), e);
        } catch (IllegalStateException e) {
            logger.error("Error en el estado de JavaFX: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error inesperado al abrir la ventana de lista de académicos: {}", e.getMessage(), e);
        }
    }

    @FXML
    private void handleViewOrganizationList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_CheckListLinkedOrganization.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Lista de Organizaciones");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            logger.error("Error al cargar el archivo FXML: {}", e.getMessage(), e);
        } catch (NullPointerException e) {
            logger.error("Recurso FXML no encontrado: {}", e.getMessage(), e);
        } catch (IllegalStateException e) {
            logger.error("Error en el estado de JavaFX: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error inesperado al abrir la ventana de lista de organizaciones: {}", e.getMessage(), e);
        }
    }

    @FXML
    private void handleViewRepresentativeList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_CheckRepresentativeList.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Lista de Representantes");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            logger.error("Error al cargar el archivo FXML: {}", e.getMessage(), e);
        } catch (NullPointerException e) {
            logger.error("Recurso FXML no encontrado: {}", e.getMessage(), e);
        } catch (IllegalStateException e) {
            logger.error("Error en el estado de JavaFX: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error inesperado al abrir la ventana de lista de representantes: {}", e.getMessage(), e);
        }
    }

    @FXML
    private void handleViewProjectList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_CheckProjectList.fxml"));
            Parent root = loader.load();
            GUI_CheckProjectListController controller = loader.getController();
            controller.setRole(Role.valueOf(userRole));

            Stage stage = new Stage();
            stage.setTitle("Lista de Proyectos");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            logger.error("Error al cargar el archivo FXML: {}", e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            logger.error("Error al convertir el rol de usuario: {}", e.getMessage(), e);
        } catch (NullPointerException e) {
            logger.error("Recurso FXML o controlador no encontrado: {}", e.getMessage(), e);
        } catch (IllegalStateException e) {
            logger.error("Error en el estado de JavaFX: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error inesperado al abrir la ventana de lista de proyectos: {}", e.getMessage(), e);
        }
    }

    @FXML
    private void handleViewProjectRequest() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_CheckProjectRequestList.fxml"));
            Parent root = loader.load();
            GUI_CheckProjectRequestListController controller = loader.getController();
            controller.setUserRole(Role.valueOf(userRole));

            Stage stage = new Stage();
            stage.setTitle("Lista de Solicitudes de Prácticas");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            logger.error("Error al cargar el archivo FXML: {}", e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            logger.error("Error al convertir el rol de usuario: {}", e.getMessage(), e);
        } catch (NullPointerException e) {
            logger.error("Recurso FXML o controlador no encontrado: {}", e.getMessage(), e);
        } catch (IllegalStateException e) {
            logger.error("Error en el estado de JavaFX: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error inesperado al abrir la ventana de lista de solicitudes de prácticas: {}", e.getMessage(), e);
        }
    }

    @FXML
    private void handleViewPeriodList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_CheckListOfPeriods.fxml"));
            Parent root = loader.load();

            GUI_CheckListOfPeriodsController controller = loader.getController();
            controller.setUserRole(Role.valueOf(userRole));

            Stage stage = new Stage();
            stage.setTitle("Lista de Períodos");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            logger.error("Error al abrir la ventana de lista de períodos: {}", e.getMessage(), e);
        }
    }

    @FXML
    private void handleViewGroupList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_CheckListOfGroups.fxml"));
            Parent root = loader.load();

            GUI_CheckListOfGroupsController controller = loader.getController();
            controller.setUserRole(Role.valueOf(userRole));

            Stage stage = new Stage();
            stage.setTitle("Lista de Grupos");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            logger.error("Error al abrir la ventana de lista de grupos: {}", e.getMessage(), e);
        }
    }

    @FXML
    private void handleManageAssessmentCriteria() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_ManageAssessmentCriteria.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestión de Criterios de Evaluación");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            logger.error("Error al abrir la ventana de criterios de evaluación: {}", e.getMessage(), e);
        }
    }

    @FXML
    private void handleManageSelfAssessmentCriteria() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_ManageSelfAssessmentCriteria.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestión de Criterios de Autoevaluación");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            logger.error("Error al abrir la ventana de criterios de autoevaluación: {}", e.getMessage(), e);
        }
    }

    @FXML
    private void handleManageActivity() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_ManageActivity.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestión de Actividades");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            logger.error("Error al abrir la ventana de actividades: {}", e.getMessage(), e);
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_Login.fxml"));
            Stage stage = (Stage) buttonLogout.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Inicio de Sesión");
            stage.show();
        } catch (Exception e) {
            logger.error("Error al cerrar sesión: {}", e.getMessage(), e);
        }
    }
}