package service;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;

import java.util.List;

/**
 * Serviço responsável por enviar notificações usando a API do Twilio.
 */
public class NotificationService {
    public static final String ACCOUNT_SID = "ACfa76215834903571cb7b2440258ca942";
    public static final String AUTH_TOKEN = "77b859ae1cd29737776b5fdf25bf333f";
    private final List<String> phoneNumbers;

    /**
     * Inicializa uma instância do serviço de notificações.
     * 
     * @param phoneNumbers Uma lista de números de telefone para envio de notificações.
     */
    public NotificationService(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    /**
     * Envia uma notificação via WhatsApp quando um site está offline.
     * 
     * @param url A URL do site que está offline.
     */
    public void sendOfflineNotification(String url) {
        String messageContent = "AVISO: A URL: " + url + " está fora do ar.";
        for (String phoneNumber : phoneNumbers) {
            try {
                Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("whatsapp:" + phoneNumber),
                        new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
                        messageContent
                ).create();
                System.out.println("Mensagem enviada: " + message.getSid());
            } catch (ApiException e) {
                System.err.println("Falha ao enviar a mensagem: " + e.getMessage());
            }
        }
    }
}
