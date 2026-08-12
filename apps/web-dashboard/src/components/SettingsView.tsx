import React, { useState } from 'react';
import { Settings2, Sliders, Shield, Bell, Palette, Globe, Save } from 'lucide-react';

export const SettingsView: React.FC = () => {
  const [activeSettingsTab, setActiveSettingsTab] = useState('general');
  const [saving, setSaving] = useState(false);

  const handleSave = () => {
    setSaving(true);
    setTimeout(() => setSaving(false), 1000);
  };

  return (
    <div className="animate-fade-in" style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
      <div style={{ marginBottom: '24px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h3 style={{ fontSize: '1.25rem', fontWeight: 600 }}>Setări ERP & Configurare</h3>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Personalizează șablonul universal pentru nișa ta</p>
        </div>
        <button 
          onClick={handleSave}
          style={{ 
            display: 'flex', alignItems: 'center', gap: '8px',
            background: 'var(--accent-primary)', border: 'none', color: 'white', 
            padding: '8px 16px', borderRadius: '6px', fontWeight: 500, cursor: 'pointer' 
          }}
        >
          {saving ? <span className="animate-spin">⏳</span> : <Save size={16} />} 
          {saving ? 'Se salvează...' : 'Salvează Modificările'}
        </button>
      </div>

      <div style={{ display: 'flex', gap: '32px', flex: 1, alignItems: 'flex-start' }}>
        {/* Settings Sidebar */}
        <div className="glass-panel" style={{ width: '250px', display: 'flex', flexDirection: 'column', padding: '8px' }}>
          <SettingsNavItem active={activeSettingsTab === 'general'} onClick={() => setActiveSettingsTab('general')} icon={<Settings2 />} label="General" />
          <SettingsNavItem active={activeSettingsTab === 'modules'} onClick={() => setActiveSettingsTab('modules')} icon={<Sliders />} label="Module Active" />
          <SettingsNavItem active={activeSettingsTab === 'appearance'} onClick={() => setActiveSettingsTab('appearance')} icon={<Palette />} label="Personalizare B2B" />
          <SettingsNavItem active={activeSettingsTab === 'notifications'} onClick={() => setActiveSettingsTab('notifications')} icon={<Bell />} label="Notificări" />
          <SettingsNavItem active={activeSettingsTab === 'security'} onClick={() => setActiveSettingsTab('security')} icon={<Shield />} label="Securitate & Roluri" />
          <SettingsNavItem active={activeSettingsTab === 'integrations'} onClick={() => setActiveSettingsTab('integrations')} icon={<Globe />} label="Integrări" />
        </div>

        {/* Settings Content */}
        <div className="glass-panel" style={{ flex: 1, padding: '32px', minHeight: '500px' }}>
          {activeSettingsTab === 'general' && (
            <div className="animate-fade-in">
              <h4 style={{ fontSize: '1.1rem', fontWeight: 500, marginBottom: '24px', borderBottom: '1px solid var(--border-color)', paddingBottom: '12px' }}>Profilul Companiei</h4>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '20px', maxWidth: '500px' }}>
                <InputGroup label="Nume Companie" defaultValue="SC OmniBusiness SRL" />
                <InputGroup label="Domeniu / Nișă" defaultValue="Producție și Servicii" />
                <InputGroup label="CUI / CIF" defaultValue="RO12345678" />
                <InputGroup label="Adresă" defaultValue="Str. Principală 1, București" />
              </div>
            </div>
          )}

          {activeSettingsTab === 'modules' && (
            <div className="animate-fade-in">
              <h4 style={{ fontSize: '1.1rem', fontWeight: 500, marginBottom: '24px', borderBottom: '1px solid var(--border-color)', paddingBottom: '12px' }}>Configurator Module B2B</h4>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                <ToggleSwitch label="Calendar & Programări" description="Permite programarea ședințelor și măsurătorilor pe teren." enabled={true} />
                <ToggleSwitch label="Comenzi & Facturare (ERP Lite)" description="Activează ciclul de vânzări B2B: Ofertă -> Comandă -> Factură." enabled={true} />
                <ToggleSwitch label="Customer Support Ticketing" description="Kanban board pentru tichete de suport tehnic și reclamații." enabled={true} />
                <ToggleSwitch label="Configurator Parametric (Nișă)" description="Exclusiv pentru produse configurabile (ex: Cabine Duș, Mobilă)." enabled={false} />
                <ToggleSwitch label="Portal B2B Clienți" description="Oferă acces clienților pentru a-și vedea statusul comenzilor." enabled={false} />
              </div>
            </div>
          )}

          {/* Alte taburi ar putea fi implementate similar */}
          {['appearance', 'notifications', 'security', 'integrations'].includes(activeSettingsTab) && (
            <div className="animate-fade-in" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: '100%', color: 'var(--text-muted)' }}>
              <Settings2 size={48} style={{ marginBottom: '16px', opacity: 0.5 }} />
              <p>Acest modul este în curs de dezvoltare.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

// Sub-components

const SettingsNavItem = ({ icon, label, active, onClick }: { icon: React.ReactNode, label: string, active: boolean, onClick: () => void }) => (
  <button 
    onClick={onClick}
    style={{
      display: 'flex', alignItems: 'center', gap: '12px', padding: '12px 16px',
      background: active ? 'rgba(59, 130, 246, 0.15)' : 'transparent',
      color: active ? 'var(--accent-primary)' : 'var(--text-secondary)',
      border: 'none', borderRadius: '6px', cursor: 'pointer', textAlign: 'left',
      fontWeight: active ? 500 : 400, transition: 'all 0.2s ease'
    }}
  >
    {React.cloneElement(icon as React.ReactElement<any>, { size: 18 })}
    {label}
  </button>
);

const InputGroup = ({ label, defaultValue }: { label: string, defaultValue: string }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
    <label style={{ fontSize: '0.875rem', fontWeight: 500, color: 'var(--text-secondary)' }}>{label}</label>
    <input 
      type="text" 
      defaultValue={defaultValue}
      style={{ 
        background: 'var(--bg-glass)', border: '1px solid var(--border-color)', 
        color: 'var(--text-primary)', padding: '10px 12px', borderRadius: '6px', outline: 'none' 
      }} 
    />
  </div>
);

const ToggleSwitch = ({ label, description, enabled }: { label: string, description: string, enabled: boolean }) => {
  const [isOn, setIsOn] = useState(enabled);
  return (
    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '16px', background: 'rgba(255,255,255,0.02)', borderRadius: '8px', border: '1px solid var(--border-color)' }}>
      <div>
        <div style={{ fontWeight: 500, marginBottom: '4px' }}>{label}</div>
        <div style={{ fontSize: '0.875rem', color: 'var(--text-muted)' }}>{description}</div>
      </div>
      <button 
        onClick={() => setIsOn(!isOn)}
        style={{
          width: '44px', height: '24px', borderRadius: '12px', border: 'none', cursor: 'pointer',
          background: isOn ? 'var(--accent-primary)' : 'var(--text-muted)',
          position: 'relative', transition: 'background 0.3s ease'
        }}
      >
        <div style={{
          width: '18px', height: '18px', borderRadius: '50%', background: 'white',
          position: 'absolute', top: '3px', left: isOn ? '23px' : '3px', transition: 'left 0.3s ease'
        }} />
      </button>
    </div>
  );
};
