package com.mits.service;

public interface AuditLogService {
    // ✅ UPDATED: Added entityName and entityId parameters
    void log(String action, String description, String entityName, Long entityId, String username);
}