package com.mits.service.impl;

import java.time.LocalDateTime;
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
        
        // ✅ FIX: Now setting the entityId so it is not null!
        auditLog.setEntityId(entityId); 
        
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setUsername(username);
        
        auditLogRepository.save(auditLog);
    }
}