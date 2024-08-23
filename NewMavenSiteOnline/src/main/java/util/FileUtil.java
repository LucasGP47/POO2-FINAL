package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Classe utilitária para operações com arquivos.
 * <p>
 * Esta classe oferece métodos utilitários para facilitar a manipulação de arquivos,
 * como a leitura do conteúdo de um arquivo como uma string.
 * </p>
 */
public class FileUtil {
	
    /**
     * Lê o conteúdo de um arquivo e o retorna como uma string.
     * <p>
     * Esse método lê todos os bytes de um arquivo localizado no caminho especificado
     * e converte esses bytes em uma string.
     * </p>
     *
     * @param path O caminho do arquivo a ser lido.
     * @return O conteúdo do arquivo como uma string.
     * @throws IOException Se ocorrer um erro ao tentar ler o arquivo.
     */
    public static String readFileContent(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
