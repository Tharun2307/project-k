import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../api/axios';

const PublicDashboard = () => {
  const [activeTab, setActiveTab] = useState('live');
  const [matches, setMatches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [darkMode, setDarkMode] = useState(false);
  const navigate = useNavigate();

  // Fetch matches from backend
  useEffect(() => {
    const fetchMatches = async () => {
      try {
        const response = await api.get('/api/matches');
        setMatches(response.data);
      } catch (err) {
        setError('Failed to load matches. Is the backend running?');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchMatches();
  }, []);

  // Filter matches based on active tab
  const filteredMatches = matches.filter(match => {
    if (activeTab === 'live') return match.status === 'LIVE';
    if (activeTab === 'upcoming') return match.status === 'UPCOMING';
    if (activeTab === 'completed') return match.status === 'COMPLETED';
    return true;
  });

  // Toggle dark mode
  const toggleDarkMode = () => {
    setDarkMode(!darkMode);
    document.documentElement.classList.toggle('dark');
  };

  // Format date
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', { 
      month: 'short', 
      day: 'numeric', 
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  if (loading) {
    return (
      <div className={`min-h-screen flex items-center justify-center ${darkMode ? 'bg-gray-900' : 'bg-gray-100'}`}>
        <div className="text-xl text-gray-600">Loading Matches...</div>
      </div>
    );
  }

  return (
    <div className={`min-h-screen transition-colors duration-300 ${darkMode ? 'bg-gray-900' : 'bg-gray-100'}`}>
      
      {/* Navigation Bar */}
      <nav className={`${darkMode ? 'bg-gray-800 border-gray-700' : 'bg-white border-gray-200'} border-b shadow-md`}>
        <div className="container mx-auto px-4">
          <div className="flex justify-between items-center h-16">
            
            {/* Logo */}
            <div className="flex items-center">
              <h1 className={`text-2xl font-bold ${darkMode ? 'text-white' : 'text-gray-800'}`}>
                🏏 LiveScore
              </h1>
            </div>

            {/* Navigation Tabs */}
            <div className="hidden md:flex space-x-1">
              {['live', 'upcoming', 'completed'].map((tab) => (
                <button
                  key={tab}
                  onClick={() => setActiveTab(tab)}
                  className={`px-4 py-2 rounded-lg font-medium capitalize transition-colors ${
                    activeTab === tab
                      ? 'bg-blue-600 text-white'
                      : darkMode
                      ? 'text-gray-300 hover:bg-gray-700'
                      : 'text-gray-600 hover:bg-gray-100'
                  }`}
                >
                  {tab} Matches
                  {tab === 'live' && filteredMatches.length > 0 && (
                    <span className="ml-2 bg-red-500 text-white text-xs px-2 py-0.5 rounded-full">
                      {filteredMatches.length}
                    </span>
                  )}
                </button>
              ))}
            </div>

            {/* Right Side Buttons */}
            <div className="flex items-center space-x-3">
              {/* Dark/Light Theme Toggle */}
              <button
                onClick={toggleDarkMode}
                className={`p-2 rounded-lg transition-colors ${
                  darkMode ? 'bg-gray-700 text-yellow-400' : 'bg-gray-100 text-gray-600'
                }`}
                title={darkMode ? 'Switch to Light Mode' : 'Switch to Dark Mode'}
              >
                {darkMode ? (
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z" />
                  </svg>
                ) : (
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" />
                  </svg>
                )}
              </button>

              {/* Login Button */}
              <button
                onClick={() => navigate('/login')}
                className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium"
              >
                Login
              </button>
            </div>
          </div>

          {/* Mobile Navigation Tabs */}
          <div className="md:hidden flex space-x-1 pb-3">
            {['live', 'upcoming', 'completed'].map((tab) => (
              <button
                key={tab}
                onClick={() => setActiveTab(tab)}
                className={`flex-1 px-3 py-2 rounded-lg font-medium capitalize text-sm transition-colors ${
                  activeTab === tab
                    ? 'bg-blue-600 text-white'
                    : darkMode
                    ? 'text-gray-300 bg-gray-700'
                    : 'text-gray-600 bg-gray-100'
                }`}
              >
                {tab}
              </button>
            ))}
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <div className="container mx-auto px-4 py-8">
        {/* Page Header */}
        <div className="mb-8">
          <h2 className={`text-3xl font-bold ${darkMode ? 'text-white' : 'text-gray-800'}`}>
            {activeTab === 'live' && '🔴 Live Matches'}
            {activeTab === 'upcoming' && ' Upcoming Matches'}
            {activeTab === 'completed' && '✅ Completed Matches'}
          </h2>
          <p className={`mt-2 ${darkMode ? 'text-gray-400' : 'text-gray-600'}`}>
            {filteredMatches.length} {filteredMatches.length === 1 ? 'match' : 'matches'} found
          </p>
        </div>

        {/* Error Message */}
        {error && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
            {error}
          </div>
        )}

        {/* Matches Grid */}
        {filteredMatches.length === 0 ? (
          <div className={`text-center py-20 ${darkMode ? 'text-gray-400' : 'text-gray-600'}`}>
            <svg className="w-16 h-16 mx-auto mb-4 opacity-50" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <p className="text-xl">No {activeTab} matches found</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredMatches.map((match) => (
              <div
                key={match.id}
                className={`${darkMode ? 'bg-gray-800 border-gray-700' : 'bg-white border-gray-200'} rounded-lg shadow-md hover:shadow-lg transition-shadow border overflow-hidden`}
              >
                {/* Match Header */}
                <div className={`${darkMode ? 'bg-gray-700' : 'bg-gray-50'} px-4 py-3 border-b ${darkMode ? 'border-gray-600' : 'border-gray-200'}`}>
                  <div className="flex justify-between items-center">
                    <span className={`text-sm font-medium ${darkMode ? 'text-gray-300' : 'text-gray-700'}`}>
                      {match.sportName}
                    </span>
                    <span className={`px-2 py-1 text-xs font-semibold rounded-full ${
                      match.status === 'LIVE' ? 'bg-red-500 text-white' :
                      match.status === 'UPCOMING' ? 'bg-blue-500 text-white' :
                      'bg-green-500 text-white'
                    }`}>
                      {match.status}
                    </span>
                  </div>
                </div>

                {/* Match Teams */}
                <div className="p-4">
                  <div className="space-y-3">
                    {/* Team 1 */}
                    <div className="flex justify-between items-center">
                      <span className={`font-semibold ${darkMode ? 'text-white' : 'text-gray-800'}`}>
                        {match.team1Name}
                      </span>
                    </div>

                    {/* VS */}
                    <div className="text-center">
                      <span className={`text-sm ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>VS</span>
                    </div>

                    {/* Team 2 */}
                    <div className="flex justify-between items-center">
                      <span className={`font-semibold ${darkMode ? 'text-white' : 'text-gray-800'}`}>
                        {match.team2Name}
                      </span>
                    </div>
                  </div>

                  {/* Match Date */}
                  <div className={`mt-4 pt-3 border-t ${darkMode ? 'border-gray-600' : 'border-gray-200'}`}>
                    <div className={`text-sm ${darkMode ? 'text-gray-400' : 'text-gray-600'}`}>
                      📅 {formatDate(match.matchDate)}
                    </div>
                  </div>

                  {/* View Details Button */}
                  <button
                    onClick={() => navigate(`/match/${match.id}`)}
                    className="w-full mt-4 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium"
                  >
                    View Live Score
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default PublicDashboard;