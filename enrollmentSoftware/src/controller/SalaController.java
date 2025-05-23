package controller;

import javafx.event.ActionEvent;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Sala;
import application.Main;
import data.DBConnection;
import data.SalaDAO;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class SalaController {

    @FXML private TextField txtIdSala;
    @FXML private TextField txtCapacidad;
    @FXML private TextField txtDetallesSala;
    @FXML private TextField txtEstadoSala;

    @FXML private Button btnRegistrarSala;
    @FXML private Button btnLeerSala;
    @FXML private Button btnActualizarSala;
    @FXML private Button btnBorrarSala;
    @FXML private Button btnMenu;

    private SalaDAO salaDAO = new SalaDAO(DBConnection.getInstance().getConnection());

    @FXML
    public void RegistrarSala(ActionEvent event) {
        try {
            // Validar campos vacíos
            if (txtIdSala.getText().isEmpty() || 
                txtCapacidad.getText().isEmpty() ||
                txtDetallesSala.getText().isEmpty() ||
                txtEstadoSala.getText().isEmpty()) {
                
                mostrarAlerta("Error", "Campos vacíos", "Todos los campos son obligatorios.");
                return;
            }

            // Validar ID y Capacidad
            int id, capacidad;
            try {
                id = Integer.parseInt(txtIdSala.getText());
                capacidad = Integer.parseInt(txtCapacidad.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "Valor inválido", "ID y Capacidad deben ser números.");
                return;
            }

            // Verificar ID único
            if (salaDAO.existeId(id)) {
                mostrarAlerta("Error", "ID duplicado", "Este ID de sala ya está registrado.");
                return;
            }

            Sala sala = new Sala(
                id,
                capacidad,
                txtDetallesSala.getText().trim(),
                txtEstadoSala.getText().trim()
            );
            
            salaDAO.guardar(sala);
            mostrarAlerta("Éxito", "Registro exitoso", "Sala registrada correctamente.");
            limpiarCampos();

        } catch (SQLException e) {
            mostrarAlerta("Error en BD", "Error al guardar", e.getMessage());
        }
    }

    @FXML
    public void LeerSala(ActionEvent event) {
        try {
            System.out.println("=== LISTADO DE SALAS ===");
            for (Sala s : salaDAO.obtenerTodos()) {
                System.out.println(s.toString());
            }
            System.out.println("=======================");
            mostrarAlerta("Información", "Datos en consola", "Se mostraron todas las salas en la consola");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error de BD", e.getMessage());
        }
    }

    @FXML
    public void BorrarSala(ActionEvent event) {
        try {
            if (txtIdSala.getText().isEmpty()) {
                mostrarAlerta("Error", "Campo vacío", "Ingrese un ID para actualizar");
                return;
            }
            
            int id = Integer.parseInt(txtIdSala.getText());
            int capacidad = Integer.parseInt(txtCapacidad.getText());
            
            Sala sala = new Sala(
                id,
                capacidad,
                txtDetallesSala.getText().trim(),
                txtEstadoSala.getText().trim()
            );
            
            salaDAO.actualizar(sala);
            mostrarAlerta("Éxito", "Actualización exitosa", "Sala actualizada correctamente");
            
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Datos inválidos", "ID y Capacidad deben ser números");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error de BD", e.getMessage());
        }
    }

    @FXML
    public void ActualizarSala(ActionEvent event) {
        try {
            if (txtIdSala.getText().isEmpty()) {
                mostrarAlerta("Error", "Campo vacío", "Ingrese un ID para borrar");
                return;
            }
            
            int id = Integer.parseInt(txtIdSala.getText());
            
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar borrado");
            confirmacion.setHeaderText("¿Está seguro de borrar esta sala?");
            confirmacion.setContentText("Esta acción no se puede deshacer");
            
            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                salaDAO.eliminar(id);
                mostrarAlerta("Éxito", "Borrado exitoso", "Sala eliminada correctamente");
                limpiarCampos();
            }
            
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "ID inválido", "El ID debe ser un número");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error de BD", e.getMessage());
        }
    }

    @FXML
    public void GoToMenu(ActionEvent event) {
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
        txtIdSala.clear();
        txtCapacidad.clear();
        txtDetallesSala.clear();
        txtEstadoSala.clear();
    }
}