package com.mits.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mits.dto.DashboardStatsDTO;
import com.mits.dto.PublicDashboardDTO;
import com.mits.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // ✅ PUBLIC: No authentication required
    @GetMapping("/public")
    public ResponseEntity<PublicDashboardDTO> getPublicDashboard() {
        return ResponseEntity.ok(dashboardService.getPublicDashboardStats());
    }

    // ✅ ADMIN: Requires EVENT_COORDINATOR role
    @GetMapping("/admin")
    public ResponseEntity<DashboardStatsDTO> getAdminDashboard() {
        return ResponseEntity.ok(dashboardService.getAdminDashboardStats());
    }
}