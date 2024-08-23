package service;

import model.Site;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Serviço de monitoramento de sites que pode utilizar uma GUI ou console.
 * <p>
 * Esse serviço monitora uma lista de sites, verificando sua disponibilidade e se seu conteúdo mudou.
 * Notificações são enviadas via WhatsApp quando um site fica offline e logs são gravados em um arquivo.
 * O serviço pode ser executado com uma interface gráfica (GUI) ou no console.
 * </p>
 */
public class MonitoringService {
    private final List<Site> sites;
    private final JPanel sitesPanel;
    private final JLabel countdownLabel;
    private final SiteChecker siteChecker;
    private final NotificationService notificationService;
    private final LogService logService;
    private final ExecutorService executor;
    private int checkInterval;
    
    private final Map<String, Boolean> notificationSentMap = new HashMap<>();
    private final boolean useGui;

    /**
     * Inicializa uma instância do serviço de monitoramento.
     * 
     * @param sites Uma lista de sites a serem monitorados.
     * @param phoneNumbers Uma lista de números de telefone para envio de notificações.
     * @param sitesPanel O painel de sites para GUI (pode ser null).
     * @param countdownLabel O rótulo de contagem regressiva para GUI (pode ser null).
     * @param useGui Se verdadeiro, usa GUI; caso contrário, usa console.
     */
    public MonitoringService(List<Site> sites, List<String> phoneNumbers, JPanel sitesPanel, JLabel countdownLabel, boolean useGui) {
        this.sites = sites;
        this.sitesPanel = sitesPanel;
        this.countdownLabel = countdownLabel;
        this.useGui = useGui;
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
     * Inicia o monitoramento dos sites.
     * <p>
     * Esse método inicia o processo de monitoramento em uma thread separada, onde os sites são periodicamente verificados.
     * </p>
     */
    public void startMonitoring() {
        executor.submit(this::monitorSites);
    }

    /**
     * Define o intervalo de verificação em milissegundos, baseado na entrada do usuário.
     * <p>
     * Se a interface gráfica estiver sendo utilizada, um diálogo será exibido para o usuário inserir o intervalo.
     * Caso contrário, o intervalo será solicitado via console.
     * </p>
     */
    private void setCheckInterval() {
        if (useGui) {
            String intervalStr = JOptionPane.showInputDialog(null, "Digite o intervalo de verificação (em segundos):", "Configuração de Intervalo", JOptionPane.QUESTION_MESSAGE);
            try {
                checkInterval = Integer.parseInt(intervalStr) * 1000;
            } catch (NumberFormatException e) {
                checkInterval = 10000;  
            }
        } else {
        	System.out.print("Digite o intervalo de verificação (em segundos): ");
        	Scanner scanner = new Scanner(System.in);
            checkInterval = scanner.nextInt() * 1000;
            scanner.close();
        }
    }

    /**
     * Método principal que realiza o monitoramento dos sites.
     * <p>
     * Esse método verifica a disponibilidade e as mudanças no conteúdo dos sites,
     * atualiza a interface gráfica ou imprime os resultados no console, e envia
     * notificações se um site ficar offline.
     * </p>
     */
    private void monitorSites() {
        while (!Main.shouldStopMonitoring()) {
            if (sitesPanel != null) {
                sitesPanel.removeAll();
            }

            for (Site site : sites) {
                JPanel sitePanel = null;
                if (sitesPanel != null) {
                    sitePanel = createSitePanel(site);
                    sitesPanel.add(sitePanel);
                }

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
                    site.setLastChangeTime(currentTime);
                }

                if (sitesPanel != null && sitePanel != null) {
                    updateSitePanel(sitePanel, isOnline, hasChanged, site.getLastChangeTime());
                }

                if (!useGui) {
                    System.out.println("URL: " + site.getUrl() + " | " + (isOnline ? "Online" : "Offline") + " | Mudou?: " + (hasChanged ? "Sim" : "Não") + " | Última Mudança: " + site.getLastChangeTime());
                }
            }

            if (sitesPanel != null) {
                sitesPanel.revalidate();
                sitesPanel.repaint();
            }

            countdownToNextCheck();
        }

        executor.shutdownNow();
    }

    /**
     * Conta o tempo até a próxima verificação e atualiza a GUI ou console.
     * <p>
     * Esse método atualiza a contagem regressiva na interface gráfica, se disponível,
     * ou imprime o tempo restante até a próxima verificação no console.
     * </p>
     */
    private void countdownToNextCheck() {
        for (int i = checkInterval / 1000; i >= 0; i--) {
            if (countdownLabel != null) {
                int finalI = i;
                SwingUtilities.invokeLater(() -> countdownLabel.setText("Próxima verificação em: " + finalI + "s"));
            } else if (!useGui) {
                System.out.println("Próxima verificação em: " + i + "s");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    /**
     * Cria um painel de site na interface gráfica.
     * <p>
     * Esse método cria um painel para um site específico, exibindo sua URL,
     * status atual, e se houve mudança de conteúdo.
     * </p>
     * 
     * @param site O site para o qual o painel será criado.
     * @return Um painel preenchido com informações do site.
     */
    private JPanel createSitePanel(Site site) {
        JPanel sitePanel = new JPanel(new GridLayout(1, 4));
        sitePanel.add(new JLabel(site.getUrl()));
        sitePanel.add(new JLabel("Verificando..."));
        sitePanel.add(new JLabel("Verificando..."));
        sitePanel.add(new JLabel("Última mudança: N/A"));
        return sitePanel;
    }


    /**
     * Atualiza o painel de site na interface gráfica.
     * <p>
     * Esse método atualiza o status de um site no painel, incluindo sua disponibilidade,
     * se houve mudança de conteúdo, e o horário da última mudança.
     * </p>
     * 
     * @param sitePanel O painel do site a ser atualizado.
     * @param isOnline O status de disponibilidade do site.
     * @param hasChanged Se o conteúdo do site mudou.
     * @param lastChangeTime O horário da última mudança no conteúdo.
     */
    private void updateSitePanel(JPanel sitePanel, boolean isOnline, boolean hasChanged, String lastChangeTime) {
        JLabel statusLabel = (JLabel) sitePanel.getComponent(1);
        JLabel changeLabel = (JLabel) sitePanel.getComponent(2);
        JLabel lastChangeLabel = (JLabel) sitePanel.getComponent(3);

        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(isOnline ? "Online" : "Offline");
            statusLabel.setForeground(isOnline ? Color.GREEN : Color.RED);
            changeLabel.setText("Mudou: " + (hasChanged ? "Sim" : "Não"));
            lastChangeLabel.setText("Última mudança: " + lastChangeTime);
        });
    }
}
