package main;

import assists.GetSiteURL;
import facade.SiteMonitoringFacade;
import assists.GetPhoneNumber;
import model.Site;

import java.util.List;

/**
 * Classe principal que inicializa o sistema de monitoramento de sites.
 */
public class Main {
    private static boolean stopMonitoring = false;
 
    public static void main(String[] args) {
        List<String> phoneNumbers = GetPhoneNumber.collectPhoneNumbers();
        List<Site> sites = GetSiteURL.collectSites();

        SiteMonitoringFacade facade = new SiteMonitoringFacade(sites, phoneNumbers);

        //(true para GUI, false para console)
        boolean useGui = false;  
        facade.startMonitoring(useGui);
    }

    /**
     * Verifica se o monitoramento deve ser interrompido.
     * 
     * @return Verdadeiro se o monitoramento deve parar, falso caso contrário.
     */
    public static boolean shouldStopMonitoring() {
        return stopMonitoring;
    }

    /**
     * Interrompe o monitoramento quando chamado.
     */
    public static void stopMonitoring() {
        stopMonitoring = true;
    }
}
