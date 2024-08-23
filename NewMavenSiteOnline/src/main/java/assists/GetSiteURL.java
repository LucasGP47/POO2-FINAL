package assists;

import model.Site;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe responsável por coletar URLs de sites do usuário para monitoramento.
 */
public class GetSiteURL {
    /**
     * Coleta URLs de sites inseridos pelo usuário no console e os armazena em uma lista do tipo Site, da classe de mesmo nome.
     * 
     * @return Uma lista de objetos do tipo Site.
     */
    public static List<Site> collectSites() {
        List<Site> sites = new ArrayList<>();
        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

        System.out.println("Digite os links dos sites (digite 'sair' para terminar):");
        while (true) {
            String url = scanner.nextLine();
            if (url.equalsIgnoreCase("sair")) {
                break;
            }
            sites.add(new Site(url));
        }
        return sites;
    }
}
