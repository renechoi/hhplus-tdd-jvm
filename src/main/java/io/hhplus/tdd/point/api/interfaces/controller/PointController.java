package io.hhplus.tdd.point.api.interfaces.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.hhplus.tdd.point.api.application.dto.UserPointChargeRequest;
import io.hhplus.tdd.point.api.application.dto.UserPointChargeResponse;
import io.hhplus.tdd.point.api.application.facade.PointFacade;
import io.hhplus.tdd.point.api.domain.entity.PointHistory;
import io.hhplus.tdd.point.api.domain.entity.UserPoint;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {

    private final PointFacade facade;


    /**
     * 특정 유저의 포인트를 조회하는 기능을 작성 예정
     */
    @GetMapping("{id}")
    public UserPoint point(
            @PathVariable long id
    ) {
        return new UserPoint(0, 0, 0);
    }

    /**
     * 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성 예장
     */
    @GetMapping("{id}/histories")
    public List<PointHistory> history(
            @PathVariable long id
    ) {
        return List.of();
    }

    /**
     * 특정 유저의 포인트를 충전하는 기능을 작성
     */
    @PatchMapping("{id}/charge")
    public ResponseEntity<UserPointChargeResponse> charge(@PathVariable long id, @RequestBody UserPointChargeRequest userPointChargeRequest) {
        return ResponseEntity.ok(facade.charge(userPointChargeRequest.withId(id)));
    }

    /**
     * 특정 유저의 포인트를 사용하는 기능을 작성 예정
     */
    @PatchMapping("{id}/use")
    public UserPoint use(
            @PathVariable long id,
            @RequestBody long amount
    ) {
        return new UserPoint(0, 0, 0);
    }
}
