package br.com.navdata.flowcash.controller;

import br.com.navdata.flowcash.entity.ClosedMonthEntity;
import br.com.navdata.flowcash.service.ClosedMonthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/closed-months")
@RequiredArgsConstructor
public class ClosedMonthController {

    private final ClosedMonthService closedMonthService;

    @GetMapping
    public ResponseEntity<List<ClosedMonthEntity>> list(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(closedMonthService.listByUser(userId));
    }

    @PostMapping("/close")
    public ResponseEntity<Void> close(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        closedMonthService.closeMonth(userId, year, month);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reopen")
    public ResponseEntity<Void> reopen(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        closedMonthService.reopenMonth(userId, year, month);
        return ResponseEntity.ok().build();
    }
}
