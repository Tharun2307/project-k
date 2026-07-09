package com.mits.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

import com.mits.entity.AuditLog;
import com.mits.repository.AuditLogRepository;
import com.mits.service.AuditLogService;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void log(String action, String description, String entityName, Long entityId, String username) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setDescription(description);
        auditLog.setEntityName(entityName);
        auditLog.setEntityId(entityId); 
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setUsername(username);
        
        auditLogRepository.save(auditLog);
    }

    // ✅ NEW: Implementations for fetching logs
    @Override
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }

    @Override
    public List<AuditLog> getLogsByEntityName(String entityName) {
        return auditLogRepository.findByEntityName(entityName);
    }

    @Override
    public List<AuditLog> getLogsByAction(String action) {
        return auditLogRepository.findByAction(action);
    }

    @Override
    public List<AuditLog> getLogsByUsername(String username) {
        return auditLogRepository.findByUsername(username);
    }

    @Override
    public List<AuditLog> getLogsByDateRange(LocalDateTime start, LocalDateTime end) {
        return auditLogRepository.findByTimestampBetween(start, end);
    }
}