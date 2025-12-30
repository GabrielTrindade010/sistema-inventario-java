package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilitário para criptografia de senhas usando SHA-256
 * 
 * Esta classe fornece métodos para criptografar senhas de forma segura
 * e verificar se uma senha corresponde a um hash armazenado.
 */
public class PasswordUtil {
    
    /**
     * Criptografa uma senha usando o algoritmo SHA-256
     * 
     * @param password senha em texto plano
     * @return senha criptografada em formato hexadecimal
     * @throws RuntimeException se o algoritmo SHA-256 não estiver disponível
     */
    public static String encrypt(String password) {
        try {
            // Cria uma instância do MessageDigest com o algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Converte a senha em bytes e gera o hash
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            
            // Converte o array de bytes para uma string hexadecimal
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
            throw new RuntimeException("Erro ao criptografar senha: algoritmo SHA-256 não encontrado", e);
        }
    }
    
    /**
     * Verifica se a senha informada corresponde ao hash armazenado
     * 
     * @param password senha em texto plano para verificar
     * @param hash hash SHA-256 armazenado no banco de dados
     * @return true se a senha corresponde ao hash, false caso contrário
     */
    public static boolean verify(String password, String hash) {
        if (password == null || hash == null) {
            return false;
        }
        
        String passwordHash = encrypt(password);
        return passwordHash.equals(hash);
    }
    

}