import React from 'react';
import { Calendar as CalendarIcon, Clock, Users } from 'lucide-react';

export const CalendarView: React.FC = () => {
  return (
    <div className="glass-panel animate-fade-in" style={{ padding: '24px', flex: 1, display: 'flex', flexDirection: 'column' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
        <h3 style={{ fontSize: '1.25rem', fontWeight: 600 }}>Calendar & Programari</h3>
        <div style={{ display: 'flex', gap: '8px' }}>
          <button style={{ background: 'var(--bg-glass)', border: '1px solid var(--border-color)', color: 'white', padding: '8px 16px', borderRadius: '6px' }}>Astazi</button>
          <button style={{ background: 'var(--accent-primary)', border: 'none', color: 'white', padding: '8px 16px', borderRadius: '6px', fontWeight: 500 }}>+ Programare Noua</button>
        </div>
      </div>
      
      <div style={{ flex: 1, display: 'grid', gridTemplateColumns: 'repeat(5, 1fr)', gap: '16px' }}>
        {/* Simple Mockup Grid */}
        {['Luni', 'Marti', 'Miercuri', 'Joi', 'Vineri'].map(day => (
          <div key={day} style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
            <div style={{ fontWeight: 500, color: 'var(--text-secondary)', paddingBottom: '12px', borderBottom: '1px solid var(--border-color)' }}>{day}</div>
            {day === 'Marti' && (
              <div className="glass-card" style={{ padding: '12px', borderRadius: '8px', borderLeft: '4px solid var(--accent-primary)' }}>
                <div style={{ fontSize: '0.875rem', fontWeight: 500, marginBottom: '4px' }}>Intalnire Client B2B</div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '4px', fontSize: '0.75rem', color: 'var(--text-secondary)' }}>
                  <Clock size={12} /> 10:00 - 11:30
                </div>
              </div>
            )}
            {day === 'Joi' && (
              <div className="glass-card" style={{ padding: '12px', borderRadius: '8px', borderLeft: '4px solid #10b981' }}>
                <div style={{ fontSize: '0.875rem', fontWeight: 500, marginBottom: '4px' }}>Review Oferte Noi</div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '4px', fontSize: '0.75rem', color: 'var(--text-secondary)' }}>
                  <Users size={12} /> Echipa Vanzari
                </div>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};
