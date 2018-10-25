package com.algaworks.algamoney.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configurações da aplicação Referenciado por anotação na classe App / main
 *
 * @author s2it_rboni
 * @since 25/10/18 18:16
 */
@ConfigurationProperties("algamoney")
public class AlgamoneyApiProperty {

	private final Seguranca seguranca = new Seguranca();

	public static class Seguranca {

		private boolean enableHttps;

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}
	}
}
