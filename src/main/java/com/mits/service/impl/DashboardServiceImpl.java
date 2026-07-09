package com.mits.service.impl;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mits.dto.DashboardStatsDTO;
import com.mits.dto.PublicDashboardDTO;
import com.mits.entity.AuditLog;
import com.mits.enums.MatchStatus;
import com.mits.repository.AuditLogRepository;
import com.mits.repository.MatchRepository;
import com.mits.repository.PlayerRepository;
import com.mits.repository.SportRepository;
import com.mits.repository.TeamRepository;
import com.mits.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final SportRepository sportRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final AuditLogRepository auditLogRepository;

    public DashboardServiceImpl(SportRepository sportRepository,
                                TeamRepository teamRepository,
                                PlayerRepository playerRepository,
                                MatchRepository matchRepository,
                                AuditLogRepository auditLogRepository) {
        this.sportRepository = sportRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        this.auditLogRepository = auditLogRepository;
    }

    // ✅ PUBLIC DASHBOARD (No audit logs)
    @Override
    public PublicDashboardDTO getPublicDashboardStats() {
        PublicDashboardDTO stats = new PublicDashboardDTO();

        stats.setTotalSports(sportRepository.count());
        stats.setTotalTeams(teamRepository.count());
        stats.setTotalPlayers(playerRepository.count());
        stats.setTotalMatches(matchRepository.count());

        stats.setUpcomingMatches(matchRepository.countByStatus(MatchStatus.UPCOMING));
        stats.setLiveMatches(matchRepository.countByStatus(MatchStatus.LIVE));
        stats.setCompletedMatches(matchRepository.countByStatus(MatchStatus.COMPLETED));
        stats.setCancelledMatches(matchRepository.countByStatus(MatchStatus.CANCELLED));

        return stats;
    }

    // ✅ ADMIN DASHBOARD (With audit logs)
    @Override
    public DashboardStatsDTO getAdminDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        stats.setTotalSports(sportRepository.count());
        stats.setTotalTeams(teamRepository.count());
        stats.setTotalPlayers(playerRepository.count());
        stats.setTotalMatches(matchRepository.count());

        stats.setUpcomingMatches(matchRepository.countByStatus(MatchStatus.UPCOMING));
        stats.setLiveMatches(matchRepository.countByStatus(MatchStatus.LIVE));
        stats.setCompletedMatches(matchRepository.countByStatus(MatchStatus.COMPLETED));
        stats.setCancelledMatches(matchRepository.countByStatus(MatchStatus.CANCELLED));

        // ✅ Only admin gets audit logs
        List<AuditLog> recentLogs = auditLogRepository.findAll(
                PageRequest.of(0, 5, Sort.by("timestamp").descending())
        ).getContent();
        
        stats.setRecentActivities(recentLogs);

        return stats;
    }
}