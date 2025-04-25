package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.sql.SQLException;
import model.Sancion;
import application.Main;
import data.DBConnection;
import data.SancionDAO;
import data.SolicitantesDAO;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class SancionController {

    // Componentes de la interfaz
    @FXML private TextField txtIdSancion;
    @FXML private ComboBox<Integer> comboBoxdSolicitanteSancion;
    @FXML private TextField txtMotivoSancion;
    @FXML private TextField txtMontoSancion;
    @FXML private TextField txtEstadoSancion;
    
    @FXML private Button btnRegistrarSancion;
    @FXML private Button btnLeerSancion;
    @FXML private Button btnActualizarSancion;
    @FXML private Button btnBorrarSancion;
    @FXML private Button btnMenu;

    // DAOs para acceso a datos
    private SancionDAO sancionDAO;
    private SolicitantesDAO solicitanteDAO;

    @FXML
    public void initialize() {
        try {
            // Inicializar DAOs con la conexión a la base de datos
            sancionDAO = new SancionDAO(DBConnection.getInstance().getConnection());
            solicitanteDAO = new SolicitantesDAO(DBConnection.getInstance().getConnection());
            
            // Cargar los IDs de solicitantes en el ComboBox
            cargarSolicitantes();
        } catch (IOException e) {
            mostrarAlerta("Error", "Error de conexión", "No se pudo conectar a la base de datos: " + e.getMessage());
        }
    }

    private void cargarSolicitantes() {
        try {
            ObservableList<Integer> idsSolicitantes = solicitanteDAO.obtenerTodosIds();
            if (idsSolicitantes != null && !idsSolicitantes.isEmpty()) {
                comboBoxdSolicitanteSancion.setItems(idsSolicitantes);
            } else {
                System.out.println("No se han encontrado solicitantes.");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al cargar solicitantes", e.getMessage());
        }
    }

    @FXML
    public void RegistrarSancion(ActionEvent event) {
        try {
            // Validar campos vacíos
            if (validarCamposVacios()) return;
            
            // Obtener valores del formulario
            int id = Integer.parseInt(txtIdSancion.getText());
            int idSolicitante = comboBoxdSolicitanteSancion.getValue();
            int monto = Integer.parseInt(txtMontoSancion.getText());
            
            // Verificar si el ID ya existe
            if (sancionDAO.existeId(id)) {
                mostrarAlerta("Error", "ID duplicado", "Ya existe una sanción con este ID");
                return;
            }

            // Crear objeto Sancion
            Sancion sancion = new Sancion(
                id,
                idSolicitante,
                txtMotivoSancion.getText().trim(),
                monto,
                txtEstadoSancion.getText().trim()
            );
            
            // Guardar en la base de datos
            sancionDAO.guardar(sancion);
            mostrarAlerta("Éxito", "Registro exitoso", "Sanción registrada correctamente");
            
            // Limpiar campos después del registro
            limpiarCampos();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Datos inválidos", "ID y Monto deben ser números enteros");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error en base de datos", e.getMessage());
        }
    }

    @FXML
    public void LeerSancion(ActionEvent event) {
        try {
            System.out.println("\n=== LISTADO COMPLETO DE SANCIONES ===");
            for (Sancion s : sancionDAO.obtenerTodos()) {
                System.out.println(s.toString());
            }
            System.out.println("====================================\n");
            
            mostrarAlerta("Información", "Datos en consola", "Se han mostrado todas las sanciones en la consola");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al leer sanciones", e.getMessage());
        }
    }

    @FXML
    public void ActualizarSancion(ActionEvent event) {
        try {
            // Validar campos vacíos
            if (validarCamposVacios()) return;
            
            // Obtener valores del formulario
            int id = Integer.parseInt(txtIdSancion.getText());
            int idSolicitante = comboBoxdSolicitanteSancion.getValue();
            int monto = Integer.parseInt(txtMontoSancion.getText());
            
            // Verificar si el ID existe
            if (!sancionDAO.existeId(id)) {
                mostrarAlerta("Error", "ID no encontrado", "No existe una sanción con este ID");
                return;
            }

            // Crear objeto Sancion con los datos actualizados
            Sancion sancion = new Sancion(
                id,
                idSolicitante,
                txtMotivoSancion.getText().trim(),
                monto,
                txtEstadoSancion.getText().trim()
            );
            
            // Actualizar en la base de datos
            sancionDAO.actualizar(sancion);
            mostrarAlerta("Éxito", "Actualización exitosa", "Sanción actualizada correctamente");

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Datos inválidos", "ID y Monto deben ser números enteros");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error en base de datos", e.getMessage());
        }
    }

    @FXML
    public void BorrarSancion(ActionEvent event) {
        try {
            // Validar ID vacío
            if (txtIdSancion.getText().isEmpty()) {
                mostrarAlerta("Error", "Campo vacío", "Ingrese un ID para borrar");
                return;
            }
            
            int id = Integer.parseInt(txtIdSancion.getText());
            
            // Verificar si el ID existe
            if (!sancionDAO.existeId(id)) {
                mostrarAlerta("Error", "ID no encontrado", "No existe una sanción con este ID");
                return;
            }
            
            // Confirmación antes de borrar
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar borrado");
            confirmacion.setHeaderText("¿Está seguro de borrar esta sanción?");
            confirmacion.setContentText("Esta acción no se puede deshacer");
            
            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                sancionDAO.eliminar(id);
                mostrarAlerta("Éxito", "Borrado exitoso", "Sanción eliminada correctamente");
                limpiarCampos();
            }
            
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "ID inválido", "El ID debe ser un número entero");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error en base de datos", e.getMessage());
        }
    }

    @FXML
    public void GoToMenu(ActionEvent event) {
        Main.loadScene("/view/MainMenu.fxml");
    }

    private boolean validarCamposVacios() {
        if (txtIdSancion.getText().isEmpty() || 
            comboBoxdSolicitanteSancion.getValue() == null ||
            txtMotivoSancion.getText().isEmpty() ||
            txtMontoSancion.getText().isEmpty() ||
            txtEstadoSancion.getText().isEmpty()) {
            
            mostrarAlerta("Error", "Campos vacíos", "Todos los campos son obligatorios");
            return true;
        }
        return false;
    }

    private void mostrarAlerta(String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private void limpiarCampos() {
        txtIdSancion.clear();
        comboBoxdSolicitanteSancion.getSelectionModel().clearSelection();
        txtMotivoSancion.clear();
        txtMontoSancion.clear();
        txtEstadoSancion.clear();
    }
}
