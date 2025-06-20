package gui;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import logic.DTO.StudentDTO;
import javafx.scene.control.Button;
import logic.DTO.StudentProjectDTO;
import logic.DTO.ProjectDTO;
import logic.DTO.LinkedOrganizationDTO;
import logic.DAO.StudentProjectDAO;
import logic.DAO.ProjectDAO;
import logic.DAO.LinkedOrganizationDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class GUI_DetailsStudentController {

    private static final Logger LOGGER = LogManager.getLogger(GUI_DetailsStudentController.class);

    @FXML
    private Label tuitionLabel;
    @FXML
    private Label namesLabel;
    @FXML
    private Label surnamesLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label NRCLabel;
    @FXML
    private Label projectNameLabel;
    @FXML
    private Label projectDescriptionLabel;
    @FXML
    private Label projectOrganizationLabel;
    @FXML
    private Button checkSelfAssessmentButton;

    private HostServices hostServices;

    public void setStudent(StudentDTO student) {
        if (student != null) {
            tuitionLabel.setText(student.getTuition());
            namesLabel.setText(student.getNames());
            surnamesLabel.setText(student.getSurnames());
            emailLabel.setText(student.getEmail());
            NRCLabel.setText(student.getNRC());

            showAssignedProject(student);
        }
    }

    private void showAssignedProject(StudentDTO student) {
        try {
            StudentProjectDAO studentProjectDAO = new StudentProjectDAO();
            StudentProjectDTO studentProject = studentProjectDAO.searchStudentProjectByIdTuiton(student.getTuition());
            if (studentProject != null && studentProject.getIdProject() != null && !studentProject.getIdProject().isEmpty()) {
                ProjectDAO projectDAO = new ProjectDAO();
                ProjectDTO project = projectDAO.searchProjectById(studentProject.getIdProject());
                if (project != null) {
                    projectNameLabel.setText(project.getName());
                    projectDescriptionLabel.setText(project.getDescription());

                    LinkedOrganizationDAO orgDAO = new LinkedOrganizationDAO();
                    LinkedOrganizationDTO org = orgDAO.searchLinkedOrganizationById(String.valueOf(project.getIdOrganization()));
                    projectOrganizationLabel.setText(org != null ? org.getName() : "N/A");
                } else {
                    projectNameLabel.setText("No asignado");
                    projectDescriptionLabel.setText("-");
                    projectOrganizationLabel.setText("-");
                }
            } else {
                projectNameLabel.setText("No asignado");
                projectDescriptionLabel.setText("-");
                projectOrganizationLabel.setText("-");
            }
        } catch (SQLException e) {
            LOGGER.error("Error de base de datos al obtener el proyecto asignado: {}", e.getMessage(), e);
            projectNameLabel.setText("Error BD");
            projectDescriptionLabel.setText("Error BD");
            projectOrganizationLabel.setText("Error BD");
        } catch (NullPointerException e) {
            LOGGER.error("Referencia nula al obtener el proyecto asignado: {}", e.getMessage(), e);
            projectNameLabel.setText("Error");
            projectDescriptionLabel.setText("Error");
            projectOrganizationLabel.setText("Error");
        } catch (Exception e) {
            LOGGER.error("Error inesperado al obtener el proyecto asignado: {}", e.getMessage(), e);
            projectNameLabel.setText("Error");
            projectDescriptionLabel.setText("Error");
            projectOrganizationLabel.setText("Error");
        }
    }

    @FXML
    private void handleCheckReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_CheckListOfReports.fxml"));
            Parent root = loader.load();

            GUI_CheckListOfReportsController controller = loader.getController();

            controller.setStudentTuition(tuitionLabel.getText()); // Luego matrícula

            Stage stage = new Stage();
            stage.setTitle("Lista de Reportes");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            LOGGER.error("No se pudo abrir la ventana de reportes: {}", e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error("Error inesperado al abrir la ventana de reportes: {}", e.getMessage(), e);
        }
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    private void handleCheckSelfAssessment() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_CheckSelfAssessment.fxml"));
            Parent root = loader.load();
            GUI_CheckSelfAssessmentController controller = loader.getController();
            controller.setStudentTuition(tuitionLabel.getText());

            Stage stage = new Stage();
            stage.setTitle("Autoevaluación");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            LOGGER.error("No se pudo abrir la ventana de autoevaluación: {}", e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error("Error inesperado al abrir la ventana de autoevaluación: {}", e.getMessage(), e);
        }
    }

    @FXML
    private void handleCheckPresentationGrade() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_CheckPresentationGrade.fxml"));
            Parent root = loader.load();

            GUI_CheckPresentationGradeController controller = loader.getController();
            // Crea el StudentDTO a partir de los datos actuales o guarda el StudentDTO en una variable de instancia
            StudentDTO student = new StudentDTO();
            student.setTuition(tuitionLabel.getText());
            student.setNames(namesLabel.getText());
            student.setSurnames(surnamesLabel.getText());
            student.setEmail(emailLabel.getText());
            student.setNRC(NRCLabel.getText());
            controller.setStudent(student);

            Stage stage = new Stage();
            stage.setTitle("Calificaciones de Presentación");
            stage.setScene(new Scene(root));
            stage.show();
        }catch (IOException e) {
            LOGGER.error("No se pudo abrir la ventana de Calificaciones de Presentacion: {}", e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error("Error inesperado al abrir la ventana de Calificaciones de Presentacion: {}", e.getMessage(), e);
        }
    }
}