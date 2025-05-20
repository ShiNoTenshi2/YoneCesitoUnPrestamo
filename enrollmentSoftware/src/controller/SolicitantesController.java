package controller;

import javafx.event.ActionEvent;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Solicitantes;
import application.Main;
import data.DBConnection;
import data.SolicitantesDAO;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class SolicitantesController {

    @FXML private TextField txtIdSolicitante;
    @FXML private TextField txtNombreSolicitante;
    @FXML private TextField txtCorreoSolicitante;
    @FXML private TextField txtTelefonoSolicitante;

    @FXML private Button btnRegistrarSolicitante;
    @FXML private Button btnLeerSolicitante;
    @FXML private Button btnActualizarSolicitante;
    @FXML private Button btnBorrarSolicitante;
    @FXML private Button btnMenu;

    private SolicitantesDAO solicitanteDAO = new SolicitantesDAO(DBConnection.getInstance().getConnection());

    @FXML
    void RegistrarSolicitante(ActionEvent event) {
        try {
            // Validar campos vacíos
            if (txtNombreSolicitante.getText().isEmpty() ||
                txtCorreoSolicitante.getText().isEmpty() ||
                txtTelefonoSolicitante.getText().isEmpty()) {
                
                mostrarAlerta("Error", "Campos vacíos", "Nombre, Correo y Teléfono son obligatorios.");
                return;
            }

            // Validar teléfono (10 dígitos)
            String telefono = txtTelefonoSolicitante.getText().trim();
            if (!telefono.matches("\\d{10}")) {
                mostrarAlerta("Error", "Teléfono inválido", "Debe tener exactamente 10 dígitos.");
                return;
            }

            // Validar correo
            String correo = txtCorreoSolicitante.getText().trim();
            if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                mostrarAlerta("Error", "Correo inválido", "Formato: usuario@dominio.com");
                return;
            }

            // Verificar correo único
            if (solicitanteDAO.existeCorreo(correo)) {
                mostrarAlerta("Error", "Correo duplicado", "Este correo ya está registrado.");
                return;
            }

            // Crear y guardar
            Solicitantes solicitante = new Solicitantes(
                0, // ID no se usa, la secuencia lo genera
                txtNombreSolicitante.getText().trim(),
                correo,
                telefono
            );
            
            solicitanteDAO.guardar(solicitante);
            mostrarAlerta("Éxito", "Registro exitoso", "Solicitante registrado correctamente.");
            limpiarCampos();

        } catch (SQLException e) {
            mostrarAlerta("Error en BD", "Error al guardar", e.getMessage());
        }
    }

    @FXML
    void LeerSolicitante(ActionEvent event) {
        try {
            // Mostrar todos los solicitantes en consola
            System.out.println("=== LISTADO COMPLETO DE SOLICITANTES ===");
            for (Solicitantes s : solicitanteDAO.obtenerTodos()) {
                System.out.println(s.toString());
            }
            System.out.println("=======================================");
            mostrarAlerta("Información", "Datos en consola", "Se han mostrado todos los solicitantes en la consola");
            
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error de base de datos", e.getMessage());
        }
    }

    @FXML
    void ActualizarSolicitante(ActionEvent event) {
        try {
            if (txtIdSolicitante.getText().isEmpty() || 
                txtNombreSolicitante.getText().isEmpty() ||
                txtCorreoSolicitante.getText().isEmpty() ||
                txtTelefonoSolicitante.getText().isEmpty()) {
                
                mostrarAlerta("Error", "Campos vacíos", "Todos los campos son obligatorios para actualizar.");
                return;
            }
            
            int id;
            try {
                id = Integer.parseInt(txtIdSolicitante.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "ID inválido", "El ID debe ser un número.");
                return;
            }
            
            // Validar teléfono
            String telefono = txtTelefonoSolicitante.getText().trim();
            if (!telefono.matches("\\d{10}")) {
                mostrarAlerta("Error", "Teléfono inválido", "Debe tener exactamente 10 dígitos.");
                return;
            }

            // Validar correo
            String correo = txtCorreoSolicitante.getText().trim();
            if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                mostrarAlerta("Error", "Correo inválido", "Formato: usuario@dominio.com");
                return;
            }

            // Verificar que el ID existe
            if (!solicitanteDAO.existeId(id)) {
                mostrarAlerta("Error", "ID no encontrado", "No existe un solicitante con ese ID.");
                return;
            }

            // Verificar correo único (excepto para el propio registro)
            Solicitantes existing = solicitanteDAO.buscarPorId(id);
            if (!correo.equals(existing.getCorreo()) && solicitanteDAO.existeCorreo(correo)) {
                mostrarAlerta("Error", "Correo duplicado", "Este correo ya está registrado.");
                return;
            }

            // Crear objeto con datos actualizados
            Solicitantes solicitante = new Solicitantes(
                id,
                txtNombreSolicitante.getText().trim(),
                correo,
                telefono
            );
            
            // Actualizar en BD
            solicitanteDAO.actualizar(solicitante);
            mostrarAlerta("Éxito", "Actualización exitosa", "Solicitante actualizado correctamente");
            limpiarCampos();
            
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error de base de datos", e.getMessage());
        }
    }

    @FXML
    void BorrarSolicitante(ActionEvent event) {
        try {
            if (txtIdSolicitante.getText().isEmpty()) {
                mostrarAlerta("Error", "Campo vacío", "Ingrese un ID para borrar");
                return;
            }
            
            int id;
            try {
                id = Integer.parseInt(txtIdSolicitante.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "ID inválido", "El ID debe ser un número");
                return;
            }
            
            // Verificar que el ID existe
            if (!solicitanteDAO.existeId(id)) {
                mostrarAlerta("Error", "ID no encontrado", "No existe un solicitante con ese ID");
                return;
            }
            
            // Confirmar borrado
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar borrado");
            confirmacion.setHeaderText("¿Está seguro de borrar este solicitante?");
            confirmacion.setContentText("Esta acción no se puede deshacer");
            
            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                solicitanteDAO.eliminar(id);
                mostrarAlerta("Éxito", "Borrado exitoso", "Solicitante borrado correctamente");
                limpiarCampos();
            }
            
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error de base de datos", e.getMessage());
        }
    }

    @FXML
    void GoToMenu(ActionEvent event) {
        Main.loadScene("/view/MainMenu.fxml");
    }

    private void mostrarAlerta(String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private void limpiarCampos() {
        txtIdSolicitante.clear();
        txtNombreSolicitante.clear();
        txtCorreoSolicitante.clear();
        txtTelefonoSolicitante.clear();
    }
}