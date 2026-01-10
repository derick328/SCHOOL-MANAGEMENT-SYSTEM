// School Management System - Shared JavaScript

const API_BASE = 'http://localhost:8080/api';

// Authentication utilities
const Auth = {
    getToken: () => localStorage.getItem('token'),
    
    getUser: () => {
        const userStr = localStorage.getItem('user');
        return userStr ? JSON.parse(userStr) : null;
    },
    
    setToken: (token) => localStorage.setItem('token', token),
    
    setUser: (user) => localStorage.setItem('user', JSON.stringify(user)),
    
    clearAuth: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        localStorage.removeItem('refreshToken');
    },
    
    isAuthenticated: () => !!Auth.getToken(),
    
    getRole: () => {
        const user = Auth.getUser();
        return user ? user.role : null;
    },
    
    getUserInitials: () => {
        const user = Auth.getUser();
        if (!user) return '?';
        return (user.firstName?.[0] || '') + (user.lastName?.[0] || '');
    }
};

// API utilities
const API = {
    async request(endpoint, options = {}) {
        const token = Auth.getToken();
        const headers = {
            'Content-Type': 'application/json',
            ...(token && { 'Authorization': `Bearer ${token}` }),
            ...options.headers
        };

        try {
            const response = await fetch(`${API_BASE}${endpoint}`, {
                ...options,
                headers
            });

            const data = await response.json();
            
            if (!response.ok) {
                throw new Error(data.message || 'Request failed');
            }
            
            return data;
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    },

    async login(credentials) {
        const data = await this.request('/auth/login', {
            method: 'POST',
            body: JSON.stringify(credentials)
        });
        
        if (data.success && data.data) {
            Auth.setToken(data.data.accessToken);
            if (data.data.refreshToken) {
                localStorage.setItem('refreshToken', data.data.refreshToken);
            }
        }
        
        return data;
    },

    async logout() {
        try {
            await this.request('/auth/logout', { method: 'POST' });
        } finally {
            Auth.clearAuth();
            window.location.href = '/login.html';
        }
    },

    async getCurrentUser() {
        const data = await this.request('/auth/me');
        if (data.success && data.data) {
            Auth.setUser(data.data);
        }
        return data;
    }
};

// UI utilities
const UI = {
    showAlert(message, type = 'success') {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type}`;
        alertDiv.textContent = message;
        
        const container = document.querySelector('.content') || document.body;
        container.insertBefore(alertDiv, container.firstChild);
        
        setTimeout(() => alertDiv.remove(), 5000);
    },

    showLoading(element) {
        element.innerHTML = '<div class="spinner"></div>';
    },

    formatDate(date) {
        return new Date(date).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });
    },

    formatTime(date) {
        return new Date(date).toLocaleTimeString('en-US', {
            hour: '2-digit',
            minute: '2-digit'
        });
    }
};

// Check authentication on protected pages
function checkAuth() {
    if (!Auth.isAuthenticated()) {
        window.location.href = '/login.html';
        return false;
    }
    return true;
}

// Load user info in header
async function loadUserInfo() {
    try {
        const userData = Auth.getUser();
        
        if (!userData) {
            const response = await API.getCurrentUser();
            if (response.success && response.data) {
                Auth.setUser(response.data);
            }
        }
        
        const user = Auth.getUser();
        if (user) {
            const userNameElement = document.getElementById('userName');
            const userAvatarElement = document.getElementById('userAvatar');
            
            if (userNameElement) {
                userNameElement.textContent = `${user.firstName} ${user.lastName}`;
            }
            if (userAvatarElement) {
                userAvatarElement.textContent = Auth.getUserInitials();
            }
        }
    } catch (error) {
        console.error('Error loading user info:', error);
    }
}

// Logout handler
function handleLogout() {
    if (confirm('Are you sure you want to logout?')) {
        API.logout();
    }
}

// Initialize dashboard
async function initDashboard() {
    if (!checkAuth()) return;
    await loadUserInfo();
}
