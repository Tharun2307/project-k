package com.mits.service;

import com.mits.dto.DashboardStatsDTO;
import com.mits.dto.PublicDashboardDTO;

public interface DashboardService {
    
    // ✅ PUBLIC: No audit logs
    PublicDashboardDTO getPublicDashboardStats();
    
    // ✅ ADMIN: Includes audit logs
    DashboardStatsDTO getAdminDashboardStats();
}