import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios';

const PublicDashboard = () => {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchStats = async () => {
      try {
        // Fetch data from your backend public endpoint
        const response = await api.get('/api/dashboard/public');
        setStats(response.data);
      } catch (err) {
        setError('Failed to load dashboard stats. Is the backend running?');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, []);

  if (loading) {
    return <div className="text-center mt-20 text-xl text-gray-600">Loading Dashboard...</div>;
  }

  if (error) {
    return <div className="text-center mt-20 text-xl text-red-500">{error}</div>;
  }

  // Helper component for the stat cards
  const StatCard = ({ title, value, color }) => (
    <div className={`bg-white p-6 rounded-lg shadow-md border-l-4 ${color}`}>
      <h3 className="text-gray-500 text-sm font-medium uppercase">{title}</h3>
      <p className="text-3xl font-bold text-gray-800 mt-2">{value}</p>
    </div>
  );

  return (
    <div className="min-h-screen bg-gray-100">
      {/* Simple Navbar */}
      <nav className="bg-blue-600 text-white p-4 shadow-md">
        <div className="container mx-auto flex justify-between items-center">
          <h1 className="text-2xl font-bold">LiveScore App</h1>
          <div className="space-x-4">
            <Link to="/login" className="hover:text-gray-200">Login</Link>
            <Link to="/register" className="hover:text-gray-200">Register</Link>
          </div>
        </div>
      </nav>

      {/* Dashboard Content */}
      <div className="container mx-auto p-8">
        <h2 className="text-3xl font-bold text-gray-800 mb-8">Public Dashboard</h2>
        
        {/* Main Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <StatCard title="Total Sports" value={stats.totalSports} color="border-blue-500" />
          <StatCard title="Total Teams" value={stats.totalTeams} color="border-green-500" />
          <StatCard title="Total Players" value={stats.totalPlayers} color="border-yellow-500" />
          <StatCard title="Total Matches" value={stats.totalMatches} color="border-purple-500" />
        </div>

        {/* Match Status Breakdown */}
        <h3 className="text-xl font-semibold text-gray-700 mb-4">Match Status</h3>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <StatCard title="Live Matches" value={stats.liveMatches} color="border-red-500" />
          <StatCard title="Upcoming Matches" value={stats.upcomingMatches} color="border-indigo-500" />
          <StatCard title="Completed Matches" value={stats.completedMatches} color="border-gray-500" />
          <StatCard title="Cancelled Matches" value={stats.cancelledMatches} color="border-pink-500" />
        </div>
      </div>
    </div>
  );
};

export default PublicDashboard;