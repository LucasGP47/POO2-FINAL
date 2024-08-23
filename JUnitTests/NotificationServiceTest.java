package JUnitTests;
import org.junit.jupiter.api.Test;

import service.NotificationService;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class NotificationServiceTest {

    @Test
    public void testSendOfflineNotification() {
        List<String> phoneNumbers = List.of("+5512991527253");

        NotificationService notificationService = new NotificationService(phoneNumbers);
        try {
            notificationService.sendOfflineNotification("http://example.com");
            assertTrue(true);
        } catch (Exception e) {
            fail("An exception was thrown during notification: " + e.getMessage());
        }
    }
}
