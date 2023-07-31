package com.example.memorip.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */


    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않는 토큰입니다"),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "계정 정보가 존재하지 않습니다"),

    /* 403 FORBIDDEN : 접근 권한 없음 */
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),
    ACCESS_DENIED_PLAN(HttpStatus.FORBIDDEN, "접근할 수 있는 여행계획이 아닙니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 여행계획 정보를 찾을 수 없습니다"),
    TIMELINE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 타임라인 정보를 찾을 수 없습니다"),
    TRAVEL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 여행기 정보를 찾을 수 없습니다"),
    USER_TRAVEL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 여행기를 작성할 수 있는 사용자가 아닙니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "데이터가 이미 존재합니다"),

    /* 500 INTERNAL_SERVER_ERROR : 서버 내부 에러 */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러")
    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
