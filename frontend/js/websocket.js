// Sportex Real-time WebSocket Module
import { CONFIG } from './config.js';
import { showToast } from './utils.js';

let stompClient = null;
let reconnectDelay = 1000;
const maxReconnectDelay = 30000;
let isConnected = false;
let currentSubscription = null;
let currentMatchId = null;
let onUpdateCallback = null;

/**
 * Establish a STOMP over SockJS WebSocket connection.
 * @param {number|string} matchId - The match ID to monitor
 * @param {Function} callback - Function called with parsed score JSON on update
 */
export function connectWebSocket(matchId, callback) {
    if (stompClient && stompClient.connected && currentMatchId === matchId) {
        // Already connected to this match
        onUpdateCallback = callback;
        return;
    }

    // Clean up existing subscription or client
    disconnectWebSocket();

    currentMatchId = matchId;
    onUpdateCallback = callback;

    const wsUrl = `${CONFIG.WS_BASE_URL}`;
    console.log(`🔌 Initializing WebSocket connection to: ${wsUrl}`);
    
    // Create SockJS client
    const socket = new SockJS(wsUrl);
    stompClient = Stomp.over(socket);

    // Disable excessive Stomp debugging messages to keep console readable
    stompClient.debug = () => {};

    stompClient.connect(
        {},
        // Success Callback
        (frame) => {
            console.log('✅ Connected to WebSocket Server');
            isConnected = true;
            reconnectDelay = 1000; // Reset reconnect delay on success
            
            // Show status in UI if listeners exist
            const wsIndicator = document.getElementById('ws-status-indicator');
            if (wsIndicator) {
                wsIndicator.className = 'flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-semibold bg-emerald-500/10 text-emerald-400 border border-emerald-500/20';
                wsIndicator.innerHTML = '<span class="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse"></span> Connected';
            }

            // Subscribe to target match score updates
            const topic = `/topic/match/${currentMatchId}/score`;
            console.log(`📬 Subscribing to: ${topic}`);
            
            currentSubscription = stompClient.subscribe(topic, (message) => {
                try {
                    const scoreData = JSON.parse(message.body);
                    if (onUpdateCallback) {
                        onUpdateCallback(scoreData);
                    }
                } catch (e) {
                    console.error('Failed to parse websocket message body:', e);
                }
            });
        },
        // Error Callback / Disconnection
        (error) => {
            console.warn('⚠️ WebSocket Error/Disconnection:', error);
            handleDisconnect();
        }
    );
}

/**
 * Handle connection drops and trigger reconnect with exponential backoff.
 */
function handleDisconnect() {
    isConnected = false;
    currentSubscription = null;
    
    const wsIndicator = document.getElementById('ws-status-indicator');
    if (wsIndicator) {
        wsIndicator.className = 'flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-semibold bg-rose-500/10 text-rose-400 border border-rose-500/20';
        wsIndicator.innerHTML = '<span class="w-1.5 h-1.5 rounded-full bg-rose-400 animate-ping"></span> Reconnecting...';
    }

    console.log(`🔄 Attempting reconnect in ${reconnectDelay}ms...`);
    setTimeout(() => {
        if (currentMatchId && onUpdateCallback) {
            reconnectDelay = Math.min(reconnectDelay * 2, maxReconnectDelay);
            connectWebSocket(currentMatchId, onUpdateCallback);
        }
    }, reconnectDelay);
}

/**
 * Unsubscribe and disconnect from WebSockets.
 */
export function disconnectWebSocket() {
    if (currentSubscription) {
        currentSubscription.unsubscribe();
        currentSubscription = null;
    }
    if (stompClient && stompClient.connected) {
        stompClient.disconnect(() => {
            console.log('🔌 Disconnected WebSocket client');
        });
    }
    stompClient = null;
    isConnected = false;
    currentMatchId = null;
}
