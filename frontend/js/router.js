// Sportex Client-side Hash Router
import { fetchWithAuth } from './api.js';
import { formatDate, formatTime, initLucideIcons, showToast } from './utils.js';
import { connectWebSocket, disconnectWebSocket } from './websocket.js';

// Central mount point
const appContent = document.getElementById('app-content');

export function parseRoute() {
    const hash = window.location.hash || '#home';
    const parts = hash.slice(1).split('/'); // Remove '#'
    return {
        view: parts[0] || 'home',
        id: parts[1] || null,
        tab: parts[2] || null
    };
}

/**
 * Main route dispatcher.
 */
export async function handleRoute() {
    // Unsubscribe from WebSocket if navigating away from match details
    disconnectWebSocket();
    
    const route = parseRoute();
    
    // Manage active navigation classes in the navbar
    updateNavLinks(route.view);

    appContent.innerHTML = `
        <div class="flex justify-center items-center py-20">
            <div class="w-10 h-10 border-4 border-emerald-500 border-t-transparent rounded-full animate-spin"></div>
        </div>
    `;

    try {
        switch (route.view) {
            case 'home':
                await renderHome();
                break;
            case 'matches':
                await renderMatches();
                break;
            case 'match':
                if (route.id) {
                    await renderMatchDetail(route.id, route.tab || 'live');
                } else {
                    window.location.hash = '#home';
                }
                break;
            default:
                appContent.innerHTML = `<div class="text-center py-20 text-slate-400">View "${route.view}" not found</div>`;
        }
    } catch (err) {
        appContent.innerHTML = `
            <div class="text-center py-20">
                <i data-lucide="alert-triangle" class="w-12 h-12 text-rose-500 mx-auto mb-4"></i>
                <h3 class="text-lg font-semibold text-slate-200">Failed to load content</h3>
                <p class="text-slate-400 text-sm mt-1">${err.message}</p>
                <button onclick="window.location.reload()" class="mt-4 px-4 py-2 bg-emerald-600 text-white rounded-lg hover:bg-emerald-500 text-sm font-semibold transition-colors">Retry</button>
            </div>
        `;
        initLucideIcons();
    }
}

/**
 * Toggles navigation link highlights.
 */
function updateNavLinks(activeView) {
    document.querySelectorAll('[data-nav]').forEach(el => {
        const view = el.getAttribute('data-nav');
        if (view === activeView) {
            el.classList.add('text-emerald-400', 'border-emerald-400');
            el.classList.remove('text-slate-400', 'border-transparent');
        } else {
            el.classList.add('text-slate-400', 'border-transparent');
            el.classList.remove('text-emerald-400', 'border-emerald-400');
        }
    });
}

// ----------------------------------------------------
// VIEW RENDERING LOGIC
// ----------------------------------------------------

async function renderHome() {
    // Fetch all matches to build ticker and lists
    const matches = await fetchWithAuth('/api/matches');
    
    const liveMatches = matches.filter(m => m.status === 'LIVE');
    const upcomingMatches = matches.filter(m => m.status === 'UPCOMING');
    const completedMatches = matches.filter(m => m.status === 'COMPLETED');

    // 1. Render Ticker
    let tickerHtml = '';
    if (liveMatches.length === 0) {
        tickerHtml = `
            <div class="flex items-center justify-center py-6 w-full text-slate-400 text-sm gap-2">
                <span class="w-2 h-2 rounded-full bg-slate-600"></span>
                No matches currently live. Check upcoming schedule below.
            </div>
        `;
    } else {
        tickerHtml = liveMatches.map(m => `
            <a href="#match/${m.id}" class="flex-shrink-0 w-80 p-4 rounded-2xl glass-panel border border-slate-800 hover:border-slate-700 transition-all flex flex-col justify-between select-none">
                <div class="flex justify-between items-center text-xs">
                    <span class="text-emerald-400 font-semibold uppercase tracking-wider text-[10px]">${m.sport.sportName}</span>
                    <span class="flex items-center gap-1.5 px-2 py-0.5 rounded-full bg-rose-500/10 text-rose-400 border border-rose-500/20 font-bold text-[9px] uppercase">
                        <span class="w-1.5 h-1.5 rounded-full bg-rose-500 animate-pulse"></span> Live
                    </span>
                </div>
                <div class="mt-4 flex flex-col gap-2">
                    <div class="flex justify-between items-center">
                        <span class="font-medium text-slate-200 text-sm">${m.team1.teamName}</span>
                        <span class="text-xs text-slate-400">${m.team1.shortName || m.team1.teamName.substring(0,3).toUpperCase()}</span>
                    </div>
                    <div class="flex justify-between items-center">
                        <span class="font-medium text-slate-200 text-sm">${m.team2.teamName}</span>
                        <span class="text-xs text-slate-400">${m.team2.shortName || m.team2.teamName.substring(0,3).toUpperCase()}</span>
                    </div>
                </div>
                <div class="mt-4 pt-3 border-t border-slate-800/60 flex justify-between items-center text-[10px] text-slate-400 font-medium">
                    <span>${formatDate(m.matchDate)}</span>
                    <span class="text-emerald-400 flex items-center gap-0.5">View Score <i data-lucide="chevron-right" class="w-3.5 h-3.5"></i></span>
                </div>
            </a>
        `).join('');
    }

    // 2. Render Categories
    const categories = [
        { name: 'Cricket', icon: 'dribbble', color: 'text-emerald-400 bg-emerald-500/10 border-emerald-500/20' },
        { name: 'Volleyball', icon: 'volleyball', color: 'text-sky-400 bg-sky-500/10 border-sky-500/20' },
        { name: 'Kabaddi', icon: 'zap', color: 'text-amber-400 bg-amber-500/10 border-amber-500/20' },
        { name: 'Badminton', icon: 'award', color: 'text-indigo-400 bg-indigo-500/10 border-indigo-500/20' }
    ];

    const categoryHtml = categories.map(c => `
        <div class="flex items-center gap-4 p-4 rounded-2xl glass-panel border border-slate-800 transition-card cursor-pointer" onclick="window.location.hash = '#matches?sport=${c.name.toLowerCase()}'">
            <div class="p-3 rounded-xl ${c.color.split(' ')[1]} ${c.color.split(' ')[2]}">
                <i data-lucide="${c.icon}" class="${c.color.split(' ')[0]} w-6 h-6"></i>
            </div>
            <div>
                <h4 class="font-semibold text-slate-200 text-sm">${c.name}</h4>
                <p class="text-slate-400 text-xs mt-0.5">Explore tournaments</p>
            </div>
        </div>
    `).join('');

    // 3. Render Upcoming list
    let upcomingHtml = '';
    if (upcomingMatches.length === 0) {
        upcomingHtml = '<div class="text-slate-500 text-sm py-4">No upcoming matches scheduled.</div>';
    } else {
        upcomingHtml = upcomingMatches.slice(0, 3).map(m => `
            <div class="flex items-center justify-between p-4 rounded-xl border border-slate-900 bg-slate-900/30 hover:border-slate-800 transition-all">
                <div class="flex items-center gap-3">
                    <span class="px-2 py-0.5 text-[9px] font-semibold bg-slate-800 text-slate-300 rounded border border-slate-700 uppercase">${m.sport.sportName}</span>
                    <span class="text-slate-200 font-semibold text-sm">${m.team1.shortName || m.team1.teamName.substring(0,3).toUpperCase()} vs ${m.team2.shortName || m.team2.teamName.substring(0,3).toUpperCase()}</span>
                </div>
                <div class="text-right">
                    <div class="text-xs text-slate-300 font-medium">${formatDate(m.matchDate)}</div>
                    <div class="text-[10px] text-slate-400 mt-0.5">${formatTime(m.matchDate)}</div>
                </div>
            </div>
        `).join('');
    }

    // 4. Render Recent list
    let completedHtml = '';
    if (completedMatches.length === 0) {
        completedHtml = '<div class="text-slate-500 text-sm py-4">No match results available.</div>';
    } else {
        completedHtml = completedMatches.slice(0, 3).map(m => `
            <div class="flex items-center justify-between p-4 rounded-xl border border-slate-900 bg-slate-900/30 hover:border-slate-800 transition-all">
                <div class="flex flex-col gap-1">
                    <span class="px-2 py-0.5 w-max text-[9px] font-semibold bg-emerald-500/10 text-emerald-400 rounded border border-emerald-500/20 uppercase">${m.sport.sportName}</span>
                    <span class="text-slate-200 font-semibold text-sm mt-1">${m.team1.shortName} vs ${m.team2.shortName}</span>
                </div>
                <a href="#match/${m.id}" class="px-3.5 py-1.5 bg-emerald-950/40 text-emerald-400 border border-emerald-500/20 hover:bg-emerald-500 hover:text-white rounded-lg text-xs font-semibold transition-all">View Result</a>
            </div>
        `).join('');
    }

    appContent.innerHTML = `
        <div class="fade-in max-w-6xl mx-auto px-4 py-8">
            <!-- Hero Header -->
            <div class="flex flex-col md:flex-row md:items-center justify-between gap-6 mb-10">
                <div>
                    <h1 class="text-3xl font-extrabold text-white tracking-tight">Real-Time Match Action</h1>
                    <p class="text-slate-400 text-sm mt-1.5">Track tournaments, detailed stats, scorecards, and expert commentaries</p>
                </div>
                <div class="flex items-center gap-3">
                    <a href="#matches" class="px-4 py-2 bg-emerald-600 text-white rounded-xl hover:bg-emerald-500 text-sm font-semibold transition-colors flex items-center gap-2 shadow-lg shadow-emerald-900/20">
                        <i data-lucide="play" class="w-4 h-4"></i> Browse Matches
                    </a>
                </div>
            </div>

            <!-- Ticker Section -->
            <div class="mb-10">
                <h3 class="text-xs font-bold text-slate-400 uppercase tracking-wider mb-4 flex items-center gap-2">
                    <span class="w-1.5 h-1.5 rounded-full bg-rose-500 animate-pulse"></span> Live Action
                </h3>
                <div class="flex gap-4 overflow-x-auto pb-4 no-scrollbar scroll-smooth snap-x snap-mandatory">
                    ${tickerHtml}
                </div>
            </div>

            <!-- Grid Layout -->
            <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
                <!-- Categories & Tournaments -->
                <div class="lg:col-span-2 space-y-8">
                    <div>
                        <h3 class="text-xs font-bold text-slate-400 uppercase tracking-wider mb-4">Sports Coverage</h3>
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            ${categoryHtml}
                        </div>
                    </div>
                </div>

                <!-- Side Panels -->
                <div class="space-y-8">
                    <!-- Upcoming schedule -->
                    <div class="p-6 rounded-2xl glass-panel border border-slate-800">
                        <h3 class="text-sm font-semibold text-slate-200 mb-4 flex items-center gap-2">
                            <i data-lucide="calendar" class="w-4 h-4 text-emerald-400"></i> Upcoming Fixtures
                        </h3>
                        <div class="flex flex-col gap-3">
                            ${upcomingHtml}
                        </div>
                    </div>

                    <!-- Recent Results -->
                    <div class="p-6 rounded-2xl glass-panel border border-slate-800">
                        <h3 class="text-sm font-semibold text-slate-200 mb-4 flex items-center gap-2">
                            <i data-lucide="check-circle" class="w-4 h-4 text-emerald-400"></i> Recent Results
                        </h3>
                        <div class="flex flex-col gap-3">
                            ${completedHtml}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;

    initLucideIcons();
}

async function renderMatches() {
    // Extract query params for sport filtering
    const hash = window.location.hash;
    const urlParams = new URLSearchParams(hash.includes('?') ? hash.split('?')[1] : '');
    const sportFilter = urlParams.get('sport');

    const matches = await fetchWithAuth('/api/matches');
    
    // DOM Filter State
    let activeTab = 'all';

    function getFilteredMatches() {
        let filtered = matches;
        if (sportFilter) {
            filtered = filtered.filter(m => m.sport.sportName.toLowerCase() === sportFilter.toLowerCase());
        }
        if (activeTab === 'live') {
            return filtered.filter(m => m.status === 'LIVE');
        } else if (activeTab === 'upcoming') {
            return filtered.filter(m => m.status === 'UPCOMING');
        } else if (activeTab === 'completed') {
            return filtered.filter(m => m.status === 'COMPLETED');
        }
        return filtered;
    }

    function renderList() {
        const filtered = getFilteredMatches();
        const listContainer = document.getElementById('matches-grid-container');
        if (!listContainer) return;

        if (filtered.length === 0) {
            listContainer.innerHTML = `
                <div class="col-span-full text-center py-20 text-slate-500">
                    <i data-lucide="search" class="w-12 h-12 mx-auto text-slate-600 mb-3"></i>
                    No matches found matching the criteria.
                </div>
            `;
            initLucideIcons();
            return;
        }

        listContainer.innerHTML = filtered.map(m => `
            <a href="#match/${m.id}" class="p-6 rounded-2xl glass-panel border border-slate-800 transition-card flex flex-col justify-between">
                <div class="flex justify-between items-center text-xs">
                    <span class="px-2.5 py-0.5 text-[10px] font-semibold bg-slate-800 text-slate-300 rounded-full border border-slate-700 uppercase tracking-wide">${m.sport.sportName}</span>
                    ${m.status === 'LIVE' ? `
                        <span class="flex items-center gap-1.5 px-2 py-0.5 rounded-full bg-rose-500/10 text-rose-400 border border-rose-500/20 font-bold text-[9px] uppercase">
                            <span class="w-1.5 h-1.5 rounded-full bg-rose-500 animate-pulse"></span> Live
                        </span>
                    ` : m.status === 'COMPLETED' ? `
                        <span class="px-2 py-0.5 text-[9px] font-semibold bg-emerald-500/10 text-emerald-400 rounded-full border border-emerald-500/20 uppercase">Ended</span>
                    ` : `
                        <span class="px-2 py-0.5 text-[9px] font-semibold bg-sky-500/10 text-sky-400 rounded-full border border-sky-500/20 uppercase">Scheduled</span>
                    `}
                </div>
                <div class="my-6">
                    <div class="flex justify-between items-center mb-3">
                        <span class="font-bold text-slate-200 text-sm">${m.team1.teamName}</span>
                        <span class="text-xs text-slate-400">${m.team1.shortName || m.team1.teamName.substring(0,3).toUpperCase()}</span>
                    </div>
                    <div class="flex justify-between items-center">
                        <span class="font-bold text-slate-200 text-sm">${m.team2.teamName}</span>
                        <span class="text-xs text-slate-400">${m.team2.shortName || m.team2.teamName.substring(0,3).toUpperCase()}</span>
                    </div>
                </div>
                <div class="pt-4 border-t border-slate-800/80 flex items-center justify-between text-xs text-slate-400">
                    <div class="flex items-center gap-1.5">
                        <i data-lucide="calendar" class="w-3.5 h-3.5 text-slate-500"></i>
                        <span>${formatDate(m.matchDate)}</span>
                    </div>
                    <span class="text-emerald-400 font-semibold flex items-center gap-0.5">Details <i data-lucide="chevron-right" class="w-3.5 h-3.5"></i></span>
                </div>
            </a>
        `).join('');
        initLucideIcons();
    }

    appContent.innerHTML = `
        <div class="fade-in max-w-6xl mx-auto px-4 py-8">
            <div class="flex flex-col md:flex-row md:items-center justify-between gap-6 mb-8">
                <div>
                    <h1 class="text-2xl font-bold text-white tracking-tight">
                        ${sportFilter ? `${sportFilter.charAt(0).toUpperCase() + sportFilter.slice(1)} Matches` : 'All Fixtures'}
                    </h1>
                    <p class="text-slate-400 text-xs mt-1">Browse current tournaments and live results</p>
                </div>
                
                <!-- Tab Filters -->
                <div class="flex bg-slate-900 border border-slate-800/80 p-1.5 rounded-xl text-xs font-semibold max-w-sm w-full md:w-auto">
                    <button data-tab-filter="all" class="flex-1 md:flex-none px-4 py-2 text-slate-300 rounded-lg bg-emerald-600 text-white transition-colors">All</button>
                    <button data-tab-filter="live" class="flex-1 md:flex-none px-4 py-2 text-slate-400 rounded-lg hover:text-slate-200 transition-colors">Live</button>
                    <button data-tab-filter="upcoming" class="flex-1 md:flex-none px-4 py-2 text-slate-400 rounded-lg hover:text-slate-200 transition-colors">Upcoming</button>
                    <button data-tab-filter="completed" class="flex-1 md:flex-none px-4 py-2 text-slate-400 rounded-lg hover:text-slate-200 transition-colors">Completed</button>
                </div>
            </div>

            <!-- Matches List Grid -->
            <div id="matches-grid-container" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                <!-- Matches injected here -->
            </div>
        </div>
    `;

    // Hook tab buttons
    document.querySelectorAll('[data-tab-filter]').forEach(btn => {
        btn.addEventListener('click', (e) => {
            document.querySelectorAll('[data-tab-filter]').forEach(b => {
                b.classList.remove('bg-emerald-600', 'text-white');
                b.classList.add('text-slate-400');
            });
            e.currentTarget.classList.add('bg-emerald-600', 'text-white');
            e.currentTarget.classList.remove('text-slate-400');

            activeTab = e.currentTarget.getAttribute('data-tab-filter');
            renderList();
        });
    });

    renderList();
    initLucideIcons();
}

async function renderMatchDetail(matchId, activeTab) {
    const match = await fetchWithAuth(`/api/matches/${matchId}`);
    const sportName = match.sport.sportName.toUpperCase();

    // 1. WebSocket live updates subscription
    connectWebSocket(matchId, (updatedScore) => {
        console.log("⚡ Received live WebSocket score update:", updatedScore);
        // Highlight updated element briefly
        const scoreElem = document.getElementById('live-score-display-panel');
        if (scoreElem) {
            scoreElem.classList.add('flash-update');
            setTimeout(() => scoreElem.classList.remove('flash-update'), 800);
        }
        
        // Re-render score details
        updateLiveScoreDOM(updatedScore, sportName, match);
    });

    // 2. Fetch initial score
    let score = null;
    try {
        score = await fetchWithAuth(`/api/matches/${matchId}/score`);
    } catch (e) {
        console.warn("Could not retrieve active scorecard:", e);
    }

    // 3. Render Detail layout structure
    appContent.innerHTML = `
        <div class="fade-in max-w-4xl mx-auto px-4 py-8">
            <!-- Navigation back -->
            <div class="mb-6">
                <a href="#matches" class="flex items-center gap-1.5 text-xs text-slate-400 hover:text-emerald-400 font-semibold transition-colors">
                    <i data-lucide="arrow-left" class="w-4 h-4"></i> Back to Matches
                </a>
            </div>

            <!-- Match Header Panel -->
            <div class="p-6 rounded-2xl glass-panel border border-slate-800 mb-8 bg-mesh-sport">
                <div class="flex justify-between items-center mb-6">
                    <div id="ws-status-indicator" class="flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-semibold bg-slate-800/80 text-slate-400 border border-slate-700/60">
                        <span class="w-1.5 h-1.5 rounded-full bg-slate-500"></span> Disconnected
                    </div>
                    ${match.status === 'LIVE' ? `
                        <span class="flex items-center gap-1.5 px-3 py-1 rounded-full bg-rose-500/10 text-rose-400 border border-rose-500/20 font-bold text-xs uppercase live-indicator">
                            <span class="w-1.5 h-1.5 rounded-full bg-rose-500"></span> Live
                        </span>
                    ` : match.status === 'COMPLETED' ? `
                        <span class="px-3 py-1 rounded-full text-xs font-semibold bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 uppercase font-bold">Completed</span>
                    ` : `
                        <span class="px-3 py-1 rounded-full text-xs font-semibold bg-sky-500/10 text-sky-400 border border-sky-500/20 uppercase font-bold">Upcoming</span>
                    `}
                </div>

                <div class="flex items-center justify-between text-center max-w-xl mx-auto my-4">
                    <div class="flex-1 flex flex-col items-center">
                        <div class="w-16 h-16 rounded-2xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center mb-3">
                            <span class="font-extrabold text-xl text-emerald-400">${match.team1.shortName || match.team1.teamName.substring(0,3).toUpperCase()}</span>
                        </div>
                        <h3 class="font-bold text-slate-100 text-sm md:text-base">${match.team1.teamName}</h3>
                    </div>
                    <div class="flex-shrink-0 px-6 font-black text-slate-600 text-lg">VS</div>
                    <div class="flex-1 flex flex-col items-center">
                        <div class="w-16 h-16 rounded-2xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center mb-3">
                            <span class="font-extrabold text-xl text-emerald-400">${match.team2.shortName || match.team2.teamName.substring(0,3).toUpperCase()}</span>
                        </div>
                        <h3 class="font-bold text-slate-100 text-sm md:text-base">${match.team2.teamName}</h3>
                    </div>
                </div>

                <!-- Display Score Box -->
                <div id="live-score-display-panel" class="text-center mt-8 py-4 border-t border-slate-800/80">
                    <!-- Loaded dynamically -->
                </div>
            </div>

            <!-- Tab Navigation -->
            <div class="flex border-b border-slate-800/80 mb-6 font-semibold text-sm">
                <button data-detail-tab="live" class="px-5 py-3 border-b-2 ${activeTab === 'live' ? 'border-emerald-500 text-emerald-400' : 'border-transparent text-slate-400'} hover:text-slate-200">Live & Updates</button>
                <button data-detail-tab="scorecard" class="px-5 py-3 border-b-2 ${activeTab === 'scorecard' ? 'border-emerald-500 text-emerald-400' : 'border-transparent text-slate-400'} hover:text-slate-200">Scorecard</button>
                <button data-detail-tab="commentary" class="px-5 py-3 border-b-2 ${activeTab === 'commentary' ? 'border-emerald-500 text-emerald-400' : 'border-transparent text-slate-400'} hover:text-slate-200">Commentary</button>
            </div>

            <!-- Tab Content Mount -->
            <div id="detail-tab-content" class="space-y-6">
                <!-- Mounted dynamically -->
            </div>
        </div>
    `;

    // Setup active state handlers
    document.querySelectorAll('[data-detail-tab]').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const selected = e.currentTarget.getAttribute('data-detail-tab');
            window.location.hash = `#match/${matchId}/${selected}`;
        });
    });

    // Populate data
    updateLiveScoreDOM(score, sportName, match);
    await loadTabContent(activeTab, matchId, sportName, score, match);
    initLucideIcons();
}

/**
 * Dynamically switches active content inside the match details tabs.
 */
async function loadTabContent(tab, matchId, sportName, score, match) {
    const tabContent = document.getElementById('detail-tab-content');
    if (!tabContent) return;

    if (tab === 'live') {
        // Live Highlights / Summary
        tabContent.innerHTML = `
            <div class="p-6 rounded-2xl glass-panel border border-slate-800">
                <h4 class="text-sm font-semibold text-slate-200 mb-4">Match Overview</h4>
                <div class="grid grid-cols-2 gap-4 text-xs">
                    <div class="p-3 bg-slate-900/50 rounded-xl">
                        <span class="text-slate-500 block mb-1">Sport</span>
                        <span class="text-slate-300 font-semibold">${match.sport.sportName}</span>
                    </div>
                    <div class="p-3 bg-slate-900/50 rounded-xl">
                        <span class="text-slate-500 block mb-1">Status</span>
                        <span class="text-slate-300 font-semibold">${match.status}</span>
                    </div>
                    <div class="p-3 bg-slate-900/50 rounded-xl">
                        <span class="text-slate-500 block mb-1">Date</span>
                        <span class="text-slate-300 font-semibold">${formatDate(match.matchDate)}</span>
                    </div>
                    <div class="p-3 bg-slate-900/50 rounded-xl">
                        <span class="text-slate-500 block mb-1">Time</span>
                        <span class="text-slate-300 font-semibold">${formatTime(match.matchDate)}</span>
                    </div>
                </div>
            </div>
        `;
    } 
    else if (tab === 'scorecard') {
        // Detailed scorecard view based on sport
        tabContent.innerHTML = `<div class="text-center py-10 text-slate-500">Loading scorecard...</div>`;
        
        if (!score) {
            tabContent.innerHTML = `<div class="text-center py-10 text-slate-500">No active scorecard yet.</div>`;
            return;
        }

        if (sportName === 'CRICKET') {
            const currentBatsmen = score.currentBatsmen || [];
            const striker = currentBatsmen[0] || { playerName: 'Striker', runs: 0, balls: 0, fours: 0, sixes: 0, strikeRate: 0.0 };
            const nonStriker = currentBatsmen[1] || { playerName: 'Non-Striker', runs: 0, balls: 0, fours: 0, sixes: 0, strikeRate: 0.0 };
            const bowler = score.currentBowler || { playerName: 'Bowler', overs: 0, balls: 0, runsGiven: 0, wickets: 0, economy: 0.0 };

            tabContent.innerHTML = `
                <div class="space-y-6">
                    <div class="p-6 rounded-2xl glass-panel border border-slate-800">
                        <h4 class="text-sm font-bold text-slate-200 mb-4">Batting Performance</h4>
                        <div class="overflow-x-auto">
                            <table class="w-full text-left text-xs border-collapse">
                                <thead>
                                    <tr class="border-b border-slate-800 text-slate-500 font-bold">
                                        <th class="pb-3">Batsman</th>
                                        <th class="pb-3 text-right">Runs</th>
                                        <th class="pb-3 text-right">Balls</th>
                                        <th class="pb-3 text-right">4s</th>
                                        <th class="pb-3 text-right">6s</th>
                                        <th class="pb-3 text-right">SR</th>
                                    </tr>
                                </thead>
                                <tbody class="text-slate-300 divide-y divide-slate-900">
                                    <tr class="hover:bg-slate-900/20">
                                        <td class="py-3 font-medium text-slate-200">${striker.playerName} *</td>
                                        <td class="py-3 text-right font-bold text-emerald-400">${striker.runs || 0}</td>
                                        <td class="py-3 text-right">${striker.balls || 0}</td>
                                        <td class="py-3 text-right">${striker.fours || 0}</td>
                                        <td class="py-3 text-right">${striker.sixes || 0}</td>
                                        <td class="py-3 text-right text-slate-400">${striker.balls > 0 ? ((striker.runs / striker.balls) * 100).toFixed(1) : '0.0'}</td>
                                    </tr>
                                    <tr class="hover:bg-slate-900/20">
                                        <td class="py-3 font-medium text-slate-200">${nonStriker.playerName}</td>
                                        <td class="py-3 text-right font-bold text-slate-300">${nonStriker.runs || 0}</td>
                                        <td class="py-3 text-right">${nonStriker.balls || 0}</td>
                                        <td class="py-3 text-right">${nonStriker.fours || 0}</td>
                                        <td class="py-3 text-right">${nonStriker.sixes || 0}</td>
                                        <td class="py-3 text-right text-slate-400">${nonStriker.balls > 0 ? ((nonStriker.runs / nonStriker.balls) * 100).toFixed(1) : '0.0'}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="p-6 rounded-2xl glass-panel border border-slate-800">
                        <h4 class="text-sm font-bold text-slate-200 mb-4">Bowling Performance</h4>
                        <div class="overflow-x-auto">
                            <table class="w-full text-left text-xs border-collapse">
                                <thead>
                                    <tr class="border-b border-slate-800 text-slate-500 font-bold">
                                        <th class="pb-3">Bowler</th>
                                        <th class="pb-3 text-right">Overs</th>
                                        <th class="pb-3 text-right">Runs</th>
                                        <th class="pb-3 text-right">Wickets</th>
                                        <th class="pb-3 text-right">Econ</th>
                                    </tr>
                                </thead>
                                <tbody class="text-slate-300 divide-y divide-slate-900">
                                    <tr class="hover:bg-slate-900/20">
                                        <td class="py-3 font-medium text-slate-200">${bowler.playerName}</td>
                                        <td class="py-3 text-right">${bowler.overs || 0}.${bowler.balls || 0}</td>
                                        <td class="py-3 text-right">${bowler.runsGiven || 0}</td>
                                        <td class="py-3 text-right font-bold text-emerald-400">${bowler.wickets || 0}</td>
                                        <td class="py-3 text-right text-slate-400">${bowler.economy || '0.00'}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            `;
        } 
        else if (sportName === 'VOLLEYBALL') {
            tabContent.innerHTML = `
                <div class="p-6 rounded-2xl glass-panel border border-slate-800">
                    <h4 class="text-sm font-bold text-slate-200 mb-4">Set Comparison (Best of 3)</h4>
                    <div class="grid grid-cols-3 text-center gap-4 text-xs font-semibold border-b border-slate-800 pb-3 mb-3 text-slate-500">
                        <div>Team</div>
                        <div>Sets Won</div>
                        <div>Current Set Points</div>
                    </div>
                    <div class="grid grid-cols-3 text-center gap-4 text-sm font-bold py-2.5 text-slate-200">
                        <div>${match.team1.shortName || match.team1.teamName.substring(0,3).toUpperCase()}</div>
                        <div class="text-emerald-400">${score.team1SetsWon || 0}</div>
                        <div>${score.team1Points || 0}</div>
                    </div>
                    <div class="grid grid-cols-3 text-center gap-4 text-sm font-bold py-2.5 text-slate-200 border-t border-slate-900">
                        <div>${match.team2.shortName || match.team2.teamName.substring(0,3).toUpperCase()}</div>
                        <div class="text-emerald-400">${score.team2SetsWon || 0}</div>
                        <div>${score.team2Points || 0}</div>
                    </div>
                </div>
            `;
        }
        else if (sportName === 'KABADDI') {
            const halfText = score.currentHalf === 2 ? 'Second Half' : 'First Half';
            tabContent.innerHTML = `
                <div class="p-6 rounded-2xl glass-panel border border-slate-800 space-y-6">
                    <div class="flex justify-between items-center border-b border-slate-800 pb-3">
                        <h4 class="text-sm font-bold text-slate-200">Kabaddi Stats Summary</h4>
                        <span class="text-xs bg-slate-800 text-slate-300 px-3 py-1 rounded-full font-bold uppercase tracking-wider">${halfText}</span>
                    </div>
                    <div class="grid grid-cols-3 text-center gap-4 text-xs font-semibold text-slate-500 mb-2">
                        <div>Stat</div>
                        <div>${match.team1.shortName || match.team1.teamName.substring(0,3).toUpperCase()}</div>
                        <div>${match.team2.shortName || match.team2.teamName.substring(0,3).toUpperCase()}</div>
                    </div>
                    <div class="grid grid-cols-3 text-center gap-4 text-sm font-bold py-2 text-slate-200 border-t border-slate-900/60">
                        <div class="text-slate-500 font-medium text-xs">Total Points</div>
                        <div class="text-emerald-400">${score.team1Points || 0}</div>
                        <div class="text-emerald-400">${score.team2Points || 0}</div>
                    </div>
                    <div class="grid grid-cols-3 text-center gap-4 text-sm font-bold py-2 text-slate-200 border-t border-slate-900/60">
                        <div class="text-slate-500 font-medium text-xs">Tackle Points</div>
                        <div>${score.team1TacklePoints || 0}</div>
                        <div>${score.team2TacklePoints || 0}</div>
                    </div>
                    <div class="grid grid-cols-3 text-center gap-4 text-sm font-bold py-2 text-slate-200 border-t border-slate-900/60">
                        <div class="text-slate-500 font-medium text-xs">Bonus Points</div>
                        <div>${score.team1BonusPoints || 0}</div>
                        <div>${score.team2BonusPoints || 0}</div>
                    </div>
                    <div class="grid grid-cols-3 text-center gap-4 text-sm font-bold py-2 text-slate-200 border-t border-slate-900/60">
                        <div class="text-slate-500 font-medium text-xs">Super Tackles</div>
                        <div>${score.team1SuperTackles || 0}</div>
                        <div>${score.team2SuperTackles || 0}</div>
                    </div>
                    <div class="grid grid-cols-3 text-center gap-4 text-sm font-bold py-2 text-slate-200 border-t border-slate-900/60">
                        <div class="text-slate-500 font-medium text-xs">All Outs</div>
                        <div>${score.team1AllOuts || 0}</div>
                        <div>${score.team2AllOuts || 0}</div>
                    </div>
                </div>
            `;
        }
        else if (sportName === 'BADMINTON') {
            tabContent.innerHTML = `
                <div class="p-6 rounded-2xl glass-panel border border-slate-800">
                    <h4 class="text-sm font-bold text-slate-200 mb-4">Set Comparison (Best of 3)</h4>
                    <div class="grid grid-cols-3 text-center gap-4 text-xs font-semibold border-b border-slate-800 pb-3 mb-3 text-slate-500">
                        <div>Player / Team</div>
                        <div>Sets Won</div>
                        <div>Current Points</div>
                    </div>
                    <div class="grid grid-cols-3 text-center gap-4 text-sm font-bold py-2.5 text-slate-200">
                        <div>${match.team1.shortName || match.team1.teamName.substring(0,3).toUpperCase()}</div>
                        <div class="text-emerald-400">${score.player1SetsWon || 0}</div>
                        <div>${score.player1Points || 0}</div>
                    </div>
                    <div class="grid grid-cols-3 text-center gap-4 text-sm font-bold py-2.5 text-slate-200 border-t border-slate-900">
                        <div>${match.team2.shortName || match.team2.teamName.substring(0,3).toUpperCase()}</div>
                        <div class="text-emerald-400">${score.player2SetsWon || 0}</div>
                        <div>${score.player2Points || 0}</div>
                    </div>
                </div>
            `;
        }
    } 
    else if (tab === 'commentary') {
        // Load commentary events
        tabContent.innerHTML = `
            <div class="p-6 rounded-2xl glass-panel border border-slate-800">
                <h4 class="text-sm font-bold text-slate-200 mb-6 flex items-center gap-2">
                    <i data-lucide="message-square" class="w-4 h-4 text-emerald-400"></i> Ball-by-Ball Live Commentary
                </h4>
                <div id="commentary-feed-container" class="space-y-4 max-h-[500px] overflow-y-auto pr-2">
                    <div class="text-center py-6 text-slate-500 text-xs">Fetching commentary feeds...</div>
                </div>
            </div>
        `;
        
        try {
            const events = await fetchWithAuth(`/api/matches/${matchId}/recent-events`);
            const feedContainer = document.getElementById('commentary-feed-container');
            if (events.length === 0) {
                feedContainer.innerHTML = `<div class="text-slate-500 text-center py-6 text-xs">No commentary events logged for this match yet.</div>`;
            } else {
                feedContainer.innerHTML = events.map(e => `
                    <div class="slide-in-commentary p-4 rounded-xl border border-slate-900 bg-slate-950/30 flex gap-4 items-start">
                        <div class="w-9 h-9 rounded-lg bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 text-xs font-bold flex items-center justify-center flex-shrink-0">
                            ${e.eventType}
                        </div>
                        <div class="flex-1">
                            <div class="text-slate-200 text-xs font-semibold">${e.team ? (e.team.shortName || e.team.teamName || 'Team') : 'General'} • <span class="text-slate-500 font-medium">${new Date(e.eventTime).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit', second:'2-digit'})}</span></div>
                            <p class="text-slate-400 text-xs mt-1.5 leading-relaxed">${e.description || 'No description provided.'}</p>
                        </div>
                    </div>
                `).join('');
            }
        } catch (e) {
            console.error(e);
        }
        initLucideIcons();
    }
}

/**
 * Perform target DOM elements updates for real-time notifications.
 */
function updateLiveScoreDOM(score, sportName, match) {
    const scorePanel = document.getElementById('live-score-display-panel');
    if (!scorePanel) return;

    if (!score) {
        scorePanel.innerHTML = `<span class="text-slate-500 text-xs font-medium">Waiting to initialize score database...</span>`;
        return;
    }

    if (sportName === 'CRICKET') {
        const t1Score = `${score.team1Runs || 0}/${score.team1Wickets || 0}`;
        const t2Score = `${score.team2Runs || 0}/${score.team2Wickets || 0}`;
        
        let overs = '0.0';
        if (score.battingTeam === 1) {
            overs = `${score.team1Overs || 0}.${score.team1Balls || 0}`;
        } else {
            overs = `${score.team2Overs || 0}.${score.team2Balls || 0}`;
        }

        scorePanel.innerHTML = `
            <div class="flex justify-between items-center px-8">
                <div class="text-left">
                    <span class="text-slate-400 text-[10px] block font-semibold uppercase tracking-wide">${match.team1.shortName || match.team1.teamName.substring(0,3).toUpperCase()} Score</span>
                    <span class="text-xl font-bold text-white">${t1Score}</span>
                </div>
                <div class="text-center bg-slate-900/60 border border-slate-800 px-4 py-2 rounded-xl">
                    <span class="text-slate-500 text-[10px] block font-semibold uppercase">Overs</span>
                    <span class="text-base font-bold text-emerald-400">${overs}</span>
                </div>
                <div class="text-right">
                    <span class="text-slate-400 text-[10px] block font-semibold uppercase tracking-wide">${match.team2.shortName || match.team2.teamName.substring(0,3).toUpperCase()} Score</span>
                    <span class="text-xl font-bold text-white">${t2Score}</span>
                </div>
            </div>
            <div class="mt-4 text-xs font-bold text-emerald-400 text-center animate-pulse">
                ${score.target ? `Target: ${score.target} runs` : 'First Inning in Progress'}
            </div>
        `;
    } 
    else if (sportName === 'VOLLEYBALL') {
        scorePanel.innerHTML = `
            <div class="flex justify-between items-center px-12">
                <div class="text-center">
                    <span class="text-[10px] text-slate-500 block uppercase font-bold mb-1">Sets</span>
                    <span class="text-2xl font-black text-emerald-400">${score.team1SetsWon || 0}</span>
                </div>
                <div class="text-center bg-slate-900/60 border border-slate-800 px-6 py-2.5 rounded-xl">
                    <span class="text-[10px] text-slate-500 block uppercase font-bold">Current Set Points</span>
                    <span class="text-lg font-black text-white">${score.team1Points || 0} <span class="text-slate-600">:</span> ${score.team2Points || 0}</span>
                </div>
                <div class="text-center">
                    <span class="text-[10px] text-slate-500 block uppercase font-bold mb-1">Sets</span>
                    <span class="text-2xl font-black text-emerald-400">${score.team2SetsWon || 0}</span>
                </div>
            </div>
        `;
    }
    else if (sportName === 'KABADDI') {
        const halfText = score.currentHalf === 2 ? '2nd Half' : '1st Half';
        scorePanel.innerHTML = `
            <div class="flex justify-between items-center px-12">
                <div class="text-left">
                    <span class="text-[10px] text-slate-500 block uppercase font-bold mb-1">${match.team1.shortName || match.team1.teamName.substring(0,3).toUpperCase()}</span>
                    <span class="text-2xl font-black text-white">${score.team1Points || 0}</span>
                </div>
                <div class="text-center bg-slate-900/60 border border-slate-800 px-6 py-2.5 rounded-xl">
                    <span class="text-[10px] text-slate-500 block uppercase font-bold">${halfText}</span>
                    <span class="text-xs font-semibold text-slate-400">Tackles: ${score.team1TacklePoints || 0} - ${score.team2TacklePoints || 0}</span>
                </div>
                <div class="text-right">
                    <span class="text-[10px] text-slate-500 block uppercase font-bold mb-1">${match.team2.shortName || match.team2.teamName.substring(0,3).toUpperCase()}</span>
                    <span class="text-2xl font-black text-white">${score.team2Points || 0}</span>
                </div>
            </div>
        `;
    }
    else if (sportName === 'BADMINTON') {
        scorePanel.innerHTML = `
            <div class="flex justify-between items-center px-12">
                <div class="text-center">
                    <span class="text-[10px] text-slate-500 block uppercase font-bold mb-1">Sets Won</span>
                    <span class="text-2xl font-black text-emerald-400">${score.player1SetsWon || 0}</span>
                </div>
                <div class="text-center bg-slate-900/60 border border-slate-800 px-6 py-2.5 rounded-xl">
                    <span class="text-[10px] text-slate-500 block uppercase font-bold">Points</span>
                    <span class="text-lg font-black text-white">${score.player1Points || 0} : ${score.player2Points || 0}</span>
                </div>
                <div class="text-center">
                    <span class="text-[10px] text-slate-500 block uppercase font-bold mb-1">Sets Won</span>
                    <span class="text-2xl font-black text-emerald-400">${score.player2SetsWon || 0}</span>
                </div>
            </div>
        `;
    }
}
