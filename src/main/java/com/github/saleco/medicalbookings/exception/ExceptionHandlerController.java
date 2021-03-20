package com.github.saleco.medicalbookings.exception;

import com.github.saleco.medicalbookings.utils.MedicalBookingsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.UndeclaredThrowableException;

@Slf4j
@ControllerAdvice("com.github.saleco.medicalbookings")
public class ExceptionHandlerController {


	private static final String START_TAG = "{";

	private static final String END_TAG = "}";

	@ExceptionHandler(value = { ConstraintViolationException.class, MethodArgumentNotValidException.class,
			BusinessException.class, Exception.class })
	protected ResponseEntity<Object> handleBusinessException(WebRequest request, Exception ex) {
		if (ex instanceof ConstraintViolationException) {
			StringBuilder message = new StringBuilder("There is a validation rule that prevents the request. See rule | ");
			ConstraintViolationException cve = (ConstraintViolationException) ex;
			for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
				ConstraintDescriptor<? extends Annotation> descriptor = violation.getConstraintDescriptor();
				Annotation annotation = descriptor.getAnnotation();
				String annotationName = annotation.annotationType().getCanonicalName();
				if (annotationName != null) {
					if (annotationName.endsWith("NotNull")) {
						message.append(START_TAG + violation.getPropertyPath() + ",mandatory" + END_TAG);
					}
					if (annotationName.endsWith("Size")) {
						String min = "" + descriptor.getAttributes().get("min");
						String max = "" + descriptor.getAttributes().get("max");
						message.append(START_TAG + violation.getPropertyPath() + ",size," + min + "-" + max + END_TAG);
					}
					if (annotationName.endsWith("Pattern")) {
						String pattern = "" + descriptor.getAttributes().get("regexp");
						message.append(START_TAG + violation.getPropertyPath() + ",pattern," + pattern + END_TAG);
					}
				}
			}

			log.info("ValidationException: %s", message, ex);
			ex = new ValidationException(message.toString());

		}

		if (ex instanceof MethodArgumentNotValidException) {
			StringBuilder message = new StringBuilder("There is a validation rule that prevents the request. See rule | ");
			MethodArgumentNotValidException nve = (MethodArgumentNotValidException) ex;
			BindingResult bindingResult = nve.getBindingResult();
			for (ObjectError error : bindingResult.getAllErrors()) {
				String code = error.getCode();
				String f = error.getCodes()[1];
				String field = f.substring(f.indexOf('.') + 1);

				if ("NotNull".equals(code)) {
					message.append(START_TAG + field + ",mandatory" + END_TAG);
				}
				if ("Size".equals(code)) {
					String min = "" + error.getArguments()[2];
					String max = "" + error.getArguments()[1];
					message.append(START_TAG + field + ",size," + min + "-" + max + END_TAG);
				}
				if ("Pattern".equals(code)) {
					String full = error.getDefaultMessage();
					String[] parts = full.split("\"");
					String pattern = "";
					if (parts.length > 1) {
						pattern = parts[1];
					}
					message.append(START_TAG + field + ",pattern," + pattern + END_TAG);
				}
				if("Min".equals(code)){
					String min = "" + error.getArguments()[1];

					message.append(START_TAG + field + ",min," + min + END_TAG);
				}
			}

			log.info("ValidationException: %s", message, ex);
			ex = new ValidationException(message.toString());
		}

		if (ex instanceof BindException) {
			StringBuilder message = new StringBuilder("There is a validation rule that prevents the request. See rule | ");
			BindException nve = (BindException) ex;
			BindingResult bindingResult = nve.getBindingResult();
			for (ObjectError error : bindingResult.getAllErrors()) {
				String code = error.getCode();
				String f = error.getCodes()[1];
				String field = f.substring(f.indexOf('.') + 1);

				if ("NotNull".equals(code)) {
					message.append(START_TAG + field + ",mandatory" + END_TAG);
				}
				if ("Size".equals(code)) {
					String min = "" + error.getArguments()[2];
					String max = "" + error.getArguments()[1];
					message.append(START_TAG + field + ",size," + min + "-" + max + END_TAG);
				}
				if ("Pattern".equals(code)) {
					String full = error.getDefaultMessage();
					String[] parts = full.split("\"");
					String pattern = "";
					if (parts.length > 1) {
						pattern = parts[1];
					}
					message.append(START_TAG + field + ",pattern," + pattern + END_TAG);
				}
				if("Min".equals(code)){
					String min = "" + error.getArguments()[1];

					message.append(START_TAG + field + ",min," + min + END_TAG);
				}
			}

			log.info("ValidationException: %s", message, ex);
			ex = new ValidationException(message.toString());
		}

		if (ex instanceof MedicalBookingsException ) {
			HttpStatus status = ex.getClass().getAnnotation(ResponseStatus.class).value();
			String message = ((MedicalBookingsException) ex).getMessage();
			MedicalBookingsResponse response = new MedicalBookingsResponse(status.value(), message);
			log.error(message);
			return ResponseEntity.status(status).body(response);
		}

		if(ex instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) ex).getUndeclaredThrowable().getCause() instanceof MedicalBookingsException){
			MedicalBookingsException exception = (MedicalBookingsException) ((UndeclaredThrowableException) ex).getUndeclaredThrowable().getCause();
			HttpStatus status = exception.getClass().getAnnotation(ResponseStatus.class).value();
			String message = exception.getMessage();
			MedicalBookingsResponse response = new MedicalBookingsResponse(status.value(), message);
			log.error(message);
			return ResponseEntity.status(status).body(response);
		}

		log.error(
				"Unknown Exception detected in ExceptionHandlerController", ex);

		MedicalBookingsResponse response = new MedicalBookingsResponse(HttpStatus.BAD_REQUEST.value(), "We are sorry, something unexpected happened. Please try it again later.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(response);
	}
}
