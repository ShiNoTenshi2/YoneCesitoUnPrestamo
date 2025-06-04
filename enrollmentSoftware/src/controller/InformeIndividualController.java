package controller;

import data.InformeIndividualDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Prestamo;
import model.Usuario;
import model.UsuarioConSancion;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class InformeIndividualController {

    @FXML private ComboBox<Long> comboBoxCedulasUsuarios;
    @FXML private TableView<Usuario> tablaUsuario;
    @FXML private TableColumn<Usuario, Long> columnCedulaUsuario;
    @FXML private TableColumn<Usuario, String> columnNombreUsuario;
    @FXML private TableColumn<Usuario, String> columnRolUsuario;
    @FXML private TableColumn<Usuario, String> columnEstadoUsuario;
    @FXML private TableColumn<Usuario, String> columnCorreoUsuario;

    @FXML private TableView<UsuarioConSancion> tablaSancion;
    @FXML private TableColumn<UsuarioConSancion, Integer> columnIdSancion;
    @FXML private TableColumn<UsuarioConSancion, String> columnMotivoSancion;
    @FXML private TableColumn<UsuarioConSancion, Double> columnMontoSancion;
    @FXML private TableColumn<UsuarioConSancion, String> columnEstadoSancion;

    @FXML private TableView<Prestamo> tablaPrestamo;
    @FXML private TableColumn<Prestamo, Long> columnIdPrestamo;
    @FXML private TableColumn<Prestamo, Long> columnIdSalaPrestamo;
    @FXML private TableColumn<Prestamo, Long> columnIdAudiovisualPrestamo;
    @FXML private TableColumn<Prestamo, String> columnHoraInicioPrestamo;
    @FXML private TableColumn<Prestamo, String> columnHoraFinPrestamo;
    @FXML private TableColumn<Prestamo, String> columnEstadoPrestamo;
    @FXML private TableColumn<Prestamo, String> columnDetallePrestamo;

    @FXML private Button btnInformes;
    @FXML private Button btnMainMenu;

    private InformeIndividualDAO informeDAO;

    @FXML
    public void initialize() {
        try {
            informeDAO = InformeIndividualDAO.getInstance();

            // Configurar ComboBox con cédulas
            List<Long> cedulas = informeDAO.obtenerCedulasUsuarios();
            comboBoxCedulasUsuarios.setItems(FXCollections.observableArrayList(cedulas));
            comboBoxCedulasUsuarios.setOnAction(event -> actualizarTablas());

            // Configurar columnas de tablaUsuario
            columnCedulaUsuario.setCellValueFactory(new PropertyValueFactory<>("cedulaUsuario"));
            columnNombreUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
            columnRolUsuario.setCellValueFactory(new PropertyValueFactory<>("rol"));
            columnEstadoUsuario.setCellValueFactory(new PropertyValueFactory<>("estado"));
            columnCorreoUsuario.setCellValueFactory(new PropertyValueFactory<>("correo"));

            // Configurar columnas de tablaSancion
            columnIdSancion.setCellValueFactory(new PropertyValueFactory<>("idSancion"));
            columnMotivoSancion.setCellValueFactory(new PropertyValueFactory<>("motivoSancion"));
            columnMontoSancion.setCellValueFactory(new PropertyValueFactory<>("montoSancion"));
            columnEstadoSancion.setCellValueFactory(new PropertyValueFactory<>("estadoSancion"));

            // Configurar columnas de tablaPrestamo
            columnIdPrestamo.setCellValueFactory(new PropertyValueFactory<>("id_prestamo"));
            columnIdSalaPrestamo.setCellValueFactory(new PropertyValueFactory<>("id_sala"));
            columnIdAudiovisualPrestamo.setCellValueFactory(new PropertyValueFactory<>("id_audiovisual"));
            columnHoraInicioPrestamo.setCellValueFactory(new PropertyValueFactory<>("hora_inicio"));
            columnHoraFinPrestamo.setCellValueFactory(new PropertyValueFactory<>("hora_fin"));
            columnEstadoPrestamo.setCellValueFactory(new PropertyValueFactory<>("estado"));
            columnDetallePrestamo.setCellValueFactory(new PropertyValueFactory<>("detalle_prestamo"));

            // Cargar datos iniciales si hay una cédula seleccionada
            if (!cedulas.isEmpty()) {
                comboBoxCedulasUsuarios.setValue(cedulas.get(0));
                actualizarTablas();
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    private void actualizarTablas() {
        Long cedulaSeleccionada = comboBoxCedulasUsuarios.getValue();
        if (cedulaSeleccionada != null) {
            try {
                // Actualizar tablaUsuario
                Usuario usuario = informeDAO.obtenerUsuarioPorCedula(cedulaSeleccionada);
                tablaUsuario.setItems(FXCollections.observableArrayList(usuario != null ? List.of(usuario) : List.of()));

                // Actualizar tablaSancion
                List<UsuarioConSancion> sanciones = informeDAO.obtenerSancionesPorCedula(cedulaSeleccionada);
                tablaSancion.setItems(FXCollections.observableArrayList(sanciones));

                // Actualizar tablaPrestamo
                List<Prestamo> prestamos = informeDAO.obtenerPrestamosPorCedula(cedulaSeleccionada);
                tablaPrestamo.setItems(FXCollections.observableArrayList(prestamos));
            } catch (SQLException e) {
                System.err.println("Error al actualizar tablas: " + e.getMessage());
            }
        }
    }

    @FXML
    private void GoToInformes() {
        loadView("/view/Informes.fxml", "Menú de Informes");
    }

    @FXML
    private void GoToMainMenu() {
        loadView("/view/MainMenu.fxml", "Menú Principal");
    }

    private void loadView(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) btnMainMenu.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la vista: " + e.getMessage());
        }
    }
}