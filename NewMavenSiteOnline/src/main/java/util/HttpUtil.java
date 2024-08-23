package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Classe utilitária para operações relacionadas a HTTP.
 * <p>
 * Esta classe fornece métodos para realizar requisições HTTP e obter respostas.
 * </p>
 */
public class HttpUtil {

    /**
     * Obtém a resposta de uma requisição HTTP GET para a URL especificada.
     * <p>
     * Esse método faz uma requisição HTTP GET para a URL fornecida e retorna o conteúdo da resposta como uma string.
     * Se a resposta não for bem-sucedida (código diferente de 200), uma exceção será lançada.
     * </p>
     *
     * @param urlString A URL para a qual a requisição HTTP GET será enviada.
     * @return O conteúdo da resposta HTTP como uma string.
     * @throws IOException Se ocorrer um erro ao tentar estabelecer a conexão ou ler a resposta.
     */
    public static String getHttpResponse(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return response.toString();
            }
        } else {
            throw new IOException("Erro ao obter resposta HTTP. Código de resposta: " + responseCode);
        }
    }
}
