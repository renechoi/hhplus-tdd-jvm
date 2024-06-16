package io.hhplus.tdd.point.common.logtrace;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */

@Getter
@AllArgsConstructor
public class TraceStatus {

	private TraceId traceId;
	private Long startTimeMs;
	private String message;
}
