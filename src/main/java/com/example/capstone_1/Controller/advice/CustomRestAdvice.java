package com.example.capstone_1.controller.advice;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


//이 코드는 Spring Boot에서 RESTful API를 개발할 때 예외 처리를 위한 컨트롤러 어드바이스(Advice) 클래스입니다. `@RestControllerAdvice` 어노테이션을 사용하여 모든 REST 컨트롤러에 대한 예외 처리를 담당합니다.
//
//        주요 기능은 다음과 같습니다:
//
//        1. `handleBindException`: `BindException` 예외를 처리하는 메서드로, 요청의 데이터 바인딩이 실패한 경우 발생하는 예외입니다. 주로 유효성 검사 실패와 관련된 에러를 반환합니다. 바인딩 오류 정보를 `Map` 형태로 담아서 클라이언트로 전송합니다.
//
//        2. `handleFKException`: `DataIntegrityViolationException` 예외를 처리하는 메서드로, 데이터베이스 제약 조건 위반 등의 무결성 관련 예외를 처리합니다. 주로 외래 키 제약 조건에 맞지 않는 데이터 삽입 등에서 발생합니다. 오류 메시지를 `Map` 형태로 담아서 클라이언트로 전송합니다.
//
//        3. `handleNoSuchElement`: `NoSuchElementException`과 `EmptyResultDataAccessException` 예외를 처리하는 메서드로, 조회 결과가 없는 경우에 발생합니다. 주로 데이터를 조회하는데 해당 데이터가 존재하지 않는 경우 발생합니다. 오류 메시지를 `Map` 형태로 담아서 클라이언트로 전송합니다.
//
//        이렇게 예외 처리를 별도의 어드바이스 클래스에서 처리하는 것은 코드의 가독성과 유지보수성을 높이는데 도움이 됩니다. 또한 각 예외에 대한 처리를 일괄적으로 관리할 수 있습니다. 프론트엔드에서 API를 호출하면, 이렇게 처리된 예외 메시지를 받아서 적절하게 화면에 표시하거나 사용자에게 알려줄 수 있습니다.


//Rest방식의 컨트롤러는 Ajax와 같이 눈에 보이지 않는 방식으로 서버 호출
//결과 전송함 따라서 에러가 발생시 어디에서 어떤 에러가 발생했는지 알아보기 힘듦
//@Valid과정에서 문제가 발생하면 처리할 수 있도록 @RestControllerAdvice를 설계하자.
@RestControllerAdvice
@Log4j2
public class CustomRestAdvice {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleBindException(BindException e) {

        log.error(e);

        Map<String, String> errorMap = new HashMap<>();

        if(e.hasErrors()){

            BindingResult bindingResult = e.getBindingResult();

            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMap.put(fieldError.getField(), fieldError.getCode());
            });
        }

        return ResponseEntity.badRequest().body(errorMap);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleFKException(Exception e) {

        log.error(e);

        Map<String, String> errorMap = new HashMap<>();

        errorMap.put("time", ""+System.currentTimeMillis());
        errorMap.put("msg",  "constraint fails");
        return ResponseEntity.badRequest().body(errorMap);
    }


    @ExceptionHandler({
            NoSuchElementException.class,
            EmptyResultDataAccessException.class }) //추가
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleNoSuchElement(Exception e) {

        log.error(e);

        Map<String, String> errorMap = new HashMap<>();

        errorMap.put("time", ""+System.currentTimeMillis());
        errorMap.put("msg",  "No Such Element Exception");
        return ResponseEntity.badRequest().body(errorMap);
    }

}
