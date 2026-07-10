package com.mits.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mits.entity.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Find all logs by username
    List<AuditLog> findByUsername(String username);

    // Find all logs by action (CREATE, UPDATE, DELETE)
    List<AuditLog> findByAction(String action);

    // Find logs by entity name (Sport, Team, Match, Player)
    List<AuditLog> findByEntityName(String entityName);

    // Find logs for a specific entity ID
    List<AuditLog> findByEntityId(Long entityId);

    // Find logs between two timestamps
    List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    // Find logs by username and action
    List<AuditLog> findByUsernameAndAction(String username, String action);

    // Find logs by entity name and action
    List<AuditLog> findByEntityNameAndAction(String entityName, String action);

}