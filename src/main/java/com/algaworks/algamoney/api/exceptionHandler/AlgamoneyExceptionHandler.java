package com.algaworks.algamoney.api.exceptionHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Class para tratamento de erros
 * Captura todos os erros dos endpoints
 * 
 * Sobrepor as classes para os tipos específicos de erros conforme necessário
 * 
 * O método handleExceptionInternal é o mais genérico
 * 
 * @author rodrigo
 */
@RestControllerAdvice
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	MessageSource messageSource;

	/**
	 * Tratamento de erro para recebimento de objetos nos endpoints / requests
	 * A prop spring.jackson.deserialization.fail-on-unknown-properties no application.properties
	 * Dispara uma exceção se receber um atributo não conhecido no bean informado na assinatura do método
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return handleExceptionInternal(ex, new ErrorMessage(messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale()), ex), headers, HttpStatus.BAD_REQUEST, request);
	}

	/**
	 * Tratamento de erros genéricos
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		// os outros métodos invocam este método - então verificar se body já é uma instância de ErrorMessage
		// se não for instanciar, para garantir a gravação do log em arquivo + geração de timestamp p/ enviar no http response
		// para facilitar o suporte
		if(body == null || !(body instanceof ErrorMessage)) {
			body = new ErrorMessage(messageSource.getMessage("erro.generico", null, LocaleContextHolder.getLocale()), ex);
		}
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
}
