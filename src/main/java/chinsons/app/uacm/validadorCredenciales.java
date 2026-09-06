/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chinsons.app.uacm;

/**
 *
 * @author Trevor
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class validadorCredenciales {

    // Variables de estado para el control de bloqueos
    private int intentosFallidos = 0;
    private boolean cuentaBloqueada = false;

    // Regex para correo válido institucional
    private static final String REGEX_CORREO = "^[A-Za-z0-9+_.-]+@uacm\\.edu\\.mx$";
    
    // Regex de alta seguridad: mínimo 8 caracteres, incluye mayúscula, minúscula, número y caracteres (#, $, &)
    private static final String REGEX_CLAVE = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$&])[a-zA-Z\\d#$&]{8,}$";

    /**
     * Método para encriptar la contraseña
     */
    public String encriptarClave(String claveReal) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(claveReal.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error fatal de seguridad en el sistema.");
        }
    }

    /**
     * Validación: Retorna "OK" si el formato es correcto, o un mensaje de error específico.
     */
    public String validarFormatosPrevios(String correo, String clave) {
        if (cuentaBloqueada) {
            return "Por seguridad: cuenta Bloqueada, levantar ticket en el Help desk de la compañía por olvido de cuenta o clave";
        }

        if (!correo.matches(REGEX_CORREO)) {
            return "Error: Ingrese una dirección de correo electrónico institucional válida (@uacm.edu.mx).";
        }

        if (!clave.matches(REGEX_CLAVE)) {
            return "Error: La contraseña debe tener mínimo 8 caracteres, incluir mayúscula, minúscula, un número y un símbolo permitido (#, $, &).";
        }

        // Si pasa los filtros, devolvemos OK para proceder a leer el archivo
        return "OK";
    }

    /**
     * Se llamará solo si el formato fue "OK" pero no se encontró el usuario en el archivo de texto.
     */
    public String registrarIntentoFallido() {
        intentosFallidos++;
        
        if (intentosFallidos >= 3) {
            cuentaBloqueada = true;
            // Mensaje exacto solicitado por el documento
            return "Por seguridad: cuenta Bloqueada, levantar ticket en el Help desk de la compañía por olvido de cuenta o clave";
        }
        
        return "Credenciales incorrectas. Le quedan " + (3 - intentosFallidos) + " intentos.";
    }
    
    /**
     * Verificación contra el archivo de texto local.
     */
    public String verificarEnArchivo(String correoIngresado, String claveIngresada) {
        // Ruta del archivo en la raíz del proyecto
        String rutaArchivo = "credenciales.txt"; 
        String claveIngresadaConHash = encriptarClave(claveIngresada);
        
        System.out.println("Copia este HASH en tu bloc de notas: " + claveIngresadaConHash);
        
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            
            // Leemos el archivo línea por línea
            while ((linea = br.readLine()) != null) {
                //el formato en el txt es: correo,contraseña y Nombre
                String[] datos = linea.split(",");
                
                if (datos.length >= 2) {
                    String correoArchivo = datos[0].trim();
                    String claveHashArchivo = datos[1].trim();
                    
                    // Si hay coincidencia exacta comparando los datos ingresados contra el archivo local
                    if (correoArchivo.equals(correoIngresado) && claveHashArchivo.equals(claveIngresadaConHash)) {
                        // Reseteamos el contador de bloqueos porque entró con éxito
                        intentosFallidos = 0; 
                        
                        // Extraemos el nombre (o usamos la primera parte del correo si no hay nombre)
                        String nombreUsuario = (datos.length >= 3) ? datos[2].trim() : correoIngresado.split("@")[0];
                        
                        // Mensaje de éxito exacto exigido por el documento
                        return "Bienvenido al sistema " + nombreUsuario;
                    }
                }
            }
        } catch (IOException e) {
            return "Error de sistema: No se pudo leer el archivo de almacenamiento local.";
        }
        
        // Si el ciclo termina y no hubo coincidencias, disparamos el intento fallido
        return registrarIntentoFallido();
    }
}