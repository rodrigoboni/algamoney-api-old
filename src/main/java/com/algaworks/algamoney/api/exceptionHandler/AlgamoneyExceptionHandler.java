package com.algaworks.algamoney.api.exceptionHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
	
	private Logger logger;

	/**
	 * Tratamento de erro para recebimento de objetos nos endpoints / requests
	 * A prop spring.jackson.deserialization.fail-on-unknown-properties no application.properties
	 * Dispara uma exceção se receber um atributo não conhecido no bean informado na assinatura do método
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return handleExceptionInternal(ex, 
				new ErrorMessage(messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale())), 
				headers, 
				HttpStatus.BAD_REQUEST, 
				request);
	}
	
	/**
	 * Tratamento de erro para bean validations
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return handleExceptionInternal(ex, getFieldValidationErrors(ex), headers, status, request);
	}
	
	/**
	 * Tratamento de exception específica (EmptyResultDataAccessException)
	 * Dispara ao tentar excluir registro que não existe, por exemplo
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	//A anotação exceptionhandler define qual exception o método deve tratar
	@ExceptionHandler({EmptyResultDataAccessException.class})
	public ResponseEntity<Object> handleEmptyResultDataAccessException(RuntimeException ex, WebRequest request) {
		return handleExceptionInternal(ex, 
				new ErrorMessage(messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale())), 
				new HttpHeaders(), 
				HttpStatus.NOT_FOUND, 
				request);
	}

	/**
	 * Tratamento de erros genéricos
	 * os outros métodos desta classe invocam este método sempre
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if(body == null || !(body instanceof ErrorMessage)) {
			body = new ErrorMessage(messageSource.getMessage("erro.generico", null, LocaleContextHolder.getLocale()));
		}
		
		persistLog(body, ex);
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	/**
	 * Lançar logs no arquivo configurado no log4j
	 * @param body
	 * @param ex
	 */
	private void persistLog(Object body, Exception ex) {
		if(body == null || !(body instanceof ErrorMessage)) {
			return;
		}
		
		logger = LoggerFactory.getLogger(ex.getClass());
		
		ErrorMessage auxErrorMessage = (ErrorMessage)body;
		
		StringBuilder logMsg = new StringBuilder();
		logMsg.append("\n"+auxErrorMessage.getErrorId()+"\n");
		
		if(auxErrorMessage.getMessages() != null && !auxErrorMessage.getMessages().isEmpty()) {
			for (Iterator<String> it = auxErrorMessage.getMessages().iterator(); it.hasNext();) {
				String message = (String) it.next();
				logMsg.append(message+"\n");
			}
		}
		
		logger.error(logMsg.toString(), ex);
	}
	
	/**
	 * Percorrer lista de validações e montar objeto para mensagem de erro
	 * @param ex
	 * @return
	 */
	private ErrorMessage getFieldValidationErrors(MethodArgumentNotValidException ex) {
		if(ex == null || ex.getBindingResult() == null || !ex.getBindingResult().hasFieldErrors()) {
			return null;
		}
		
		List<String> fieldErrors = new ArrayList<>();
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			fieldErrors.add(messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()));
		}
		
		return new ErrorMessage(fieldErrors);
	}
}
