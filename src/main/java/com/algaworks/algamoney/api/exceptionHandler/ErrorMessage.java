package com.algaworks.algamoney.api.exceptionHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe / bean para representar erros no http response
 * Retorna as mensagens + id do erro, p/ auxiliar no suporte
 * 
 * @author rodrigo
 *
 */
public class ErrorMessage {
	private List<String> messages;
	private String errorId;
	
	public ErrorMessage(String message) {
		ArrayList<String> messageList = new ArrayList<>();
		messageList.add(message);
		this.messages = messageList;
		this.errorId = getErrorID();
	}
	
	public ErrorMessage(List<String> messages) {
		this.messages = messages;
		this.errorId = getErrorID();
	}

	public List<String> getMessages() {
		return messages;
	}

	public String getErrorId() {
		return errorId;
	}
	
	private String getErrorID() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
}
