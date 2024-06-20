package io.hhplus.tdd.point.api.interfaces.controller;

import static org.springframework.http.ResponseEntity.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.hhplus.tdd.point.api.application.dto.PointHistoriesResponse;
import io.hhplus.tdd.point.api.application.dto.UserPointChargeRequest;
import io.hhplus.tdd.point.api.application.dto.UserPointChargeResponse;
import io.hhplus.tdd.point.api.application.dto.UserPointSearchResponse;
import io.hhplus.tdd.point.api.application.dto.UserPointUseResponse;
import io.hhplus.tdd.point.api.application.dto.UserPointUserRequest;
import io.hhplus.tdd.point.api.application.facade.PointFacade;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {

    private final PointFacade facade;


    /**
     * 특정 유저의 포인트를 조회하는 기능
     */
    @GetMapping("{id}")
    public ResponseEntity<UserPointSearchResponse> point(@PathVariable long id) {
        return ok(facade.search(id));
    }

    /**
     * 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성 예장
     */
    @GetMapping("{id}/histories")
    public ResponseEntity<PointHistoriesResponse> history(@PathVariable long id) {
        return ok(facade.getHistories(id));
    }

    /**
     * 특정 유저의 포인트를 충전하는 기능
     */
    @PatchMapping("{id}/charge")
    public ResponseEntity<UserPointChargeResponse> charge(@PathVariable long id, @RequestBody UserPointChargeRequest userPointChargeRequest) {
        return ok(facade.charge(userPointChargeRequest.withId(id)));
    }

    /**
     * 특정 유저의 포인트를 사용하는 기능을 작성 예정
     */
    @PatchMapping("{id}/use")
    public ResponseEntity<UserPointUseResponse> use(@PathVariable long id, @RequestBody UserPointUserRequest userPointUserRequest) {
        return ok(facade.use(userPointUserRequest.withId(id)));
    }
}
