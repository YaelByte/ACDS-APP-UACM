package chinsons.app.uacm;

/**
 *
 * @author aleja
 */


import java.io.IOException;
import static java.lang.System.in;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControlAccesoController {

    @FXML
    private TextField FieldCorreo;

    @FXML
    private PasswordField FieldClave;

    @FXML
    private Label etiquetaMensaje;

    private validadorCredenciales motorValidador = new validadorCredenciales();

    @FXML
    private void botonIngresarPresionado() throws IOException {
        // Obtenemos los textos que ingresó el usuario
        String correo = FieldCorreo.getText();
        String clave = FieldClave.getText();
        
        etiquetaMensaje.setStyle("");

        // 1. Ejecutamos tu validación de formato
        String respuestaFiltro = motorValidador.validarFormatosPrevios(correo, clave);

        if (respuestaFiltro.equals("OK")) {
            // 2. Si el formato cumple, procedemos a leer el archivo de texto
            try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chinsons/app/uacm/Dashboard.fxml"));
            Parent root = loader.load();
            
            javafx.stage.Stage stage = (javafx.stage.Stage) etiquetaMensaje.getScene().getWindow();
            
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            
            }catch(java.io.IOException e){
            
                System.out.println("Error al cargar");
                e.fillInStackTrace();
            
            }
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