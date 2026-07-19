// Sportex Utility Library

/**
 * Re-initialize Lucide Icons across the DOM.
 */
export function initLucideIcons() {
    if (window.lucide) {
        window.lucide.createIcons();
    }
}

/**
 * Format ISO datetime string into human readable string.
 * @param {string} dateStr 
 * @returns {string}
 */
export function formatDate(dateStr) {
    if (!dateStr) return 'TBD';
    const date = new Date(dateStr);
    return date.toLocaleDateString(undefined, { 
        weekday: 'short', 
        month: 'short', 
        day: 'numeric', 
        year: 'numeric' 
    });
}

/**
 * Format ISO datetime into hours and minutes.
 * @param {string} dateStr 
 * @returns {string}
 */
export function formatTime(dateStr) {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    return date.toLocaleTimeString(undefined, {
        hour: '2-digit',
        minute: '2-digit'
    });
}

/**
 * Helper to display values nicely.
 */
export function formatNumber(val) {
    return isNaN(val) ? 0 : Number(val);
}

/**
 * Smoothly animate value counting.
 * @param {HTMLElement} element 
 * @param {number} start 
 * @param {number} end 
 * @param {number} duration 
 */
export function animateValue(element, start, end, duration = 600) {
    if (!element) return;
    let startTimestamp = null;
    const step = (timestamp) => {
        if (!startTimestamp) startTimestamp = timestamp;
        const progress = Math.min((timestamp - startTimestamp) / duration, 1);
        element.innerHTML = Math.floor(progress * (end - start) + start);
        if (progress < 1) {
            window.requestAnimationFrame(step);
        } else {
            element.innerHTML = end;
        }
    };
    window.requestAnimationFrame(step);
}

/**
 * Toast notifications. Creates container if it doesn't exist.
 * @param {string} message 
 * @param {'success' | 'error' | 'info' | 'warning'} type 
 */
export function showToast(message, type = 'success') {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'fixed top-4 right-4 z-50 flex flex-col gap-2 max-w-sm w-full pointer-events-none px-4';
        document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `flex items-center gap-3 p-4 rounded-xl shadow-2xl glass-panel border transition-all duration-300 transform translate-x-12 opacity-0 pointer-events-auto`;
    
    let borderCol = 'border-emerald-500/30';
    let textCol = 'text-emerald-400';
    let iconName = 'check-circle';
    
    if (type === 'error') {
        borderCol = 'border-rose-500/30';
        textCol = 'text-rose-400';
        iconName = 'alert-triangle';
    } else if (type === 'warning') {
        borderCol = 'border-amber-500/30';
        textCol = 'text-amber-400';
        iconName = 'alert-circle';
    } else if (type === 'info') {
        borderCol = 'border-sky-500/30';
        textCol = 'text-sky-400';
        iconName = 'info';
    }

    toast.innerHTML = `
        <i data-lucide="${iconName}" class="${textCol} w-5 h-5 flex-shrink-0"></i>
        <div class="text-sm font-medium text-slate-200">${message}</div>
        <button class="ml-auto text-slate-400 hover:text-slate-200 focus:outline-none" onclick="this.parentElement.remove()">
            <i data-lucide="x" class="w-4 h-4"></i>
        </button>
    `;

    container.appendChild(toast);
    initLucideIcons();

    // Trigger animation
    setTimeout(() => {
        toast.classList.remove('translate-x-12', 'opacity-0');
    }, 10);

    // Auto remove
    setTimeout(() => {
        toast.classList.add('translate-x-12', 'opacity-0');
        setTimeout(() => {
            toast.remove();
        }, 300);
    }, 4000);
}
