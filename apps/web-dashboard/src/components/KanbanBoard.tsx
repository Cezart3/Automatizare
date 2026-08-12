import React from 'react';
import { MoreVertical, MessageSquare } from 'lucide-react';

const columns = [
  { id: 'todo', title: 'Tichete Noi', color: '#3b82f6' },
  { id: 'in-progress', title: 'In Lucru', color: '#f59e0b' },
  { id: 'review', title: 'Asteapta Raspuns', color: '#8b5cf6' },
  { id: 'done', title: 'Rezolvat', color: '#10b981' }
];

export const KanbanBoard: React.FC = () => {
  return (
    <div className="animate-fade-in delay-100" style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
      <div style={{ marginBottom: '24px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h3 style={{ fontSize: '1.25rem', fontWeight: 600 }}>Support & Ticketing</h3>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Managementul cererilor si problemelor generale</p>
        </div>
        <button style={{ background: 'var(--accent-primary)', border: 'none', color: 'white', padding: '8px 16px', borderRadius: '6px', fontWeight: 500 }}>
          + Tichet Nou
        </button>
      </div>

      <div style={{ flex: 1, display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '20px', overflowX: 'auto' }}>
        {columns.map(col => (
          <div key={col.id} className="glass-panel" style={{ padding: '16px', display: 'flex', flexDirection: 'column', gap: '16px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <div style={{ width: '12px', height: '12px', borderRadius: '50%', backgroundColor: col.color }} />
                <span style={{ fontWeight: 500 }}>{col.title}</span>
              </div>
              <MoreVertical size={16} color="var(--text-muted)" />
            </div>

            {/* Mock Task */}
            {col.id === 'todo' && (
              <div className="glass-card" style={{ padding: '16px', borderRadius: '8px', cursor: 'grab' }}>
                <div style={{ fontSize: '0.75rem', padding: '2px 8px', background: 'rgba(59, 130, 246, 0.2)', color: '#60a5fa', borderRadius: '999px', display: 'inline-block', marginBottom: '8px' }}>Cerere Factura</div>
                <div style={{ fontWeight: 500, marginBottom: '8px' }}>Clientul SC Exemplu SRL solicita factura pentru luna trecuta.</div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '4px', fontSize: '0.75rem', color: 'var(--text-secondary)' }}>
                  <MessageSquare size={12} /> 2 comentarii
                </div>
              </div>
            )}
            
            {col.id === 'in-progress' && (
              <div className="glass-card" style={{ padding: '16px', borderRadius: '8px', cursor: 'grab' }}>
                <div style={{ fontSize: '0.75rem', padding: '2px 8px', background: 'rgba(245, 158, 11, 0.2)', color: '#fbbf24', borderRadius: '999px', display: 'inline-block', marginBottom: '8px' }}>Tehnic</div>
                <div style={{ fontWeight: 500, marginBottom: '8px' }}>Verificare conexiune SMTP cu serverul Yahoo.</div>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};
