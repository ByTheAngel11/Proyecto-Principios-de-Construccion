package gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.DAO.EvaluationPresentationDAO;
import logic.DTO.EvaluationPresentationDTO;
import logic.DTO.StudentDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class GUI_CheckPresentationGradeController {

    @FXML
    private TableView<EvaluationPresentationDTO> presentationGradeTableView;
    @FXML
    private TableColumn<EvaluationPresentationDTO, Integer> idEvaluationColumn;
    @FXML
    private TableColumn<EvaluationPresentationDTO, Integer> idPresentationColumn;
    @FXML
    private TableColumn<EvaluationPresentationDTO, String> dateColumn;
    @FXML
    private TableColumn<EvaluationPresentationDTO, Double> averageColumn;
    @FXML
    private TableColumn<EvaluationPresentationDTO, Void> seeDetailsColumn;
    @FXML
    private Label statusLabel;
    @FXML
    private Label evaluationCountsLabel;

    private static final Logger logger = LogManager.getLogger(GUI_CheckPresentationGradeController.class);

    private StudentDTO student;

    public void setStudent(StudentDTO student) {
        this.student = student;
        loadEvaluations();
    }

    @FXML
    public void initialize() {
        idEvaluationColumn.setCellValueFactory(new PropertyValueFactory<>("idEvaluation"));
        idPresentationColumn.setCellValueFactory(new PropertyValueFactory<>("idProject"));
        dateColumn.setCellValueFactory(cellData -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return new SimpleStringProperty(cellData.getValue().getDate() != null ? sdf.format(cellData.getValue().getDate()) : "");
        });
        averageColumn.setCellValueFactory(new PropertyValueFactory<>("average"));

        addViewDetailsButtonToTable();
    }

    private void addViewDetailsButtonToTable() {
        Callback<TableColumn<EvaluationPresentationDTO, Void>, TableCell<EvaluationPresentationDTO, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btn = new Button("Ver Detalles");

            {
                btn.setOnAction(event -> {
                    EvaluationPresentationDTO evaluation = getTableView().getItems().get(getIndex());
                    showDetailsDialog(evaluation);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        };
        seeDetailsColumn.setCellFactory(cellFactory);

        presentationGradeTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> presentationGradeTableView.refresh());
    }

    private void showDetailsDialog(EvaluationPresentationDTO evaluation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_DetailsPresentationStudent.fxml"));
            Parent root = loader.load();

            GUI_DetailsPresentationStudentController controller = loader.getController();
            controller.setIdEvaluation(evaluation.getIdEvaluation());

            Stage stage = new Stage();
            stage.setTitle("Detalles de la Evaluación");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            logger.error("Error al abrir la ventana de detalles de la presentación: {}", e.getMessage(), e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana de detalles.");
            alert.showAndWait();
        }
    }

    private void loadEvaluations() {
        try {
            presentationGradeTableView.getItems().clear();
            statusLabel.setText("");

            if (student == null) {
                statusLabel.setText("No se encontró información del estudiante.");
                updateEvaluationCounts(FXCollections.observableArrayList());
                return;
            }

            EvaluationPresentationDAO evaluationDAO = new EvaluationPresentationDAO();
            List<EvaluationPresentationDTO> studentEvaluations = evaluationDAO.getEvaluationPresentationsByTuiton(student.getTuition());

            ObservableList<EvaluationPresentationDTO> data = FXCollections.observableArrayList(studentEvaluations);

            if (studentEvaluations == null || studentEvaluations.isEmpty()) {
                statusLabel.setText("No tienes evaluaciones de presentación registradas.");
            }
            presentationGradeTableView.setItems(data);
            updateEvaluationCounts(data);

        } catch (SQLException e) {
            logger.error("Error de base de datos al cargar las evaluaciones: {}", e.getMessage(), e);
            statusLabel.setText("Error de base de datos al cargar las evaluaciones.");
            updateEvaluationCounts(FXCollections.observableArrayList());
        } catch (Exception e) {
            logger.error("Error inesperado al cargar las evaluaciones: {}", e.getMessage(), e);
            statusLabel.setText("Error inesperado al cargar las evaluaciones.");
            updateEvaluationCounts(FXCollections.observableArrayList());
        }
    }

    private void updateEvaluationCounts(ObservableList<EvaluationPresentationDTO> list) {
        int total = list.size();
        evaluationCountsLabel.setText("Totales: " + total);
    }
}