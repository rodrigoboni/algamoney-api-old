package com.algaworks.algamoney.api.exceptionHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe / bean para representar erro no http response
 * Retorna a mensagem + id do erro, p/ auxiliar no suporte
 * Grava detalhes do erro no arquivo de log, junto com o id do erro
 * 
 * @author rodrigo
 *
 */
public class ErrorMessage {
	private String message;
	private String errorId;
	
	private final Logger logger;

	public ErrorMessage(String message, Exception exception) {
		this.message = message;
		this.errorId = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String logMsg = errorId + " - " + message;
		
		logger = LoggerFactory.getLogger(exception.getClass());
		logger.error(logMsg, exception);
	}

	public String getMessage() {
		return message;
	}

	public String getErrorId() {
		return errorId;
	}
}
