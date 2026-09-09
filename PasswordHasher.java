/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.library.utils;

public class PasswordHasher {

public static String hashPassword(String password) {
    String textoEncriptado = "";
    
    for (int i = 0; i < password.length(); i++) {
        char letra = password.charAt(i);        
        char letraEncriptada = (char) (letra + 3);       
        textoEncriptado += letraEncriptada;
    }
    
    return textoEncriptado;
}//Aqui cierra hashPassword
                
public  static boolean verifyPassword(String passwordInput, String storedHash){
     String hashedInput = hashPassword(passwordInput);
     return hashedInput.equals(storedHash);
    }//Aqui cierra verifyPassword

}

