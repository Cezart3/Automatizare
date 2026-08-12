import { useEffect, useState } from 'react';
import axios from 'axios';
import { Search, Filter, Download } from 'lucide-react';

export const OrdersView = () => {
  const [orders, setOrders] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axios.get('http://localhost:3000/api/orders')
      .then(res => setOrders(res.data))
      .catch(err => console.error(err))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="animate-fade-in" style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
      <div style={{ marginBottom: '24px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h3 style={{ fontSize: '1.25rem', fontWeight: 600 }}>Comenzi</h3>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Gestionează toate comenzile și ofertele primite.</p>
        </div>
        <div style={{ display: 'flex', gap: '12px' }}>
          <button style={{ display: 'flex', alignItems: 'center', gap: '8px', background: 'transparent', border: '1px solid var(--border-color)', color: 'var(--text-primary)', padding: '8px 16px', borderRadius: '8px', cursor: 'pointer' }}>
            <Filter size={16} /> Filtrează
          </button>
          <button style={{ display: 'flex', alignItems: 'center', gap: '8px', background: 'var(--accent-primary)', border: 'none', color: '#fff', padding: '8px 16px', borderRadius: '8px', cursor: 'pointer', fontWeight: 500 }}>
            <Download size={16} /> Export CSV
          </button>
        </div>
      </div>

      <div className="glass-panel" style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
        <div style={{ padding: '16px 24px', borderBottom: '1px solid var(--border-color)', display: 'flex', gap: '16px' }}>
          <div style={{ flex: 1, position: 'relative' }}>
            <Search size={18} style={{ position: 'absolute', left: '12px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
            <input type="text" placeholder="Caută comandă, client, sau email..." style={{ width: '100%', padding: '10px 12px 10px 40px', background: 'rgba(255,255,255,0.03)', border: '1px solid var(--border-color)', borderRadius: '8px', color: 'var(--text-primary)', outline: 'none' }} />
          </div>
        </div>
        
        <div style={{ flex: 1, overflow: 'auto' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
            <thead>
              <tr style={{ borderBottom: '1px solid var(--border-color)', color: 'var(--text-muted)', position: 'sticky', top: 0, background: 'rgba(15, 23, 42, 0.95)', backdropFilter: 'blur(10px)' }}>
                <th style={{ padding: '16px 24px', fontWeight: 500 }}>Nr. Comandă</th>
                <th style={{ padding: '16px 24px', fontWeight: 500 }}>Client</th>
                <th style={{ padding: '16px 24px', fontWeight: 500 }}>Detalii (Extras automat)</th>
                <th style={{ padding: '16px 24px', fontWeight: 500 }}>Dată</th>
                <th style={{ padding: '16px 24px', fontWeight: 500 }}>Status</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan={5} style={{ padding: '32px', textAlign: 'center', color: 'var(--text-muted)' }}>Se încarcă comenzile...</td>
                </tr>
              ) : orders.length === 0 ? (
                <tr>
                  <td colSpan={5} style={{ padding: '32px', textAlign: 'center', color: 'var(--text-muted)' }}>Nicio comandă găsită în baza de date. Trimite un e-mail pentru a testa.</td>
                </tr>
              ) : (
                orders.map(order => (
                  <tr key={order.id} style={{ borderBottom: '1px solid rgba(255,255,255,0.05)', transition: 'background 0.2s ease', cursor: 'pointer' }}>
                    <td style={{ padding: '16px 24px', fontWeight: 500 }}>{order.orderNumber}</td>
                    <td style={{ padding: '16px 24px' }}>
                      <div style={{ fontWeight: 500 }}>{order.customer?.name}</div>
                      <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{order.customer?.email}</div>
                    </td>
                    <td style={{ padding: '16px 24px', maxWidth: '300px', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{order.details}</td>
                    <td style={{ padding: '16px 24px', color: 'var(--text-secondary)' }}>{new Date(order.createdAt).toLocaleDateString('ro-RO')}</td>
                    <td style={{ padding: '16px 24px' }}>
                      <span style={{
                        padding: '4px 8px', borderRadius: '12px', fontSize: '0.75rem', fontWeight: 500,
                        background: order.status === 'COMPLETED' ? 'rgba(16, 185, 129, 0.1)' : 'rgba(245, 158, 11, 0.1)',
                        color: order.status === 'COMPLETED' ? 'var(--success-color)' : 'var(--warning-color)'
                      }}>
                        {order.status || 'PENDING'}
                      </span>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
