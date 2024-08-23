package service;

import model.Site;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Janela de interface gráfica para monitoramento de sites.
 * <p>
 * Esta classe cria uma janela que exibe o status de uma lista de sites em tempo real,
 * permitindo que o usuário visualize a contagem regressiva para a próxima verificação
 * e o status atual de cada site.
 * </p>
 */
@SuppressWarnings("serial")
public class SiteMonitorWindow extends JFrame {
    private final MonitoringService monitoringService;
    private final JLabel countdownLabel;
    private final JPanel sitesPanel;

    /**
     * Construtor que inicializa a janela de monitoramento de sites.
     * 
     * @param sites Uma lista de sites a serem monitorados.
     * @param phoneNumbers Uma lista de números de telefone para envio de notificações.
     */
    public SiteMonitorWindow(List<Site> sites, List<String> phoneNumbers) {
        setTitle("Monitoramento de Sites");
        setSize(800, 600);
        setLayout(new BorderLayout());
        countdownLabel = new JLabel();
        sitesPanel = new JPanel();
        sitesPanel.setLayout(new GridLayout(sites.size(), 1, 10, 10));
        add(countdownLabel, BorderLayout.NORTH);
        add(new JScrollPane(sitesPanel), BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        monitoringService = new MonitoringService(sites, phoneNumbers, sitesPanel, countdownLabel, true);
        monitoringService.startMonitoring();
    }
}
