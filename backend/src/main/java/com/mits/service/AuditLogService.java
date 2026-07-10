package com.mits.service;

import java.time.LocalDateTime;
import java.util.List;
import com.mits.entity.AuditLog;

public interface AuditLogService {
    
    // Existing method for saving logs
    void log(String action, String description, String entityName, Long entityId, String username);

    // ✅ NEW: Methods for fetching logs via API
    List<AuditLog> getAllLogs();
    List<AuditLog> getLogsByEntityName(String entityName);
    List<AuditLog> getLogsByAction(String action);
    List<AuditLog> getLogsByUsername(String username);
    List<AuditLog> getLogsByDateRange(LocalDateTime start, LocalDateTime end);
}