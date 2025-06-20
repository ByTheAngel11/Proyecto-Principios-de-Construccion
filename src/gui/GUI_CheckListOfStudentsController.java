package gui;

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

import logic.DTO.ProjectDTO;
import logic.DTO.StudentDTO;
import logic.DTO.StudentProjectDTO;
import logic.DTO.UserStudentViewDTO;
import logic.services.ServiceFactory;
import logic.services.StudentService;
import logic.DTO.Role;
import logic.DAO.UserStudentViewDAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GUI_CheckListOfStudentsController {

    private static final Logger logger = LogManager.getLogger(GUI_CheckListOfStudentsController.class);

    @FXML
    private TableView<StudentDTO> tableView;

    @FXML
    private TableColumn<StudentDTO, String> tuitionColumn;

    @FXML
    private TableColumn<StudentDTO, String> namesColumn;

    @FXML
    private TableColumn<StudentDTO, String> surnamesColumn;

    @FXML
    private TableColumn<StudentDTO, String> emailColumn;

    @FXML
    private TableColumn<StudentDTO, String> nrcColumn;

    @FXML
    private TableColumn<StudentDTO, Void> detailsColumn;

    @FXML
    private TableColumn<StudentDTO, Void> managementColumn;

    @FXML
    private TextField searchField;

    @FXML
    private ChoiceBox<String> filterChoiceBox;

    @FXML
    private Button searchButton;

    @FXML
    private Button registerStudentButton;

    @FXML
    private Button assignProjectButton;

    @FXML
    private Button reassignProjectButton;

    @FXML
    private Label statusLabel, studentCountsLabel;

    private StudentDTO selectedStudent;
    private StudentService studentService;
    private StudentProjectDTO studentProject;
    private ProjectDTO currentProject;
    private Role userRole;

    private int idUserAcademic = -1;

    public void setIdUserAcademic(int idUsuario) {
        this.idUserAcademic = idUsuario;
    }

    public void initialize() {
        try {
            this.studentService = ServiceFactory.getStudentService();
        } catch (RuntimeException e) {
            logger.error("Error al inicializar StudentService: {}", e.getMessage(), e);
            statusLabel.setText("Error interno. Intente más tarde.");
            return;
        }

        filterChoiceBox.getItems().addAll("Todos", "Mis estudiantes");
        filterChoiceBox.setValue("Todos");
        filterChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> loadStudentData());

        setColumns();

        addDetailsButtonToTable();
        addManagementButtonToTable();

        loadStudentData();
        updateStudentCounts();

        searchButton.setOnAction(event -> searchStudent());
        registerStudentButton.setOnAction(event -> openRegisterStudentWindow());
        assignProjectButton.setOnAction(event -> openAssignProjectWindow());
        reassignProjectButton.setOnAction(event -> openReassignProjectWindow());

        assignProjectButton.setDisable(true);

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedStudent = newValue;
            assignProjectButton.setDisable(selectedStudent == null);
            reassignProjectButton.setDisable(selectedStudent == null);
            tableView.refresh();
        });
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
        applyRoleRestrictions();
    }

    public void setColumns () {
        tuitionColumn.setCellValueFactory(new PropertyValueFactory<>("tuition"));
        namesColumn.setCellValueFactory(new PropertyValueFactory<>("names"));
        surnamesColumn.setCellValueFactory(new PropertyValueFactory<>("surnames"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        nrcColumn.setCellValueFactory(new PropertyValueFactory<>("NRC"));
    }

    public void setButtonVisibility(Button btn, boolean visible) {
        if (btn != null) {
            btn.setVisible(visible);
            btn.setManaged(visible);
        }
    }

    public void applyRoleRestrictions() {
        if (userRole == Role.ACADEMICO_EVALUADOR) {
            setButtonVisibility(registerStudentButton, false);
            setButtonVisibility(assignProjectButton, false);
            setButtonVisibility(reassignProjectButton, false);
            managementColumn.setVisible(false);
        } else if (userRole == Role.ACADEMICO) {
            setButtonVisibility(registerStudentButton, true);
            setButtonVisibility(assignProjectButton, false);
            setButtonVisibility(reassignProjectButton, false);
            managementColumn.setVisible(true);
        } else if (userRole == Role.COORDINADOR) {
            setButtonVisibility(registerStudentButton, false);
            setButtonVisibility(assignProjectButton, true);
            setButtonVisibility(reassignProjectButton, true);
            managementColumn.setVisible(false);
        }
    }

    public void openRegisterStudentWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_RegisterStudent.fxml"));
            Parent root = loader.load();

            GUI_RegisterStudentController registerController = loader.getController();
            registerController.setParentController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            logger.error("No se pudo cargar el archivo FXML: {}", e.getMessage(), e);
        } catch (NullPointerException e) {
            logger.error("No se encontró el recurso FXML: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error al abrir la ventana de registro de estudiante: {}", e.getMessage(), e);
        }
    }

    public void openAssignProjectWindow() {
        if (selectedStudent == null) {
            statusLabel.setText("Debe seleccionar un estudiante para asignar un proyecto");
            return;
        }

        try {
            GUI_AssignProject.setStudent(selectedStudent);
            GUI_AssignProject assignProjectApp = new GUI_AssignProject();
            Stage stage = new Stage();
            assignProjectApp.start(stage);
        } catch (NullPointerException e) {
            logger.error("Recurso nulo al abrir la ventana: {}", e.getMessage(), e);
            statusLabel.setText("Error interno: recurso no encontrado");
        } catch (IllegalStateException e) {
            logger.error("Estado ilegal al abrir la ventana: {}", e.getMessage(), e);
            statusLabel.setText("Error de estado al abrir la ventana");
        } catch (Exception e) {
            logger.error("Error al abrir la ventana de asignación de proyecto: {}", e.getMessage(), e);
            statusLabel.setText("Error al abrir la ventana de asignación de proyecto");
        }
    }

    public void openReassignProjectWindow() {
        if (selectedStudent == null) {
            statusLabel.setText("Debe seleccionar un estudiante para reasignar proyecto");
            return;
        }
        ProjectDTO currentProject = null;
        try {
            StudentProjectDTO studentProjectDTO = new logic.DAO.StudentProjectDAO().searchStudentProjectByIdTuiton(selectedStudent.getTuition());
            if (studentProjectDTO != null && studentProjectDTO.getIdProject() != null && !studentProjectDTO.getIdProject().isEmpty()) {
                currentProject = new logic.DAO.ProjectDAO().searchProjectById(studentProjectDTO.getIdProject());
            }
            gui.GUI_ReassignProject.setProjectStudent(selectedStudent, currentProject);
            gui.GUI_ReassignProject reassignProjectApp = new gui.GUI_ReassignProject();
            Stage stage = new Stage();
            reassignProjectApp.start(stage);
        } catch (SQLException e) {
            logger.error("Error de base de datos al reasignar proyecto: {}", e.getMessage(), e);
            statusLabel.setText("Error de base de datos al reasignar proyecto");
        } catch (NullPointerException e) {
            logger.error("Recurso nulo al abrir la ventana: {}", e.getMessage(), e);
            statusLabel.setText("Error interno: recurso no encontrado");
        } catch (IllegalStateException e) {
            logger.error("Estado ilegal al abrir la ventana: {}", e.getMessage(), e);
            statusLabel.setText("Error de estado al abrir la ventana");
        } catch (Exception e) {
            logger.error("Error al abrir la ventana de reasignación de proyecto: {}", e.getMessage(), e);
            statusLabel.setText("Error al abrir la ventana de reasignación de proyecto");
        }
    }


    public void loadStudentData() {
        ObservableList<StudentDTO> studentList = FXCollections.observableArrayList();
        try {
            UserStudentViewDAO userStudentViewDAO = new UserStudentViewDAO();
            List<UserStudentViewDTO> userStudentViews = userStudentViewDAO.getAllUserStudentViews();
            boolean verTodos = filterChoiceBox != null && "Todos".equals(filterChoiceBox.getValue());
            for (UserStudentViewDTO userStudentView : userStudentViews) {
                if (userStudentView.isStatus() && (verTodos || userStudentView.getUserId() == idUserAcademic)) {
                    StudentDTO student = new StudentDTO(
                            userStudentView.getTuition(),
                            userStudentView.isStatus() ? 1 : 0,
                            userStudentView.getStudentNames(),
                            userStudentView.getStudentSurnames(),
                            userStudentView.getPhoneNumber(),
                            userStudentView.getEmail(),
                            userStudentView.getStudentUsername(),
                            "",
                            String.valueOf(userStudentView.getNrc()),
                            userStudentView.getCreditProgress() != null ? String.valueOf(userStudentView.getCreditProgress()) : "",
                            userStudentView.getFinalGrade() != null ? userStudentView.getFinalGrade().doubleValue() : 0.0
                    );
                    studentList.add(student);
                }
            }
            statusLabel.setText("");
        } catch (SQLException e) {
            statusLabel.setText("Error al cargar los datos de los estudiantes.");
            logger.error("Error al cargar los datos de los estudiantes: {}", e.getMessage(), e);
        }
        tableView.setItems(studentList);
        updateStudentCounts();
    }

    public void searchStudent() {
        String searchQuery = searchField.getText().trim();
        if (searchQuery.isEmpty()) {
            loadStudentData();
            return;
        }

        ObservableList<StudentDTO> filteredList = FXCollections.observableArrayList();

        try {
            UserStudentViewDAO userStudentViewDAO = new UserStudentViewDAO();
            UserStudentViewDTO userStudentView = userStudentViewDAO.getUserStudentViewByMatricula(searchQuery);
            if (userStudentView != null && userStudentView.isStatus() &&
                    (idUserAcademic == -1 || userStudentView.getUserId() == idUserAcademic)) {
                StudentDTO student = new StudentDTO(
                        userStudentView.getTuition(),
                        userStudentView.isStatus() ? 1 : 0,
                        userStudentView.getStudentNames(),
                        userStudentView.getStudentSurnames(),
                        userStudentView.getPhoneNumber(),
                        userStudentView.getEmail(),
                        userStudentView.getStudentUsername(),
                        "",
                        String.valueOf(userStudentView.getNrc()),
                        userStudentView.getCreditProgress() != null ? String.valueOf(userStudentView.getCreditProgress()) : "",
                        userStudentView.getFinalGrade() != null ? userStudentView.getFinalGrade().doubleValue() : 0.0
                );
                filteredList.add(student);
            }
        } catch (SQLException e) {
            statusLabel.setText("Error al buscar el estudiante.");
            logger.error("Error al buscar el estudiante: {}", e.getMessage(), e);
        }

        tableView.setItems(filteredList);
    }

    private void updateStudentCounts() {
        try {
            UserStudentViewDAO userStudentViewDAO = new UserStudentViewDAO();
            List<UserStudentViewDTO> userStudentViews = userStudentViewDAO.getAllUserStudentViews();
            int total = 0;
            int activos = 0;
            int inactivos = 0;
            for (UserStudentViewDTO userStudentView : userStudentViews) {
                if (idUserAcademic == -1 || userStudentView.getUserId() == idUserAcademic) {
                    total++;
                    if (userStudentView.isStatus()) {
                        activos++;
                    } else {
                        inactivos++;
                    }
                }
            }
            studentCountsLabel.setText("Totales: " + total + " | Activos: " + activos + " | Inactivos: " + inactivos);
        } catch (SQLException e) {
            studentCountsLabel.setText("Error al contar estudiantes");
            logger.error("Error al contar estudiantes: {}", e.getMessage(), e);
        }
    }

    public void addDetailsButtonToTable() {
        Callback<TableColumn<StudentDTO, Void>, TableCell<StudentDTO, Void>> cellFactory = param -> new TableCell<>() {
            private final Button detailsButton = new Button("Ver detalles");

            {
                detailsButton.setOnAction(event -> {
                    StudentDTO student = getTableView().getItems().get(getIndex());
                    openDetailsStudentWindow(student);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || selectedStudent == null || getTableView().getItems().get(getIndex()) != selectedStudent) {
                    setGraphic(null);
                } else {
                    setGraphic(detailsButton);
                }
            }
        };

        detailsColumn.setCellFactory(cellFactory);
    }

    public void openDetailsStudentWindow(StudentDTO student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_DetailsStudent.fxml"));
            Parent root = loader.load();
            GUI_DetailsStudentController controller = loader.getController();
            controller.setStudent(student);
            Stage stage = new Stage();
            stage.setTitle("Detalles del Estudiante");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            logger.error("No se pudo cargar la ventana de detalles: {}", e.getMessage(), e);
        }
    }

    public void addManagementButtonToTable() {
        Callback<TableColumn<StudentDTO, Void>, TableCell<StudentDTO, Void>> cellFactory = param -> new TableCell<>() {
            private final Button manageButton = new Button("Gestionar Estudiante");

            {
                manageButton.setOnAction(event -> {
                    StudentDTO student = getTableView().getItems().get(getIndex());
                    openManageStudentWindow(student);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || selectedStudent == null || getTableView().getItems().get(getIndex()) != selectedStudent) {
                    setGraphic(null);
                } else {
                    setGraphic(manageButton);
                }
            }
        };

        managementColumn.setCellFactory(cellFactory);
    }

    public void openManageStudentWindow(StudentDTO student) {
        ProjectDTO currentProject = null;
        try {
            StudentProjectDTO studentProjectDTO = new logic.DAO.StudentProjectDAO().searchStudentProjectByIdTuiton(student.getTuition());
            if (studentProjectDTO != null && studentProjectDTO.getIdProject() != null && !studentProjectDTO.getIdProject().isEmpty()) {
                currentProject = new logic.DAO.ProjectDAO().searchProjectById(studentProjectDTO.getIdProject());
            }
            GUI_ManageStudent.setStudent(student, currentProject);
            GUI_ManageStudent manageStudentApp = new GUI_ManageStudent();
            Stage stage = new Stage();
            manageStudentApp.start(stage);
        } catch (SQLException e) {
            logger.error("Error de base de datos al gestionar estudiante: {}", e.getMessage(), e);
        } catch (NullPointerException e) {
            logger.error("Recurso nulo al abrir la ventana de gestión de estudiante: {}", e.getMessage(), e);
        } catch (IllegalStateException e) {
            logger.error("Estado ilegal al abrir la ventana de gestión de estudiante: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error al abrir la ventana de gestión de estudiante: {}", e.getMessage(), e);
        }
    }
}