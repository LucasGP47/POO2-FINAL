package facade;

import model.Site;
import service.MonitoringService;
import service.SiteMonitorWindow;

import javax.swing.*;
import java.util.List;
/**
 * Facade que coordena o monitoramento de sites e o envio de notificações via WhatsApp.
 */
public class SiteMonitoringFacade {
    private final List<Site> sites;
    private final List<String> phoneNumbers;
    private MonitoringService monitoringService;

    /**
     * Inicializa uma instância de facade de monitoramento de sites.
     * 
     * @param sites Uma lista de objetos do tipo Site a serem monitorados.
     * @param phoneNumbers Uma lista de números de telefone para envio de notificações.
     */
    public SiteMonitoringFacade(List<Site> sites, List<String> phoneNumbers) {
        this.sites = sites;
        this.phoneNumbers = phoneNumbers;
    }

    
    /**
     * Inicia o monitoramento dos sites utilizando uma interface gráfica (GUI).
     */
    public void startMonitoringWithGui() {
        SwingUtilities.invokeLater(() -> {
            new SiteMonitorWindow(sites, phoneNumbers);
        });
    }

    /**
     * Inicia o monitoramento dos sites utilizando a linha de comando (console).
     */   
    public void startMonitoringWithConsole() {
        monitoringService = new MonitoringService(sites, phoneNumbers, null, null, false); 
        monitoringService.startMonitoring();
    }
    
    /**
     * Inicia o monitoramento dos sites, utilizando GUI ou console, dependendo do parâmetro.
     * 
     * @param useGui Se verdadeiro, utiliza GUI; caso contrário, utiliza o console.
     */
    public void startMonitoring(boolean useGui) {
        if (useGui) {
            startMonitoringWithGui();
        } else {
            startMonitoringWithConsole();
        }
    }
}
