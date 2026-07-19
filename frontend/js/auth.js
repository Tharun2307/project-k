// Sportex Authentication & Role-Based Guard Module
import { fetchWithAuth } from './api.js';
import { showToast } from './utils.js';

/**
 * Log in a user and store credentials.
 * @param {string} email 
 * @param {string} password 
 * @returns {Promise<Object>} User data
 */
export async function login(email, password) {
    try {
        const data = await fetchWithAuth('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ email, password })
        });
        
        if (data.token) {
            localStorage.setItem('token', data.token);
            localStorage.setItem('email', data.email);
            localStorage.setItem('role', data.role);
            localStorage.setItem('name', data.name);
            showToast(`Welcome back, ${data.name}!`, 'success');
            return data;
        } else {
            throw new Error('Invalid login response');
        }
    } catch (error) {
        throw error;
    }
}

/**
 * Terminate the user session and clear storage.
 */
export function logout() {
    localStorage.clear();
    showToast('Logged out successfully', 'info');
    setTimeout(() => {
        window.location.href = '/login.html';
    }, 800);
}

/**
 * Check if a token exists in storage.
 * @returns {boolean}
 */
export function isAuthenticated() {
    return !!localStorage.getItem('token');
}

/**
 * Check if logged-in user possesses a specific role.
 * @param {string} role 
 * @returns {boolean}
 */
export function hasRole(role) {
    return localStorage.getItem('role') === role;
}

/**
 * Retrieve current logged-in user details.
 * @returns {Object}
 */
export function getUserProfile() {
    return {
        email: localStorage.getItem('email'),
        role: localStorage.getItem('role'),
        name: localStorage.getItem('name')
    };
}

/**
 * Protect dashboard/admin pages.
 * Redirects to login if unauthenticated, or to index if wrong role.
 * @param {string[]} requiredRoles 
 */
export function guardPage(requiredRoles = []) {
    const role = localStorage.getItem('role');
    const token = localStorage.getItem('token');

    if (!token) {
        showToast('Please log in to access this page.', 'warning');
        window.location.href = '/login.html';
        return false;
    }

    if (requiredRoles.length > 0 && !requiredRoles.includes(role)) {
        showToast('Access denied: Unauthorized role.', 'error');
        setTimeout(() => {
            window.location.href = '/index.html';
        }, 1200);
        return false;
    }

    return true;
}
