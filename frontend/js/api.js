// Sportex REST API Utility Library
import { CONFIG } from './config.js';
import { showToast } from './utils.js';

/**
 * Centered fetch wrapper that attaches Authorization header,
 * handles HTTP status errors, and intercepts 401 unauthorized calls.
 * @param {string} endpoint - API path (e.g. /api/matches or full URL)
 * @param {Object} [options={}] - Standard Fetch options
 * @returns {Promise<any>}
 */
export async function fetchWithAuth(endpoint, options = {}) {
    const url = endpoint.startsWith('http') ? endpoint : `${CONFIG.API_BASE_URL}${endpoint}`;
    
    // Setup headers
    const headers = new Headers(options.headers || {});
    
    // Automatically set Content-Type to JSON if sending a body and content type is not defined
    if (options.body && !(options.body instanceof FormData) && !headers.has('Content-Type')) {
        headers.set('Content-Type', 'application/json');
    }

    // Attach JWT if present
    const token = localStorage.getItem('token');
    if (token) {
        headers.set('Authorization', `Bearer ${token}`);
    }

    const fetchOptions = {
        ...options,
        headers
    };

    try {
        const response = await fetch(url, fetchOptions);
        
        // Intercept 401 Unauthorized
        if (response.status === 401) {
            localStorage.clear();
            showToast('Session expired. Please log in again.', 'error');
            // Redirect to login page
            setTimeout(() => {
                window.location.href = '/login.html';
            }, 1000);
            throw new Error('Unauthorized');
        }

        // Handle other HTTP errors
        if (!response.ok) {
            let errorMsg = `Server error: ${response.statusText}`;
            try {
                const errData = await response.json();
                errorMsg = errData.message || errorMsg;
            } catch (e) {
                // If response is not JSON
                try {
                    const txt = await response.text();
                    if (txt) errorMsg = txt;
                } catch (e2) {}
            }
            throw new Error(errorMsg);
        }

        // Return JSON or text depending on response header
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            return await response.json();
        } else {
            return await response.text();
        }

    } catch (error) {
        if (error.message !== 'Unauthorized') {
            console.error('API Error:', error);
            showToast(error.message || 'Network request failed', 'error');
        }
        throw error;
    }
}
