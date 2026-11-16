package ar.edu.daw1.tpintegrador;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> manejarValidaciones(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.putIfAbsent(
                        error.getField(),
                        error.getDefaultMessage()
                )
        );

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Hay errores en los datos enviados"
        );

        problema.setTitle("Error de validación");
        problema.setProperty("errores", errores);

        return ResponseEntity.badRequest().body(problema);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> manejarJsonInvalido(
            HttpMessageNotReadableException ex) {

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "El JSON enviado no es válido"
        );

        problema.setTitle("Solicitud incorrecta");

        return ResponseEntity.badRequest().body(problema);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> manejarTipoIncorrecto(
            MethodArgumentTypeMismatchException ex) {

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "El parámetro tiene un formato incorrecto"
        );

        problema.setTitle("Solicitud incorrecta");

        return ResponseEntity.badRequest().body(problema);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> manejarEstadoHttp(
            ResponseStatusException ex) {

        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                status,
                ex.getReason() != null ? ex.getReason() : "Error en la solicitud"
        );

        problema.setTitle(status.getReasonPhrase());

        return ResponseEntity.status(status).body(problema);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> manejarConflictos(
            DataIntegrityViolationException ex) {

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "El dato ya existe o infringe una restricción de la base de datos"
        );

        problema.setTitle("Conflicto");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problema);
    }
}
