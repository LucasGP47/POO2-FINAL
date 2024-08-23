package model;


/**
 * Representa um site a ser monitorado pelo sistema.
 * <p>
 * Esta classe encapsula os detalhes de um site.
 * </p>
 */
public class Site {
    private String url;
    private String lastContent;
    private boolean isOnline;
    private String lastChangeTime;

    /**
     * Constrói um novo objeto Site com a URL especificada.
     * 
     * @param url a URL do site a ser monitorado
     */
    public Site(String url) {
        this.url = ensureHttpProtocol(url);
    }
    
    /**
     * Retorna a URL do site.
     * 
     * @return a URL do site
     */
    public String getUrl() {
        return url;
    }

    /**
     * Define a URL do site.
     * <p>
     * Se a URL não começar com "http://" ou "https://", 
     * o prefixo "http://" será adicionado automaticamente.
     * </p>
     * 
     * @param url a nova URL do site
     */
    public void setUrl(String url) {
        this.url = ensureHttpProtocol(url);
    }

    /**
     * Retorna o último conteúdo recuperado do site.
     * 
     * @return o último conteúdo do site
     */
    public String getLastContent() {
        return lastContent;
    }

    /**
     * Define o último conteúdo recuperado do site.
     * 
     * @param lastContent o novo conteúdo do site
     */
    public void setLastContent(String lastContent) {
        this.lastContent = lastContent;
    }

    /**
     * Retorna o status atual de online do site.
     * 
     * @return true se o site estiver online, false se estiver offline
     */
    public boolean isOnline() {
        return isOnline;
    }

    /**
     * Retorna o horário da última alteração detectada no conteúdo do site.
     * 
     * @return o horário da última alteração
     */
    public String getLastChangeTime() {
		return lastChangeTime;
	}

    /**
     * Define o horário da última alteração detectada no conteúdo do site.
     * 
     * @param lastChangeTime o novo horário da última alteração
     */
	public void setLastChangeTime(String lastChangeTime) {
		this.lastChangeTime = lastChangeTime;
	}

    /**
     * Define o status de online do site.
     * 
     * @param isOnline true se o site estiver online, false se estiver offline
     */
	public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    /**
     * Garante que a URL tenha um protocolo HTTP ou HTTPS.
     * <p>
     * Se a URL não começar com "http://" ou "https://", 
     * o prefixo "http://" será adicionado automaticamente.
     * </p>
     * 
     * @param url a URL a ser verificada
     * @return a URL com o protocolo adequado
     */
    private String ensureHttpProtocol(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "http://" + url;
        }
        return url;
    }
}
