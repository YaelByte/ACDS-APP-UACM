package chinsons.app.uacm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

//JIJIJI 03/09/26


/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
    scene = new Scene(loadFXML("Control_Acceso"), 640, 480); 
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

public static void main(String[] args) {
        // --- PRUEBAS TEMPORALES DEL MOTOR ---
        validadorCredenciales motor = new validadorCredenciales();
        
        System.out.println("Prueba 1 (Fallo Regex): " + motor.validarFormatosPrevios("trevor@uacm.edu.mx", "123"));
        System.out.println("Prueba 2 (Fallo Correo): " + motor.validarFormatosPrevios("trevor@gmail.com", "Secreto456#"));
        System.out.println("Prueba 3 (Formato OK): " + motor.validarFormatosPrevios("trevor@uacm.edu.mx", "Secreto456#"));
        
        launch();
    }

}