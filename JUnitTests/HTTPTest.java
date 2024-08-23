package JUnitTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.HttpServer;

import util.HttpUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class HTTPTest {

    private HttpServer server;

    @BeforeEach
    public void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        server.createContext("/success", exchange -> {
            String response = "Success response";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        server.createContext("/error", exchange -> {
            String response = "Error response";
            exchange.sendResponseHeaders(404, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        server.setExecutor(null); 
        server.start();
    }

    @AfterEach
    public void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    public void testGetHttpResponse_Success() throws IOException {
        String response = HttpUtil.getHttpResponse("http://localhost:8080/success");
        assertEquals("Success response", response);
    }

    @Test
    public void testGetHttpResponse_Error() {
        IOException exception = assertThrows(IOException.class, () -> {
            HttpUtil.getHttpResponse("http://localhost:8080/error");
        });
        
        assertEquals("Erro ao obter resposta HTTP. Código de resposta: 404", exception.getMessage());
    }
}
