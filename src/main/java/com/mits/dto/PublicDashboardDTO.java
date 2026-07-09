package com.mits.dto;

public class PublicDashboardDTO {

    // Total Counts
    private long totalSports;
    private long totalTeams;
    private long totalPlayers;
    private long totalMatches;

    // Match Status Breakdown
    private long upcomingMatches;
    private long liveMatches;
    private long completedMatches;
    private long cancelledMatches;

    public PublicDashboardDTO() {
    }

    // --- Getters and Setters ---
    
    public long getTotalSports() { return totalSports; }
    public void setTotalSports(long totalSports) { this.totalSports = totalSports; }

    public long getTotalTeams() { return totalTeams; }
    public void setTotalTeams(long totalTeams) { this.totalTeams = totalTeams; }

    public long getTotalPlayers() { return totalPlayers; }
    public void setTotalPlayers(long totalPlayers) { this.totalPlayers = totalPlayers; }

    public long getTotalMatches() { return totalMatches; }
    public void setTotalMatches(long totalMatches) { this.totalMatches = totalMatches; }

    public long getUpcomingMatches() { return upcomingMatches; }
    public void setUpcomingMatches(long upcomingMatches) { this.upcomingMatches = upcomingMatches; }

    public long getLiveMatches() { return liveMatches; }
    public void setLiveMatches(long liveMatches) { this.liveMatches = liveMatches; }

    public long getCompletedMatches() { return completedMatches; }
    public void setCompletedMatches(long completedMatches) { this.completedMatches = completedMatches; }

    public long getCancelledMatches() { return cancelledMatches; }
    public void setCancelledMatches(long cancelledMatches) { this.cancelledMatches = cancelledMatches; }
}