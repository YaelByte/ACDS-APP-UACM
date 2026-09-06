package chinsons.app.uacm;

/**
 *
 * @author aleja
 */


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ControlAccesoController {

    @FXML
    private TextField FieldCorreo;

    @FXML
    private PasswordField FieldClave;

    @FXML
    private Label etiquetaMensaje;

    private validadorCredenciales motorValidador = new validadorCredenciales();

    @FXML
    private void botonIngresarPresionado() {
        // Obtenemos los textos que ingresó el usuario
        String correo = FieldCorreo.getText();
        String clave = FieldClave.getText();
        
        etiquetaMensaje.setStyle("");

        // 1. Ejecutamos tu validación de formato
        String respuestaFiltro = motorValidador.validarFormatosPrevios(correo, clave);

        if (respuestaFiltro.equals("OK")) {
            // 2. Si el formato cumple, procedemos a leer el archivo de texto
            String respuestaArchivo = motorValidador.verificarEnArchivo(correo, clave);
            etiquetaMensaje.setText(respuestaArchivo);

            if (respuestaArchivo.contains("Bloqueada") || respuestaArchivo.contains("incorrectas") || respuestaArchivo.contains("Error")) {
                // Color naranja institucional para alertas
                etiquetaMensaje.setStyle("-fx-text-fill: #FF8C00; -fx-font-weight: bold;"); 
            } else {
                // Color verde para el mensaje de bienvenida exitoso
                etiquetaMensaje.setStyle("-fx-text-fill: #2ECC71; -fx-font-weight: bold;"); 
            }

        } else {
            // Si falló el filtro de Regex, mostramos tu error exacto sin consultar el archivo
            etiquetaMensaje.setText(respuestaFiltro);
            etiquetaMensaje.setStyle("-fx-text-fill: #FF8C00; -fx-font-weight: bold;"); 
        }
    }
}