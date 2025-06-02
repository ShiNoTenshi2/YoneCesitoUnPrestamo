package controller;

import data.DBConnectionFactory;
import data.SancionDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Sancion;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SancionController {
    @FXML private TextField txtIdSancion;
    @FXML private ComboBox<Long> comboBoxCedulaUsuarioSancion;
    @FXML private ComboBox<String> comboBoxEstadoSancion;
    @FXML private ComboBox<Integer> comboBoxIdDevolucionSancion;
    @FXML private TextField txtMotivoSancion;
    @FXML private TextField txtMontoSancion;
    @FXML private Button btnRegistrarSancion;
    @FXML private Button btnLeerSancion;
    @FXML private Button btnActualizarSancion;
    @FXML private Button btnBorrarSancion;
    @FXML private Button btnMenu;

    private SancionDAO sancionDAO;

    @FXML
    public void initialize() {
        try {
            // Obtener la conexión
            Connection connection = DBConnectionFactory.getConnectionByRole("Coordinador").getConnection();
            if (connection == null || connection.isClosed()) {
                throw new SQLException("La conexión a la base de datos no está disponible.");
            }

            // Inicializar DAO
            sancionDAO = new SancionDAO(connection);

            // Configurar ComboBox de cédula de usuario (bloqueada)
            comboBoxCedulaUsuarioSancion.setDisable(true);

            // Llenar ComboBox de estado (solo para actualizar, vacío al registrar)
            comboBoxEstadoSancion.getItems().addAll("Pendiente", "Pagado");
            comboBoxEstadoSancion.setDisable(true); // Deshabilitar al registrar

            // Llenar ComboBox de IDs de devolución (solo los asociados a préstamos)
            try {
                comboBoxIdDevolucionSancion.setItems(sancionDAO.obtenerIdsDevoluciones());
            } catch (SQLException e) {
                mostrarAlerta("Error", "No se pudieron cargar los IDs de devoluciones: " + e.getMessage());
            }

            // Añadir listener para actualizar la cédula del usuario cuando cambie el id_devolucion
            comboBoxIdDevolucionSancion.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue != null) {
                    try {
                        Long cedulaUsuario = sancionDAO.obtenerCedulaUsuarioPorDevolucion(newValue);
                        if (cedulaUsuario != null) {
                            comboBoxCedulaUsuarioSancion.getItems().setAll(cedulaUsuario);
                            comboBoxCedulaUsuarioSancion.getSelectionModel().select(cedulaUsuario);
                        } else {
                            comboBoxCedulaUsuarioSancion.getItems().clear();
                            mostrarAlerta("Error", "No se encontró una cédula de usuario asociada a esta devolución.");
                        }
                    } catch (SQLException e) {
                        mostrarAlerta("Error", "Error al obtener la cédula del usuario: " + e.getMessage());
                        comboBoxCedulaUsuarioSancion.getItems().clear();
                    }
                } else {
                    comboBoxCedulaUsuarioSancion.getItems().clear();
                }
            });
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo conectar a la base de datos: " + e.getMessage());
            try {
                GoToMenu();
            } catch (Exception ex) {
                mostrarAlerta("Error Crítico", "No se pudo regresar al menú principal: " + ex.getMessage());
            }
        }
    }

    @FXML
    public void RegistrarSancion() {
        if (sancionDAO == null) {
            mostrarAlerta("Error", "No se puede registrar: la conexión a la base de datos no está disponible.");
            return;
        }

        try {
            // Validar campos
            if (!validarCampos()) return;

            // Crear nueva sanción (estado se manejará por el trigger)
            Sancion sancion = new Sancion(
                0, // ID se genera con la secuencia
                txtMotivoSancion.getText(),
                Double.parseDouble(txtMontoSancion.getText()),
                null, // Estado se pondrá a "Pendiente" por el trigger
                comboBoxIdDevolucionSancion.getValue(),
                comboBoxCedulaUsuarioSancion.getValue()
            );

            // Guardar en la base de datos
            sancionDAO.guardar(sancion);
            mostrarAlerta("Éxito", "Sanción registrada correctamente.");
            limpiarCampos();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El monto debe ser un número válido.");
        } catch (SQLException e) {
            mostrarAlerta("Error", e.getMessage());
        }
    }

    @FXML
    public void LeerSancion() {
        if (sancionDAO == null) {
            mostrarAlerta("Error", "No se puede leer: la conexión a la base de datos no está disponible.");
            return;
        }

        try {
            var sanciones = sancionDAO.obtenerTodas();

            if (sanciones.isEmpty()) {
                mostrarAlerta("Información", "No hay sanciones registradas.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Lista de Sanciones:\n\n");
            for (Sancion sancion : sanciones) {
                sb.append(sancion.toString()).append("\n");
            }

            mostrarAlerta("Sanciones Registradas", sb.toString());
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al leer las sanciones: " + e.getMessage());
        }
    }

    @FXML
    public void ActualizarSancion() {
        if (sancionDAO == null) {
            mostrarAlerta("Error", "No se puede actualizar: la conexión a la base de datos no está disponible.");
            return;
        }

        try {
            // Validar ID
            String idText = txtIdSancion.getText();
            if (idText.isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese el ID de la sanción.");
                return;
            }
            int idSancion = Integer.parseInt(idText);

            // Validar otros campos
            if (!validarCampos()) return;

            // Verificar si el ID existe
            if (!sancionDAO.existeId(idSancion)) {
                mostrarAlerta("Error", "El ID de sanción no existe.");
                return;
            }

            // Habilitar comboBoxEstado para actualizar
            comboBoxEstadoSancion.setDisable(false);

            // Validar que se haya seleccionado un estado
            if (comboBoxEstadoSancion.getValue() == null) {
                mostrarAlerta("Error", "Por favor, seleccione un estado para la sanción. Ahora puedes cambiarlo a 'Pendiente' o 'Pagado'.");
                return;
            }

            // Crear sanción actualizada
            Sancion sancion = new Sancion(
                idSancion,
                txtMotivoSancion.getText(),
                Double.parseDouble(txtMontoSancion.getText()),
                comboBoxEstadoSancion.getValue(), // Ahora está validado que no sea null
                comboBoxIdDevolucionSancion.getValue(),
                comboBoxCedulaUsuarioSancion.getValue()
            );

            // Actualizar en la base de datos
            sancionDAO.actualizar(sancion);
            mostrarAlerta("Éxito", "Sanción actualizada correctamente.");
            limpiarCampos();
            comboBoxEstadoSancion.setDisable(true); // Deshabilitar de nuevo
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El monto o ID deben ser numéricos.");
        } catch (SQLException e) {
            // Personalizar mensaje técnico
            String mensajeError = e.getMessage();
            String mensajeAmigable = mensajeError.contains("ORA-01407") 
                ? "Por favor, seleccione un estado para la sanción. Ahora puedes cambiarlo a 'Pendiente' o 'Pagado'."
                : "Error al actualizar la sanción: " + mensajeError;
            mostrarAlerta("Error", mensajeAmigable);
        }
    }

    @FXML
    public void BorrarSancion() {
        if (sancionDAO == null) {
            mostrarAlerta("Error", "No se puede borrar: la conexión a la base de datos no está disponible.");
            return;
        }

        try {
            // Validar ID
            String idText = txtIdSancion.getText();
            if (idText.isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese el ID de la sanción.");
                return;
            }
            int idSancion = Integer.parseInt(idText);

            // Verificar si el ID existe
            if (!sancionDAO.existeId(idSancion)) {
                mostrarAlerta("Error", "El ID de sanción no existe.");
                return;
            }

            // Eliminar de la base de datos
            sancionDAO.eliminar(idSancion);
            mostrarAlerta("Éxito", "Sanción borrada correctamente.");
            limpiarCampos();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID de sanción debe ser numérico.");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al eliminar la sanción: " + e.getMessage());
        }
    }

    @FXML
    private void GoToMenu() {
        try {
            loadView("/view/MainMenu.fxml", "Menú Coordinador");
        } catch (IOException e) {
            mostrarAlerta("Error", "Error al cargar el menú: " + e.getMessage());
        }
    }

    private void loadView(String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = (Stage) btnMenu.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    private boolean validarCampos() {
        // Validar campos obligatorios
        if (txtMotivoSancion.getText().isEmpty() || txtMontoSancion.getText().isEmpty() ||
            comboBoxIdDevolucionSancion.getValue() == null || comboBoxCedulaUsuarioSancion.getValue() == null) {
            mostrarAlerta("Error", "Por favor, complete todos los campos obligatorios.");
            return false;
        }

        // Validar que el monto sea un número positivo
        try {
            double monto = Double.parseDouble(txtMontoSancion.getText());
            if (monto <= 0) {
                mostrarAlerta("Error", "El monto debe ser mayor a 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El monto debe ser un número válido.");
            return false;
        }

        return true;
    }

    private void limpiarCampos() {
        txtIdSancion.clear();
        comboBoxCedulaUsuarioSancion.getSelectionModel().clearSelection();
        comboBoxCedulaUsuarioSancion.getItems().clear();
        comboBoxEstadoSancion.getSelectionModel().clearSelection();
        comboBoxIdDevolucionSancion.getSelectionModel().clearSelection();
        txtMotivoSancion.clear();
        txtMontoSancion.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}