package com.mits.service;

public interface AuditLogService {

    void log(String action, String username);

}