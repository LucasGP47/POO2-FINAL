package service;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Serviço de log responsável por gravar logs em um arquivo.
 * <p>
 * Esta classe permite registrar mensagens de log em um arquivo de texto especificado,
 * incluindo logs de mensagens enviadas via Twilio e erros relacionados.
 * </p>
 */
public class LogService {
    private final String logFilePath;

    /**
     * Inicializa uma instância do serviço de log.
     * 
     * @param logFilePath Caminho para o arquivo de log.
     */
    public LogService(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    /**
     * Registra uma mensagem enviada via Twilio.
     * 
     * @param message A mensagem a ser registrada.
     */
    public void logTwilioMessage(String message) {
        writeToFile("Twilio Log: " + message + System.lineSeparator());
    }

    /**
     * Registra um erro relacionado ao Twilio.
     * 
     * @param error O erro a ser registrado.
     */
    public void logTwilioError(String error) {
        writeToFile("Erro Twilio: " + error + System.lineSeparator());
    }

    /**
     * Escreve o conteúdo especificado no arquivo de log.
     * <p>
     * Este método é responsável por abrir o arquivo de log no caminho especificado
     * e gravar o conteúdo nele. Se o arquivo já existir, ele será sobrescrito.
     * </p>
     * 
     * @param content O conteúdo a ser escrito no arquivo de log.
     */
    private void writeToFile(String content) {
        try (FileWriter writer = new FileWriter(logFilePath, false)) { 
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Formata a data e hora atual em uma string.
     * 
     * @return A data e hora atual formatada como "yyyy-MM-dd HH:mm:ss".
     */
    public String formatCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
