package service;

import model.Site;
import util.HttpUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe que verifica o estado de um site e se seu conteúdo mudou desde sua ultima verificação.
 */
public class SiteChecker {
    private final Map<String, String> previousHashes = new HashMap<>();

    /**
     * Verifica se o site está online.
     * <p>
     * Esse método faz uma requisição HTTP ao site e verifica se o código de resposta
     * é 200 (OK), indicando que o site está online.
     * </p>
     * 
     * @param site O site a ser verificado.
     * @return {@code true} se o site está online, {@code false} caso contrário.
     */
    public boolean isSiteOnline(Site site) {
        try {
            URL url = new URL(site.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            return responseCode == 200; 
        } catch (IOException e) {
            return false;
        }
    }


    /**
     * Verifica se o conteúdo de um site mudou desde a última verificação.
     * <p>
     * Esse método recupera o conteúdo atual do site, calcula seu hash e compara
     * com o hash do conteúdo anterior. Se houver uma diferença, significa que
     * o conteúdo mudou.
     * </p>
     * 
     * @param site O site a ser verificado.
     * @return {@code true} se o conteúdo do site mudou, {@code false} caso contrário.
     */
    public boolean hasSiteChanged(Site site) {
        try {
            String currentContent = HttpUtil.getHttpResponse(site.getUrl());
            String currentHash = hashContent(currentContent);
            String previousHash = previousHashes.get(site.getUrl());

            if (previousHash != null && !previousHash.equals(currentHash)) {
            	previousHashes.put(site.getUrl(), currentHash);
                return true;
            }

            previousHashes.put(site.getUrl(), currentHash);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
  
    /**
     * Calcula o hash do conteúdo dado usando o algoritmo SHA-256.
     * <p>
     * Esse método converte o conteúdo em bytes e calcula seu hash, retornando-o
     * como uma string hexadecimal.
     * </p>
     * 
     * @param content O conteúdo a ser hashado.
     * @return O hash do conteúdo em formato hexadecimal.
     */
    private String hashContent(String content) {
        try {
            MessageDigest codingResponse = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = codingResponse.digest(content.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
