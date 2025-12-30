package util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Utilitário para realizar backup e restauração do banco de dados MySQL
 */
public class BackupUtil {
    
    private static final String BACKUP_DIR = "backups/";
    private static final String DB_NAME = "sistema_inventario";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456789"; // Alterar conforme sua configuração
    private static final String MYSQL_PATH = "C:/Program Files/MySQL/MySQL Server 8.0/bin/"; // Ajustar conforme instalação
    
    static {
        // Criar diretório de backups se não existir
        File dir = new File(BACKUP_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    /**
     * Cria um backup do banco de dados
     * @return caminho do arquivo de backup criado
     * @throws Exception em caso de erro
     */
    public static String createBackup() throws Exception {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = BACKUP_DIR + "backup_" + timestamp + ".sql";
        
        // Construir comando mysqldump usando ProcessBuilder
        List<String> command = new ArrayList<>();
        command.add(MYSQL_PATH + "mysqldump");
        command.add("-u" + DB_USER);
        
        if (!DB_PASSWORD.isEmpty()) {
            command.add("-p" + DB_PASSWORD);
        }
        
        command.add(DB_NAME);
        command.add("-r");
        command.add(filename);
        
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        
        if (exitCode != 0) {
            // Ler erro
            BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );
            StringBuilder errorMsg = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errorMsg.append(line).append("\n");
            }
            errorReader.close();
            throw new Exception("Erro ao criar backup: " + errorMsg.toString());
        }
        
        return filename;
    }
    
    /**
     * Restaura um backup do banco de dados
     * @param backupFile arquivo de backup
     * @throws Exception em caso de erro
     */
    public static void restoreBackup(File backupFile) throws Exception {
        if (!backupFile.exists()) {
            throw new FileNotFoundException("Arquivo de backup não encontrado: " + backupFile.getAbsolutePath());
        }
        
        // Construir comando mysql usando ProcessBuilder
        List<String> command = new ArrayList<>();
        command.add(MYSQL_PATH + "mysql");
        command.add("-u" + DB_USER);
        
        if (!DB_PASSWORD.isEmpty()) {
            command.add("-p" + DB_PASSWORD);
        }
        
        command.add(DB_NAME);
        
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        
        Process process = processBuilder.start();
        
        // Enviar o conteúdo do arquivo SQL para o processo
        OutputStream outputStream = process.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(backupFile);
        
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        
        fileInputStream.close();
        outputStream.close();
        
        int exitCode = process.waitFor();
        
        if (exitCode != 0) {
            BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );
            StringBuilder errorMsg = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errorMsg.append(line).append("\n");
            }
            errorReader.close();
            throw new Exception("Erro ao restaurar backup: " + errorMsg.toString());
        }
    }
    
    /**
     * Lista todos os arquivos de backup disponíveis
     * @return lista de arquivos de backup
     */
    public static List<File> listBackups() {
        List<File> backups = new ArrayList<>();
        File dir = new File(BACKUP_DIR);
        
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".sql"));
            if (files != null) {
                for (File file : files) {
                    backups.add(file);
                }
            }
        }
        
        return backups;
    }
    
    /**
     * Exclui um arquivo de backup
     * @param backupFile arquivo a ser excluído
     * @return true se excluído com sucesso
     */
    public static boolean deleteBackup(File backupFile) {
        return backupFile.exists() && backupFile.delete();
    }
}