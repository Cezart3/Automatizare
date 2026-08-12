import React from 'react';
import { BrowserRouter, Routes, Route, Link, useLocation } from 'react-router-dom';
import { LayoutDashboard, Calendar, Ticket, Settings, Bell, Search, Inbox, Package, Globe, Users } from 'lucide-react';
import { CalendarView } from './components/CalendarView';
import { KanbanBoard } from './components/KanbanBoard';
import { SettingsView } from './components/SettingsView';
import { OrdersView } from './components/OrdersView';
import { mockStats, mockOrders } from './data';

const NavItem = ({ icon, label, to }: { icon: React.ReactNode, label: string, to: string }) => {
  const location = useLocation();
  const active = location.pathname === to || (to === '/' && location.pathname === '/dashboard');
  return (
    <Link 
      to={to}
      style={{
        display: 'flex', alignItems: 'center', gap: '12px', padding: '12px 16px',
        background: active ? 'rgba(59, 130, 246, 0.15)' : 'transparent',
        color: active ? 'var(--accent-primary)' : 'var(--text-secondary)',
        textDecoration: 'none', borderRadius: '8px',
        fontWeight: active ? 500 : 400, transition: 'all 0.2s ease', border: 'none'
      }}
    >
      {React.cloneElement(icon as React.ReactElement<any>, { size: 20 })}
      {label}
    </Link>
  );
};

import axios from 'axios';

const DashboardView = () => {
  const [orders, setOrders] = React.useState<any[]>(mockOrders);
  const [loading, setLoading] = React.useState(true);

  React.useEffect(() => {
    axios.get('http://localhost:3000/api/orders')
      .then(res => {
        if (res.data && res.data.length > 0) setOrders(res.data);
      })
      .catch(err => console.error("Failed to fetch live orders", err))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="animate-fade-in" style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
      <div style={{ marginBottom: '24px' }}>
        <h3 style={{ fontSize: '1.25rem', fontWeight: 600 }}>Bun venit!</h3>
        <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Iată sumarul performanței tale.</p>
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '24px', marginBottom: '32px' }}>
        <div className="glass-panel stat-card" style={{ padding: '24px', display: 'flex', flexDirection: 'column', gap: '8px' }}>
          <div style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Total Comenzi</div>
          <div style={{ fontSize: '2rem', fontWeight: 700 }}>{mockStats.totalOrders}</div>
        </div>
        <div className="glass-panel stat-card" style={{ padding: '24px', display: 'flex', flexDirection: 'column', gap: '8px' }}>
          <div style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Rată Succes</div>
          <div style={{ fontSize: '2rem', fontWeight: 700 }}>{mockStats.successRate}%</div>
        </div>
        <div className="glass-panel stat-card" style={{ padding: '24px', display: 'flex', flexDirection: 'column', gap: '8px' }}>
          <div style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Venituri Generate</div>
          <div style={{ fontSize: '2rem', fontWeight: 700 }}>{mockStats.revenueGenerated}</div>
        </div>
      </div>
      <div className="glass-panel" style={{ flex: 1, padding: '24px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
          <h4 style={{ fontSize: '1.1rem', fontWeight: 600 }}>Comenzi Recente {loading && <span className="animate-pulse" style={{fontSize: '0.8rem', color: 'var(--accent-primary)'}}>(Live Sync...)</span>}</h4>
          <Link to="/orders" style={{ background: 'transparent', border: '1px solid var(--border-color)', color: 'var(--text-primary)', padding: '6px 12px', borderRadius: '6px', cursor: 'pointer', textDecoration: 'none', fontSize: '0.875rem' }}>Vezi toate</Link>
        </div>
        <div style={{ width: '100%', overflowX: 'auto' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
            <thead>
              <tr style={{ borderBottom: '1px solid var(--border-color)', color: 'var(--text-muted)' }}>
                <th style={{ padding: '12px 16px', fontWeight: 500 }}>Comandă</th>
                <th style={{ padding: '12px 16px', fontWeight: 500 }}>Client</th>
                <th style={{ padding: '12px 16px', fontWeight: 500 }}>Detalii</th>
                <th style={{ padding: '12px 16px', fontWeight: 500 }}>Status</th>
              </tr>
            </thead>
            <tbody>
              {orders.map(order => (
                <tr key={order.id} style={{ borderBottom: '1px solid rgba(255,255,255,0.05)', transition: 'background 0.2s ease' }}>
                  <td style={{ padding: '16px', fontWeight: 500 }}>{order.id || order.orderNumber}</td>
                  <td style={{ padding: '16px' }}>{order.customerEmail || (order.customer?.email)}</td>
                  <td style={{ padding: '16px' }}>{order.subject || order.details}</td>
                  <td style={{ padding: '16px' }}>
                    <span style={{
                      padding: '4px 8px', borderRadius: '12px', fontSize: '0.75rem', fontWeight: 500,
                      background: order.status === 'success' || order.status === 'COMPLETED' ? 'rgba(16, 185, 129, 0.1)' : 'rgba(245, 158, 11, 0.1)',
                      color: order.status === 'success' || order.status === 'COMPLETED' ? 'var(--success-color)' : 'var(--warning-color)'
                    }}>
                      {order.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

const AppContent = () => (
  <div style={{ display: 'flex', height: '100vh', width: '100vw', overflow: 'hidden' }}>
    {/* Sidebar */}
    <aside className="glass-panel" style={{ width: '280px', margin: '16px', display: 'flex', flexDirection: 'column', border: '1px solid var(--border-color)' }}>
      <div style={{ padding: '24px', borderBottom: '1px solid var(--border-color)', display: 'flex', alignItems: 'center', gap: '12px' }}>
        <div style={{ width: '32px', height: '32px', borderRadius: '8px', background: 'var(--accent-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          <Globe size={18} color="white" />
        </div>
        <h2 style={{ fontSize: '1.25rem', fontWeight: 600, letterSpacing: '-0.5px' }}>OmniBusiness ERP</h2>
      </div>
      <nav style={{ flex: 1, padding: '16px 12px', display: 'flex', flexDirection: 'column', gap: '4px' }}>
        <NavItem to="/" icon={<LayoutDashboard />} label="Dashboard" />
        <NavItem to="/orders" icon={<Package />} label="Comenzi & Facturi" />
        <NavItem to="/calendar" icon={<Calendar />} label="Calendar & Ședințe" />
        <NavItem to="/tickets" icon={<Ticket />} label="Asistență & Ticketing" />
        <NavItem to="/customers" icon={<Users />} label="Clienți (CRM)" />
        <div style={{ margin: '16px 0', borderBottom: '1px solid var(--border-color)' }} />
        <NavItem to="/inbox" icon={<Inbox />} label="Inbox Automatizări" />
        <NavItem to="/settings" icon={<Settings />} label="Setări & Module" />
      </nav>
      <div style={{ padding: '20px', borderTop: '1px solid var(--border-color)', display: 'flex', alignItems: 'center', gap: '12px' }}>
        <div style={{ width: '36px', height: '36px', borderRadius: '50%', background: 'var(--accent-secondary)' }} />
        <div>
          <div style={{ fontSize: '0.875rem', fontWeight: 500 }}>Admin User</div>
          <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Producție Sticlă</div>
        </div>
      </div>
    </aside>

    {/* Main Content */}
    <main style={{ flex: 1, display: 'flex', flexDirection: 'column', padding: '16px 16px 16px 0', overflow: 'hidden' }}>
      <header className="glass-panel" style={{ height: '70px', marginBottom: '16px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '0 24px' }}>
        <div style={{ display: 'flex', alignItems: 'center', background: 'var(--bg-glass)', borderRadius: '8px', padding: '8px 16px', border: '1px solid var(--border-color)', width: '300px' }}>
          <Search size={18} color="var(--text-muted)" />
          <input type="text" placeholder="Caută comenzi, clienți..." style={{ background: 'transparent', border: 'none', color: 'var(--text-primary)', marginLeft: '8px', outline: 'none', width: '100%' }} />
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
          <button style={{ background: 'transparent', border: 'none', color: 'var(--text-secondary)', cursor: 'pointer', position: 'relative' }}>
            <Bell size={20} />
            <span style={{ position: 'absolute', top: -2, right: -2, width: '8px', height: '8px', background: 'var(--danger-color)', borderRadius: '50%' }} />
          </button>
        </div>
      </header>

      <div style={{ flex: 1, overflowY: 'auto' }}>
        <Routes>
          <Route path="/" element={<DashboardView />} />
          <Route path="/calendar" element={<CalendarView />} />
          <Route path="/orders" element={<OrdersView />} />
          <Route path="/tickets" element={<KanbanBoard />} />
          <Route path="/settings" element={<SettingsView />} />
          <Route path="*" element={<div style={{ padding: 24 }}>In curand...</div>} />
        </Routes>
      </div>
    </main>
  </div>
);

const App: React.FC = () => {
  return (
    <BrowserRouter>
      <AppContent />
    </BrowserRouter>
  );
};

export default App;
