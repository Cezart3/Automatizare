import React, { useEffect, useState } from 'react';
import { MoreVertical, X } from 'lucide-react';
import axios from 'axios';

const columns = [
  { id: 'TODO', title: 'Tichete Noi', color: '#3b82f6' },
  { id: 'IN_PROGRESS', title: 'In Lucru', color: '#f59e0b' },
  { id: 'REVIEW', title: 'Asteapta Raspuns', color: '#8b5cf6' },
  { id: 'DONE', title: 'Rezolvat', color: '#10b981' }
];

export const KanbanBoard: React.FC = () => {
  const [tickets, setTickets] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [newTitle, setNewTitle] = useState('');
  const [newDesc, setNewDesc] = useState('');

  const fetchTickets = () => {
    setLoading(true);
    axios.get('http://localhost:3000/api/tickets')
      .then(res => setTickets(res.data))
      .catch(err => console.error(err))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchTickets();
  }, []);

  const handleAdd = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newTitle || !newDesc) return;
    
    axios.post('http://localhost:3000/api/tickets', {
      title: newTitle,
      description: newDesc,
      status: 'TODO',
      priority: 'MEDIUM'
    })
    .then(() => {
      setIsModalOpen(false);
      setNewTitle('');
      setNewDesc('');
      fetchTickets();
    })
    .catch(err => console.error(err));
  };

  const handleDragStart = (e: React.DragEvent, ticketId: number) => {
    e.dataTransfer.setData('ticketId', ticketId.toString());
  };

  const handleDrop = (e: React.DragEvent, newStatus: string) => {
    e.preventDefault();
    const ticketId = e.dataTransfer.getData('ticketId');
    if (!ticketId) return;

    axios.patch(`http://localhost:3000/api/tickets/${ticketId}`, { status: newStatus })
      .then(() => {
        // Optimistic UI update
        setTickets(tickets.map(t => t.id === parseInt(ticketId) ? { ...t, status: newStatus } : t));
      })
      .catch(err => console.error(err));
  };

  const handleDragOver = (e: React.DragEvent) => {
    e.preventDefault();
  };

  return (
    <div className="animate-fade-in delay-100" style={{ flex: 1, display: 'flex', flexDirection: 'column', position: 'relative' }}>
      <div style={{ marginBottom: '24px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h3 style={{ fontSize: '1.25rem', fontWeight: 600 }}>Support & Ticketing</h3>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Managementul cererilor si problemelor generale</p>
        </div>
        <button onClick={() => setIsModalOpen(true)} style={{ background: 'var(--accent-primary)', border: 'none', color: 'white', padding: '8px 16px', borderRadius: '6px', fontWeight: 500, cursor: 'pointer' }}>
          + Tichet Nou
        </button>
      </div>

      <div style={{ flex: 1, display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '20px', overflowX: 'auto' }}>
        {columns.map(col => (
          <div 
            key={col.id} 
            className="glass-panel" 
            style={{ padding: '16px', display: 'flex', flexDirection: 'column', gap: '16px' }}
            onDragOver={handleDragOver}
            onDrop={(e) => handleDrop(e, col.id)}
          >
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <div style={{ width: '12px', height: '12px', borderRadius: '50%', backgroundColor: col.color }} />
                <span style={{ fontWeight: 500 }}>{col.title}</span>
              </div>
              <MoreVertical size={16} color="var(--text-muted)" />
            </div>

            {loading ? (
               <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Se incarca...</div>
            ) : (
               tickets.filter(t => t.status === col.id).map(ticket => (
                <div 
                  key={ticket.id} 
                  draggable 
                  onDragStart={(e) => handleDragStart(e, ticket.id)}
                  className="glass-card" 
                  style={{ padding: '16px', borderRadius: '8px', cursor: 'grab', background: 'rgba(255,255,255,0.03)' }}
                >
                  <div style={{ fontSize: '0.75rem', padding: '2px 8px', background: `${col.color}20`, color: col.color, borderRadius: '999px', display: 'inline-block', marginBottom: '8px' }}>#{ticket.id} {ticket.priority}</div>
                  <div style={{ fontWeight: 500, marginBottom: '8px' }}>{ticket.title}</div>
                  <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', marginBottom: '8px' }}>{ticket.description}</div>
                </div>
              ))
            )}
            
            {!loading && tickets.filter(t => t.status === col.id).length === 0 && (
              <div style={{ padding: '20px', textAlign: 'center', color: 'var(--text-muted)', fontSize: '0.8rem', border: '1px dashed var(--border-color)', borderRadius: '8px' }}>Niciun tichet</div>
            )}
          </div>
        ))}
      </div>

      {isModalOpen && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 100 }}>
          <div className="glass-panel" style={{ padding: '24px', width: '400px', borderRadius: '12px', position: 'relative' }}>
            <button onClick={() => setIsModalOpen(false)} style={{ position: 'absolute', top: '16px', right: '16px', background: 'transparent', border: 'none', color: 'white', cursor: 'pointer' }}><X size={20}/></button>
            <h3 style={{ marginBottom: '16px', fontSize: '1.2rem', fontWeight: 600 }}>Adauga Tichet</h3>
            <form onSubmit={handleAdd} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                <label style={{ fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Titlu</label>
                <input required value={newTitle} onChange={e => setNewTitle(e.target.value)} type="text" style={{ background: 'var(--bg-glass)', border: '1px solid var(--border-color)', color: 'white', padding: '10px', borderRadius: '6px', outline: 'none' }} placeholder="Scurta descriere" />
              </div>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                <label style={{ fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Detalii Complete</label>
                <textarea required value={newDesc} onChange={e => setNewDesc(e.target.value)} style={{ background: 'var(--bg-glass)', border: '1px solid var(--border-color)', color: 'white', padding: '10px', borderRadius: '6px', outline: 'none', minHeight: '100px', resize: 'vertical' }} placeholder="Explica problema..." />
              </div>
              <button type="submit" style={{ background: 'var(--accent-primary)', border: 'none', color: 'white', padding: '12px', borderRadius: '6px', fontWeight: 600, cursor: 'pointer', marginTop: '8px' }}>Creaza Tichet</button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
