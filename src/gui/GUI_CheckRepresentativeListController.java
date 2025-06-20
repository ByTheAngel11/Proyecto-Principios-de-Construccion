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
import logic.DTO.DepartmentDTO;
import logic.DTO.LinkedOrganizationDTO;
import logic.DTO.RepresentativeDTO;
import logic.DAO.DepartmentDAO;
import logic.services.ServiceConfig;
import logic.services.RepresentativeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

public class GUI_CheckRepresentativeListController {

    private static final Logger logger = LogManager.getLogger(GUI_CheckRepresentativeListController.class);

    @FXML
    private Button registerRepresentativeButton;

    @FXML
    private TableColumn<RepresentativeDTO, ?> representativeEmailColumn;

    @FXML
    private TableColumn<RepresentativeDTO, ?> representativeNameColumn;

    @FXML
    private TableColumn<RepresentativeDTO, String> representativeDepartmentColumn;

    @FXML
    private TableColumn<RepresentativeDTO, String> representativeOrganizationColumn;

    @FXML
    private TableColumn<RepresentativeDTO, ?> representativeSurnameColumn;

    @FXML
    private TableColumn<RepresentativeDTO, Void> managementColumn;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML
    private Label statusLabel;

    @FXML
    private Label representativeCountsLabel;

    @FXML
    private TableView<RepresentativeDTO> tableView;

    private RepresentativeDTO selectedRepresentative;
    private RepresentativeService representativeService;
    private DepartmentDAO departmentDAO = new DepartmentDAO();

    public void initialize() {
        try {
            ServiceConfig serviceConfig = new ServiceConfig();
            representativeService = serviceConfig.getRepresentativeService();
        } catch (SQLException e) {
            logger.error("Error al inicializar el servicio del representante: {}", e.getMessage(), e);
        }

        representativeNameColumn.setCellValueFactory(new PropertyValueFactory<>("names"));
        representativeSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("surnames"));
        representativeEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        representativeDepartmentColumn.setCellValueFactory(cellData -> {
            String departmentId = cellData.getValue().getIdDepartment();
            String departmentName = getDepartmentNameById(departmentId);
            return new SimpleStringProperty(departmentName);
        });

        representativeOrganizationColumn.setCellValueFactory(cellData -> {
            String departmentId = cellData.getValue().getIdDepartment();
            String organizationName = getOrganizationNameByDepartmentId(departmentId);
            return new SimpleStringProperty(organizationName);
        });

        addManagementButtonToTable();

        loadOrganizationData();

        searchButton.setOnAction(event -> searchRepresentative());
        registerRepresentativeButton.setOnAction(event -> openRegisterRepresentativeWindow());

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedRepresentative = (RepresentativeDTO) newValue;
            tableView.refresh();
        });
    }

    private String getDepartmentNameById(String departmentId) {
        try {
            if (departmentId == null || departmentId.isEmpty()) {
                return "No asignado";
            }
            DepartmentDTO department = departmentDAO.searchDepartmentById(Integer.parseInt(departmentId));
            return (department != null) ? department.getName() : "Departamento no encontrado";
        } catch (Exception e) {
            logger.error("Error al obtener nombre de departamento: {}", e.getMessage(), e);
            return "Error";
        }
    }

    private String getOrganizationNameByDepartmentId(String departmentId) {
        try {
            if (departmentId == null || departmentId.isEmpty()) {
                return "No asignado";
            }
            DepartmentDTO department = departmentDAO.searchDepartmentById(Integer.parseInt(departmentId));
            if (department == null) {
                return "Departamento no encontrado";
            }
            ServiceConfig serviceConfig = new ServiceConfig();
            LinkedOrganizationDTO organization = serviceConfig.getLinkedOrganizationService()
                    .searchLinkedOrganizationById(String.valueOf(department.getOrganizationId()));
            return (organization != null) ? organization.getName() : "Organización no encontrada";
        } catch (Exception e) {
            logger.error("Error al obtener nombre de organización: {}", e.getMessage(), e);
            return "Error";
        }
    }

    private void openRegisterRepresentativeWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GUI_RegisterRepresentative.fxml"));
            Parent root = loader.load();

            GUI_RegisterRepresentativeController registerController = loader.getController();
            registerController.setParentController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            logger.error("Error al abrir la ventana de registro de representantes: {}", e.getMessage(), e);
        }
    }

    public void loadOrganizationData() {
        ObservableList<RepresentativeDTO> representativeList = FXCollections.observableArrayList();

        try {
            List<RepresentativeDTO> representatives = representativeService.getAllRepresentatives();
            representativeList.addAll(representatives);
            statusLabel.setText("");
        } catch (SQLException e) {
            statusLabel.setText("Error al cargar los datos de las organizaciones.");
            logger.error("Error al cargar los datos de las organizaciones: {}", e.getMessage(), e);
        }

        tableView.setItems(representativeList);
        updateRepresentativeCounts(representativeList);
    }

    private void searchRepresentative() {
        String searchQuery = searchField.getText().trim();
        if (searchQuery.isEmpty()) {
            loadOrganizationData();
            return;
        }

        ObservableList<RepresentativeDTO> filteredList = FXCollections.observableArrayList();

        try {
            RepresentativeDTO representative = representativeService.searchRepresentativeById(searchQuery);
            if (representative != null) {
                filteredList.add(representative);
            }
        } catch (SQLException e) {
            statusLabel.setText("Error al buscar la organización.");
            logger.error("Error al buscar la organización: {}", e.getMessage(), e);
        }

        tableView.setItems(filteredList);
        updateRepresentativeCounts(filteredList);
    }

    private void addManagementButtonToTable() {
        Callback<TableColumn<RepresentativeDTO, Void>, TableCell<RepresentativeDTO, Void>> cellFactory = param -> new TableCell<>() {
            private final Button manageButton = new Button("Gestionar Representante");

            {
                manageButton.setOnAction(event -> {
                    if (getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                        return;
                    }
                    RepresentativeDTO representative = getTableView().getItems().get(getIndex());
                    openManageRepresentativeWindow(representative);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || selectedRepresentative == null ||
                        getIndex() < 0 || getIndex() >= getTableView().getItems().size() ||
                        !getTableView().getItems().get(getIndex()).equals(selectedRepresentative)) {
                    setGraphic(null);
                } else {
                    setGraphic(manageButton);
                }
            }
        };

        managementColumn.setCellFactory(cellFactory);
    }

    private void openManageRepresentativeWindow(RepresentativeDTO representative) {
        try {
            GUI_ManageRepresentative.setRepresentative(representative);
            GUI_ManageRepresentative manageRepresentativeApp = new GUI_ManageRepresentative();
            Stage stage = new Stage();
            manageRepresentativeApp.start(stage);
        } catch (Exception e) {
            logger.error("Error al abrir la ventana de gestión de representante: {}", e.getMessage(), e);
        }
    }

    private void updateRepresentativeCounts(ObservableList<RepresentativeDTO> list) {
        int total = list.size();
        representativeCountsLabel.setText("Totales: " + total);
    }
}