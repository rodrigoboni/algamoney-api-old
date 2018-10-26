package com.algaworks.algamoney.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configurações da aplicação Referenciado por anotação na classe App / main
 *
 * @author s2it_rboni
 * @since 25/10/18 18:16
 */
@ConfigurationProperties("appProperties")
public class ApplicationProperties {

	private final Security security = new Security();

	public Security getSecurity() {
		return security;
	}

	public static class Security {

		private boolean enableHttps;
		private String allowedOrigin = "*";

		
		public String getAllowedOrigin() {
			return allowedOrigin;
		}

		
		public void setAllowedOrigin(String allowedOrigin) {
			this.allowedOrigin = allowedOrigin;
		}

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}
	}
}
