package controller;

import data.InformeDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class InformeGeneralController {

    @FXML private TableView<Usuario> tablaUsuariosSinSancion;
    @FXML private TableColumn<Usuario, Long> columnCedulaUsuario;
    @FXML private TableColumn<Usuario, String> columnNombreUsuario;
    @FXML private TableColumn<Usuario, String> columnRolUsuario;
    @FXML private TableColumn<Usuario, String> columnEstadoUsuario;
    @FXML private TableColumn<Usuario, String> columnCorreoUsuario;

    @FXML private TableView<UsuarioConSancion> tablaUsuariosConSancion;
    @FXML private TableColumn<UsuarioConSancion, Integer> columnIdSancion;
    @FXML private TableColumn<UsuarioConSancion, Long> columnCedulaUsuarioSancion;
    @FXML private TableColumn<UsuarioConSancion, String> columnNombreUsuarioSancion;
    @FXML private TableColumn<UsuarioConSancion, String> columnEstadoUsuarioSancion;
    @FXML private TableColumn<UsuarioConSancion, Double> columnMontoSancion;
    @FXML private TableColumn<UsuarioConSancion, String> columnMotivoSancion;

    @FXML private TableView<Prestamo> tablaPrestamosAprobados;
    @FXML private TableColumn<Prestamo, Long> columnIdPrestamoAprobado;
    @FXML private TableColumn<Prestamo, Long> columnCedulaPrestamoAprobado;
    @FXML private TableColumn<Prestamo, Long> columnIdSalaPrestamoAprobado;
    @FXML private TableColumn<Prestamo, Long> columnIdAudiovisualPrestamoAprobado;
    @FXML private TableColumn<Prestamo, String> columnHoraInicioPrestamoAprobado;
    @FXML private TableColumn<Prestamo, String> columnHoraFinPrestamoAprobado;
    @FXML private TableColumn<Prestamo, String> columnEstadoPrestamoAprobado;
    @FXML private TableColumn<Prestamo, String> columnDetallePrestamoAprobado;

    @FXML private TableView<Prestamo> tablaPrestamosFinalizados;
    @FXML private TableColumn<Prestamo, Long> columnIdPrestamoFinalizado;
    @FXML private TableColumn<Prestamo, Long> columnCedulaPrestamoFinalizado;
    @FXML private TableColumn<Prestamo, Long> columnIdSalaPrestamoFinalizado;
    @FXML private TableColumn<Prestamo, Long> columnIdAudiovisualPrestamoFinalizado;
    @FXML private TableColumn<Prestamo, String> columnHoraInicioPrestamoFinalizado;
    @FXML private TableColumn<Prestamo, String> columnHoraFinPrestamoFinalizado;
    @FXML private TableColumn<Prestamo, String> columnEstadoPrestamoFinalizado;
    @FXML private TableColumn<Prestamo, String> columnDetallePrestamoFinalizado;

    @FXML private Button btnInformes;
    @FXML private Button btnMainMenu;

    private InformeDAO informeDAO;

    @FXML
    public void initialize() {
        try {
            informeDAO = InformeDAO.getInstance();

            // Configurar columnas de "Usuarios sin sanción"
            columnCedulaUsuario.setCellValueFactory(new PropertyValueFactory<>("cedulaUsuario"));
            columnNombreUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
            columnRolUsuario.setCellValueFactory(new PropertyValueFactory<>("rol"));
            columnEstadoUsuario.setCellValueFactory(new PropertyValueFactory<>("estado"));
            columnCorreoUsuario.setCellValueFactory(new PropertyValueFactory<>("correo"));

            // Configurar columnas de "Usuarios sancionados"
            columnIdSancion.setCellValueFactory(new PropertyValueFactory<>("idSancion"));
            columnCedulaUsuarioSancion.setCellValueFactory(new PropertyValueFactory<>("cedulaUsuario"));
            columnNombreUsuarioSancion.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
            columnEstadoUsuarioSancion.setCellValueFactory(new PropertyValueFactory<>("estadoSancion")); // Cambiado de estadoUsuario a estadoSancion
            columnMontoSancion.setCellValueFactory(new PropertyValueFactory<>("montoSancion"));
            columnMotivoSancion.setCellValueFactory(new PropertyValueFactory<>("motivoSancion"));

            // Configurar columnas de "Préstamos aprobados"
            columnIdPrestamoAprobado.setCellValueFactory(new PropertyValueFactory<>("id_prestamo"));
            columnCedulaPrestamoAprobado.setCellValueFactory(new PropertyValueFactory<>("cedula_usuario"));
            columnIdSalaPrestamoAprobado.setCellValueFactory(new PropertyValueFactory<>("id_sala"));
            columnIdAudiovisualPrestamoAprobado.setCellValueFactory(new PropertyValueFactory<>("id_audiovisual"));
            columnHoraInicioPrestamoAprobado.setCellValueFactory(new PropertyValueFactory<>("hora_inicio"));
            columnHoraFinPrestamoAprobado.setCellValueFactory(new PropertyValueFactory<>("hora_fin"));
            columnEstadoPrestamoAprobado.setCellValueFactory(new PropertyValueFactory<>("estado"));
            columnDetallePrestamoAprobado.setCellValueFactory(new PropertyValueFactory<>("detalle_prestamo"));

            // Configurar columnas de "Préstamos finalizados"
            columnIdPrestamoFinalizado.setCellValueFactory(new PropertyValueFactory<>("id_prestamo"));
            columnCedulaPrestamoFinalizado.setCellValueFactory(new PropertyValueFactory<>("cedula_usuario"));
            columnIdSalaPrestamoFinalizado.setCellValueFactory(new PropertyValueFactory<>("id_sala"));
            columnIdAudiovisualPrestamoFinalizado.setCellValueFactory(new PropertyValueFactory<>("id_audiovisual"));
            columnHoraInicioPrestamoFinalizado.setCellValueFactory(new PropertyValueFactory<>("hora_inicio"));
            columnHoraFinPrestamoFinalizado.setCellValueFactory(new PropertyValueFactory<>("hora_fin"));
            columnEstadoPrestamoFinalizado.setCellValueFactory(new PropertyValueFactory<>("estado"));
            columnDetallePrestamoFinalizado.setCellValueFactory(new PropertyValueFactory<>("detalle_prestamo"));

            // Cargar datos
            cargarDatos();
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    private void cargarDatos() throws SQLException {
        List<Usuario> usuariosSinSancion = informeDAO.obtenerUsuariosSinSancion();
        tablaUsuariosSinSancion.setItems(FXCollections.observableArrayList(usuariosSinSancion));

        List<UsuarioConSancion> usuariosConSancion = informeDAO.obtenerUsuariosConSancion();
        tablaUsuariosConSancion.setItems(FXCollections.observableArrayList(usuariosConSancion));

        List<Prestamo> prestamosAprobados = informeDAO.obtenerPrestamosAprobados();
        tablaPrestamosAprobados.setItems(FXCollections.observableArrayList(prestamosAprobados));

        List<Prestamo> prestamosFinalizados = informeDAO.obtenerPrestamosFinalizados();
        tablaPrestamosFinalizados.setItems(FXCollections.observableArrayList(prestamosFinalizados));
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
            e.printStackTrace();
        }
    }
}