package br.com.navdata.flowcash.controller;

import br.com.navdata.flowcash.dto.response.DashboardSummaryResponse;
import br.com.navdata.flowcash.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponse> getSummary(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseEntity.ok(dashboardService.getMonthlySummary(userId, year, month));
    }
}
