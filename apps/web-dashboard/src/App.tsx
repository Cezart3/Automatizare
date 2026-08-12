import React, { useState } from 'react';
import { 
  LayoutDashboard, 
  Mail, 
  Settings, 
  FileText, 
  Users, 
  Search,
  Bell,
  CheckCircle2,
  XCircle,
  Loader2,
  Download,
  Activity,
  Calendar,
  MessageSquare
} from 'lucide-react';
import { mockOrders, mockStats } from './data';
import { CalendarView } from './components/CalendarView';
import { KanbanBoard } from './components/KanbanBoard';
import './App.css'; // Just keeping the import if it exists

const App: React.FC = () => {
  const [activeTab, setActiveTab] = useState('dashboard');

  return (
    <div style={{ display: 'flex', minHeight: '100vh', width: '100vw', overflow: 'hidden' }}>
      
      {/* Sidebar */}
      <aside className="glass-panel" style={{ width: '280px', margin: '16px', display: 'flex', flexDirection: 'column' }}>
        <div style={{ padding: '24px', borderBottom: '1px solid var(--border-color)', display: 'flex', alignItems: 'center', gap: '12px' }}>
          <div style={{ width: '32px', height: '32px', borderRadius: '8px', background: 'linear-gradient(135deg, var(--accent-primary), #8b5cf6)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <Activity size={20} color="white" />
          </div>
          <h1 style={{ fontSize: '1.25rem', fontWeight: 600, letterSpacing: '-0.025em' }}>OmniBusiness ERP</h1>
        </div>
        
        <nav style={{ padding: '16px', display: 'flex', flexDirection: 'column', gap: '8px', flex: 1 }}>
          <NavItem icon={<LayoutDashboard />} label="Dashboard" active={activeTab === 'dashboard'} onClick={() => setActiveTab('dashboard')} />
          <NavItem icon={<Calendar />} label="Calendar & Programari" active={activeTab === 'calendar'} onClick={() => setActiveTab('calendar')} />
          <NavItem icon={<FileText />} label="Comenzi & Facturare" active={activeTab === 'orders'} onClick={() => setActiveTab('orders')} />
          <NavItem icon={<MessageSquare />} label="Support Ticketing" active={activeTab === 'tickets'} onClick={() => setActiveTab('tickets')} />
          <NavItem icon={<Mail />} label="Inbox Automatizari" active={activeTab === 'inbox'} onClick={() => setActiveTab('inbox')} />
          <NavItem icon={<Users />} label="CRM & Clienti" active={activeTab === 'customers'} onClick={() => setActiveTab('customers')} />
        </nav>
        
        <div style={{ padding: '16px', borderTop: '1px solid var(--border-color)' }}>
          <NavItem icon={<Settings />} label="Setari ERP" active={activeTab === 'settings'} onClick={() => setActiveTab('settings')} />
        </div>
      </aside>

      {/* Main Content */}
      <main style={{ flex: 1, padding: '16px 32px 32px 16px', display: 'flex', flexDirection: 'column', overflowY: 'auto' }}>
        
        {/* Header */}
        <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', paddingBottom: '24px', paddingTop: '8px' }}>
          <div>
            <h2 style={{ fontSize: '1.75rem', fontWeight: 600, marginBottom: '4px' }}>Overview Universal</h2>
            <p style={{ color: 'var(--text-secondary)' }}>Statusul automatizarilor in timp real</p>
          </div>
          
          <div style={{ display: 'flex', gap: '16px', alignItems: 'center' }}>
            <div className="glass-card" style={{ display: 'flex', alignItems: 'center', padding: '8px 16px', borderRadius: '999px' }}>
              <Search size={18} color="var(--text-muted)" />
              <input 
                type="text" 
                placeholder="Cauta tichete, clienti..." 
                style={{ background: 'transparent', border: 'none', color: 'white', marginLeft: '8px', outline: 'none', width: '200px' }}
              />
            </div>
            <button className="glass-card" style={{ width: '40px', height: '40px', display: 'flex', alignItems: 'center', justifyContent: 'center', borderRadius: '50%', cursor: 'pointer', border: '1px solid var(--border-color)', background: 'var(--bg-glass)' }}>
              <Bell size={20} color="var(--text-primary)" />
            </button>
            <div style={{ width: '40px', height: '40px', borderRadius: '50%', background: 'var(--text-muted)', overflow: 'hidden' }}>
              <img src="https://ui-avatars.com/api/?name=Admin&background=3b82f6&color=fff" alt="Profile" style={{ width: '100%', height: '100%' }} />
            </div>
          </div>
        </header>

        {activeTab === 'dashboard' && (
          <>
            {/* Stats Grid */}
            <div className="animate-fade-in" style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '24px', marginBottom: '32px' }}>
              <StatCard title="Comenzi Procesate" value={mockStats.totalOrders.toString()} trend="+12% luna aceasta" />
              <StatCard title="Rata Succes" value={`${mockStats.successRate}%`} trend="+2.4% luna aceasta" />
              <StatCard title="Valoare Generata" value={mockStats.revenueGenerated} trend="+€1.2k fata de ieri" />
              <StatCard title="Procese Active" value={mockStats.activeAutomations.toString()} trend="Toate functionale" />
            </div>

            {/* Recent Orders Table */}
            <div className="glass-panel animate-fade-in delay-100" style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
              <div style={{ padding: '20px 24px', borderBottom: '1px solid var(--border-color)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <h3 style={{ fontSize: '1.25rem', fontWeight: 500 }}>Activitati Recente</h3>
                <button style={{ background: 'transparent', color: 'var(--accent-primary)', border: 'none', cursor: 'pointer', fontWeight: 500 }}>Vezi toate</button>
              </div>
              
              <div style={{ overflowX: 'auto', flex: 1 }}>
                <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
                  <thead>
                    <tr style={{ color: 'var(--text-muted)', fontSize: '0.875rem', borderBottom: '1px solid var(--border-color)' }}>
                      <th style={{ padding: '16px 24px', fontWeight: 500 }}>ID Comanda</th>
                      <th style={{ padding: '16px 24px', fontWeight: 500 }}>Client</th>
                      <th style={{ padding: '16px 24px', fontWeight: 500 }}>Subiect / Detalii extrase</th>
                      <th style={{ padding: '16px 24px', fontWeight: 500 }}>Status Automatizare</th>
                      <th style={{ padding: '16px 24px', fontWeight: 500 }}>Actiune</th>
                    </tr>
                  </thead>
                  <tbody>
                    {mockOrders.map((order) => (
                      <tr key={order.id} style={{ borderBottom: '1px solid rgba(255,255,255,0.05)', transition: 'background 0.2s', cursor: 'pointer' }} className="table-row-hover">
                        <td style={{ padding: '16px 24px', fontWeight: 500 }}>{order.id}</td>
                        <td style={{ padding: '16px 24px' }}>
                          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                            <Mail size={16} color="var(--text-muted)" />
                            {order.customerEmail}
                          </div>
                        </td>
                        <td style={{ padding: '16px 24px' }}>
                          <div style={{ fontWeight: 500, marginBottom: '4px' }}>{order.subject}</div>
                          <div style={{ fontSize: '0.875rem', color: 'var(--text-secondary)' }}>{order.details}</div>
                        </td>
                        <td style={{ padding: '16px 24px' }}>
                          <StatusBadge status={order.status} />
                        </td>
                        <td style={{ padding: '16px 24px' }}>
                          {order.status === 'success' ? (
                            <button style={{ display: 'flex', alignItems: 'center', gap: '6px', background: 'rgba(59, 130, 246, 0.1)', color: 'var(--accent-primary)', border: '1px solid rgba(59, 130, 246, 0.2)', padding: '6px 12px', borderRadius: '6px', cursor: 'pointer', fontSize: '0.875rem', fontWeight: 500, transition: 'all 0.2s' }}>
                              <Download size={16} /> Detalii
                            </button>
                          ) : (
                            <span style={{ color: 'var(--text-muted)', fontSize: '0.875rem' }}>N/A</span>
                          )}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </>
        )}

        {activeTab === 'calendar' && <CalendarView />}
        {activeTab === 'tickets' && <KanbanBoard />}

      </main>
      
      <style>{`
        .table-row-hover:hover {
          background: rgba(255, 255, 255, 0.02);
        }
      `}</style>
    </div>
  );
};

// Subcomponents

const NavItem = ({ icon, label, active, onClick }: { icon: React.ReactNode, label: string, active: boolean, onClick: () => void }) => {
  return (
    <button 
      onClick={onClick}
      style={{
        display: 'flex',
        alignItems: 'center',
        gap: '12px',
        width: '100%',
        padding: '12px 16px',
        borderRadius: 'var(--border-radius-sm)',
        background: active ? 'rgba(59, 130, 246, 0.15)' : 'transparent',
        color: active ? 'var(--text-primary)' : 'var(--text-secondary)',
        border: '1px solid',
        borderColor: active ? 'rgba(59, 130, 246, 0.3)' : 'transparent',
        cursor: 'pointer',
        transition: 'all 0.2s ease',
        textAlign: 'left',
        fontWeight: active ? 500 : 400
      }}
      onMouseEnter={(e) => {
        if (!active) e.currentTarget.style.background = 'rgba(255, 255, 255, 0.05)';
      }}
      onMouseLeave={(e) => {
        if (!active) e.currentTarget.style.background = 'transparent';
      }}
    >
      {React.cloneElement(icon as React.ReactElement<any>, { size: 20, color: active ? 'var(--accent-primary)' : 'var(--text-muted)' })}
      {label}
    </button>
  );
};

const StatCard = ({ title, value, trend }: { title: string, value: string, trend: string }) => {
  return (
    <div className="glass-panel" style={{ padding: '24px', display: 'flex', flexDirection: 'column', gap: '8px' }}>
      <div style={{ color: 'var(--text-secondary)', fontSize: '0.875rem', fontWeight: 500 }}>{title}</div>
      <div style={{ fontSize: '2rem', fontWeight: 700, color: 'var(--text-primary)' }}>{value}</div>
      <div style={{ color: 'var(--success)', fontSize: '0.875rem', display: 'flex', alignItems: 'center', gap: '4px' }}>
        {trend}
      </div>
    </div>
  );
};

const StatusBadge = ({ status }: { status: string }) => {
  switch (status) {
    case 'success':
      return (
        <div className="status-badge status-success" style={{ display: 'inline-flex', alignItems: 'center', gap: '4px' }}>
          <CheckCircle2 size={12} /> Succes
        </div>
      );
    case 'failed':
      return (
        <div className="status-badge status-danger" style={{ display: 'inline-flex', alignItems: 'center', gap: '4px' }}>
          <XCircle size={12} /> Esuat
        </div>
      );
    case 'processing':
      return (
        <div className="status-badge status-info" style={{ display: 'inline-flex', alignItems: 'center', gap: '4px' }}>
          <Loader2 size={12} className="animate-spin" style={{ animation: 'spin 2s linear infinite' }} /> In Lucru
        </div>
      );
    default:
      return <div className="status-badge">{status}</div>;
  }
};

export default App;
