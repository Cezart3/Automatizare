import React, { useEffect, useState } from 'react';
import { Clock, X } from 'lucide-react';
import axios from 'axios';

export const CalendarView: React.FC = () => {
  const [appointments, setAppointments] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [newTitle, setNewTitle] = useState('');
  const [newDate, setNewDate] = useState('');

  const fetchAppointments = () => {
    setLoading(true);
    axios.get('http://localhost:3000/api/appointments')
      .then(res => setAppointments(res.data))
      .catch(err => console.error(err))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchAppointments();
  }, []);

  const handleAdd = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newTitle || !newDate) return;
    
    axios.post('http://localhost:3000/api/appointments', {
      title: newTitle,
      date: newDate,
      location: 'Sediu',
    })
    .then(() => {
      setIsModalOpen(false);
      setNewTitle('');
      setNewDate('');
      fetchAppointments();
    })
    .catch(err => console.error(err));
  };

  // Groupping by day for the mockup grid. Realistically this should be a calendar component.
  const days = ['Luni', 'Marti', 'Miercuri', 'Joi', 'Vineri'];

  return (
    <div className="glass-panel animate-fade-in" style={{ padding: '24px', flex: 1, display: 'flex', flexDirection: 'column', position: 'relative' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
        <h3 style={{ fontSize: '1.25rem', fontWeight: 600 }}>Calendar & Programari</h3>
        <div style={{ display: 'flex', gap: '8px' }}>
          <button style={{ background: 'var(--bg-glass)', border: '1px solid var(--border-color)', color: 'white', padding: '8px 16px', borderRadius: '6px' }}>Astazi</button>
          <button onClick={() => setIsModalOpen(true)} style={{ background: 'var(--accent-primary)', border: 'none', color: 'white', padding: '8px 16px', borderRadius: '6px', fontWeight: 500, cursor: 'pointer' }}>+ Programare Noua</button>
        </div>
      </div>
      
      {loading ? (
        <div style={{ padding: '20px', color: 'var(--text-muted)' }}>Se incarca...</div>
      ) : (
        <div style={{ flex: 1, display: 'grid', gridTemplateColumns: 'repeat(5, 1fr)', gap: '16px' }}>
          {days.map(day => (
            <div key={day} style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
              <div style={{ fontWeight: 500, color: 'var(--text-secondary)', paddingBottom: '12px', borderBottom: '1px solid var(--border-color)' }}>{day}</div>
              
              {appointments.filter(a => {
                const d = new Date(a.date).getDay();
                // 1=Luni, 2=Marti, 3=Miercuri, 4=Joi, 5=Vineri
                const map: any = { 1: 'Luni', 2: 'Marti', 3: 'Miercuri', 4: 'Joi', 5: 'Vineri' };
                return map[d] === day;
              }).map(appt => (
                <div key={appt.id} className="glass-card" style={{ padding: '12px', borderRadius: '8px', borderLeft: '4px solid var(--accent-primary)' }}>
                  <div style={{ fontSize: '0.875rem', fontWeight: 500, marginBottom: '4px' }}>{appt.title}</div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '4px', fontSize: '0.75rem', color: 'var(--text-secondary)' }}>
                    <Clock size={12} /> {new Date(appt.date).toLocaleTimeString('ro-RO', {hour: '2-digit', minute:'2-digit'})}
                  </div>
                </div>
              ))}
            </div>
          ))}
        </div>
      )}

      {isModalOpen && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 100 }}>
          <div className="glass-panel" style={{ padding: '24px', width: '400px', borderRadius: '12px', position: 'relative' }}>
            <button onClick={() => setIsModalOpen(false)} style={{ position: 'absolute', top: '16px', right: '16px', background: 'transparent', border: 'none', color: 'white', cursor: 'pointer' }}><X size={20}/></button>
            <h3 style={{ marginBottom: '16px', fontSize: '1.2rem', fontWeight: 600 }}>Adauga Programare</h3>
            <form onSubmit={handleAdd} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                <label style={{ fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Titlu Programare</label>
                <input required value={newTitle} onChange={e => setNewTitle(e.target.value)} type="text" style={{ background: 'var(--bg-glass)', border: '1px solid var(--border-color)', color: 'white', padding: '10px', borderRadius: '6px', outline: 'none' }} placeholder="Ex: Masuratori Client X" />
              </div>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                <label style={{ fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Data si Ora</label>
                <input required value={newDate} onChange={e => setNewDate(e.target.value)} type="datetime-local" style={{ background: 'var(--bg-glass)', border: '1px solid var(--border-color)', color: 'white', padding: '10px', borderRadius: '6px', outline: 'none', colorScheme: 'dark' }} />
              </div>
              <button type="submit" style={{ background: 'var(--accent-primary)', border: 'none', color: 'white', padding: '12px', borderRadius: '6px', fontWeight: 600, cursor: 'pointer', marginTop: '8px' }}>Salveaza</button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
