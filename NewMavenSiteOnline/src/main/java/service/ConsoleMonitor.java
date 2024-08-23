package service;

import model.Site;
import main.Main;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Serviço de monitoramento de sites que exibe resultados no console.
 * <p>
 * Esta classe monitora uma lista de sites, verificando periodicamente 
 * se estão online e se o conteúdo mudou. Além disso, envia notificações 
 * quando um site fica offline e registra logs em um arquivo.
 * </p>
 */
public class ConsoleMonitor {
    private final List<Site> sites;
    private final SiteChecker siteChecker;
    private final NotificationService notificationService;
    private final LogService logService;
    private final ExecutorService executor;
    private int checkInterval;
    
    private final Map<String, Boolean> notificationSentMap = new HashMap<>();

    /**
     * Inicializa uma instância do monitoramento de console.
     * 
     * @param sites Uma lista de sites a serem monitorados.
     * @param phoneNumbers Uma lista de números de telefone para envio de notificações.
     */
    public ConsoleMonitor(List<Site> sites, List<String> phoneNumbers) {
        this.sites = sites;
        this.siteChecker = new SiteChecker();
        this.notificationService = new NotificationService(phoneNumbers);
        this.logService = new LogService("messages_log.txt");
        setCheckInterval();
        executor = Executors.newSingleThreadExecutor();

        for (Site site : sites) {
            notificationSentMap.put(site.getUrl(), false);
        }
    }

    /**
     * Inicia o monitoramento dos sites no console.
     * <p>
     * O monitoramento é realizado em um thread separado para permitir a execução contínua.
     * </p>
     */
    public void startMonitoring() {
        executor.submit(this::monitorSites);
    }

    /**
     * Define o intervalo de verificação dos sites, solicitando ao usuário 
     * que insira o valor em segundos.
     * <p>
     * Caso o valor inserido seja inválido, será usado um valor padrão de 10 segundos.
     * </p>
     */
    private void setCheckInterval() {
        System.out.println("Digite o intervalo de verificação (em segundos):");
        try {
            checkInterval = Integer.parseInt(System.console().readLine()) * 1000;
        } catch (NumberFormatException e) {
            checkInterval = 10000;  // Padrão = 10 segundos
        }
    }

    /**
     * Monitora os sites continuamente até que o sinal para parar seja dado.
     * <p>
     * Verifica se os sites estão online, se o conteúdo mudou, envia notificações
     * e imprime o status de cada site no console.
     * </p>
     */
    private void monitorSites() {
        while (!Main.shouldStopMonitoring()) {
            for (Site site : sites) {
                boolean isOnline = siteChecker.isSiteOnline(site);
                boolean hasChanged = isOnline && siteChecker.hasSiteChanged(site);

                if (!isOnline && !notificationSentMap.get(site.getUrl())) {
                    notificationService.sendOfflineNotification(site.getUrl());
                    notificationSentMap.put(site.getUrl(), true);
                }

                if (isOnline && notificationSentMap.get(site.getUrl())) {
                    notificationSentMap.put(site.getUrl(), false);
                }

                if (hasChanged) {
                    String currentTime = logService.formatCurrentDateTime();
                    site.setLastChangeTime(currentTime);  // Atualiza o tempo de mudança no Site
                }

                printSiteStatus(site, isOnline, hasChanged, site.getLastChangeTime());
            }

            countdownToNextCheck();
        }

        executor.shutdownNow();
    }

    /**
     * Imprime o status atual do site no console.
     * 
     * @param site O site que está sendo monitorado.
     * @param isOnline Indica se o site está online.
     * @param hasChanged Indica se o conteúdo do site mudou.
     * @param lastChangeTime O horário da última alteração detectada no conteúdo do site.
     */
    private void printSiteStatus(Site site, boolean isOnline, boolean hasChanged, String lastChangeTime) {
        System.out.printf("URL: %s | Status: %s | Mudou?: %s | Última Mudança: %s%n",
                site.getUrl(),
                isOnline ? "Online" : "Offline",
                hasChanged ? "Sim" : "Não",
                lastChangeTime != null ? lastChangeTime : "N/A"
        );
    }

    /**
     * Conta regressivamente até a próxima verificação.
     * <p>
     * Durante a contagem, o tempo restante até a próxima verificação é impresso no console.
     * </p>
     */
    private void countdownToNextCheck() {
        for (int i = checkInterval / 1000; i >= 0; i--) {
            System.out.println("Próxima verificação em: " + i + "s");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }
}
