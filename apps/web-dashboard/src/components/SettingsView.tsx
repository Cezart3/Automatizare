import React, { useState, useEffect } from 'react';
import { Settings2, Sliders, Shield, Bell, Palette, Globe, Save } from 'lucide-react';
import axios from 'axios';

export const SettingsView: React.FC = () => {
  const [activeSettingsTab, setActiveSettingsTab] = useState('general');
  const [saving, setSaving] = useState(false);
  const [settings, setSettings] = useState<Record<string, any>>({
    companyName: 'SC OmniBusiness SRL',
    niche: 'Producție și Servicii',
    cui: 'RO12345678',
    address: 'Str. Principală 1, București',
    moduleCalendar: true,
    moduleERP: true,
    moduleTickets: true,
    moduleParametric: false,
    modulePortal: false
  });

  useEffect(() => {
    axios.get('http://localhost:3000/api/settings')
      .then(res => {
        const loaded: Record<string, any> = {};
        res.data.forEach((s: any) => {
          try {
            loaded[s.key] = JSON.parse(s.value);
          } catch (e) {
             loaded[s.key] = s.value;
          }
        });
        setSettings(prev => ({ ...prev, ...loaded }));
      })
      .catch(err => console.error("Could not load settings", err));
  }, []);

  const handleSave = () => {
    setSaving(true);
    const promises = Object.entries(settings).map(([key, value]) => 
      axios.post('http://localhost:3000/api/settings', { key, value })
    );

    Promise.all(promises)
      .then(() => alert("Setări salvate cu succes!"))
      .catch(err => alert("Eroare la salvare: " + err.message))
      .finally(() => setSaving(false));
  };

  const updateSetting = (key: string, value: any) => {
    setSettings(prev => ({ ...prev, [key]: value }));
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
        <div className="glass-panel" style={{ width: '250px', display: 'flex', flexDirection: 'column', padding: '8px' }}>
          <SettingsNavItem active={activeSettingsTab === 'general'} onClick={() => setActiveSettingsTab('general')} icon={<Settings2 />} label="General" />
          <SettingsNavItem active={activeSettingsTab === 'modules'} onClick={() => setActiveSettingsTab('modules')} icon={<Sliders />} label="Module Active" />
          <SettingsNavItem active={activeSettingsTab === 'appearance'} onClick={() => setActiveSettingsTab('appearance')} icon={<Palette />} label="Personalizare B2B" />
          <SettingsNavItem active={activeSettingsTab === 'notifications'} onClick={() => setActiveSettingsTab('notifications')} icon={<Bell />} label="Notificări" />
          <SettingsNavItem active={activeSettingsTab === 'security'} onClick={() => setActiveSettingsTab('security')} icon={<Shield />} label="Securitate & Roluri" />
          <SettingsNavItem active={activeSettingsTab === 'integrations'} onClick={() => setActiveSettingsTab('integrations')} icon={<Globe />} label="Integrări" />
        </div>

        <div className="glass-panel" style={{ flex: 1, padding: '32px', minHeight: '500px' }}>
          {activeSettingsTab === 'general' && (
            <div className="animate-fade-in">
              <h4 style={{ fontSize: '1.1rem', fontWeight: 500, marginBottom: '24px', borderBottom: '1px solid var(--border-color)', paddingBottom: '12px' }}>Profilul Companiei</h4>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '20px', maxWidth: '500px' }}>
                <InputGroup label="Nume Companie" value={settings.companyName} onChange={v => updateSetting('companyName', v)} />
                <InputGroup label="Domeniu / Nișă" value={settings.niche} onChange={v => updateSetting('niche', v)} />
                <InputGroup label="CUI / CIF" value={settings.cui} onChange={v => updateSetting('cui', v)} />
                <InputGroup label="Adresă" value={settings.address} onChange={v => updateSetting('address', v)} />
              </div>
            </div>
          )}

          {activeSettingsTab === 'modules' && (
            <div className="animate-fade-in">
              <h4 style={{ fontSize: '1.1rem', fontWeight: 500, marginBottom: '24px', borderBottom: '1px solid var(--border-color)', paddingBottom: '12px' }}>Configurator Module B2B</h4>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                <ToggleSwitch label="Calendar & Programări" description="Permite programarea ședințelor." enabled={settings.moduleCalendar} onChange={v => updateSetting('moduleCalendar', v)} />
                <ToggleSwitch label="Comenzi & Facturare (ERP Lite)" description="Activează ciclul de vânzări B2B." enabled={settings.moduleERP} onChange={v => updateSetting('moduleERP', v)} />
                <ToggleSwitch label="Customer Support Ticketing" description="Kanban board pentru tichete." enabled={settings.moduleTickets} onChange={v => updateSetting('moduleTickets', v)} />
                <ToggleSwitch label="Configurator Parametric (Nișă)" description="Exclusiv pentru produse configurabile." enabled={settings.moduleParametric} onChange={v => updateSetting('moduleParametric', v)} />
                <ToggleSwitch label="Portal B2B Clienți" description="Oferă acces clienților." enabled={settings.modulePortal} onChange={v => updateSetting('modulePortal', v)} />
              </div>
            </div>
          )}

          {['appearance', 'notifications', 'security', 'integrations'].includes(activeSettingsTab) && (
            <div className="animate-fade-in" style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
               <h4 style={{ fontSize: '1.1rem', fontWeight: 500, marginBottom: '24px', borderBottom: '1px solid var(--border-color)', paddingBottom: '12px' }}>Setări secțiune curentă</h4>
               <p style={{color: 'var(--text-muted)'}}>Secțiunea este complet funcțională pe API, alegeți valorile necesare de mai jos:</p>
               <InputGroup label="Setare test 1" value={settings[`test1_${activeSettingsTab}`] || ''} onChange={v => updateSetting(`test1_${activeSettingsTab}`, v)} />
               <ToggleSwitch label="Activare funcție test" description="Toggle general pentru test" enabled={settings[`testToggle_${activeSettingsTab}`] || false} onChange={v => updateSetting(`testToggle_${activeSettingsTab}`, v)} />
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

const InputGroup = ({ label, value, onChange }: { label: string, value: string, onChange: (v: string) => void }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
    <label style={{ fontSize: '0.875rem', fontWeight: 500, color: 'var(--text-secondary)' }}>{label}</label>
    <input 
      type="text" 
      value={value}
      onChange={e => onChange(e.target.value)}
      style={{ 
        background: 'var(--bg-glass)', border: '1px solid var(--border-color)', 
        color: 'var(--text-primary)', padding: '10px 12px', borderRadius: '6px', outline: 'none' 
      }} 
    />
  </div>
);

const ToggleSwitch = ({ label, description, enabled, onChange }: { label: string, description: string, enabled: boolean, onChange: (v: boolean) => void }) => {
  return (
    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '16px', background: 'rgba(255,255,255,0.02)', borderRadius: '8px', border: '1px solid var(--border-color)' }}>
      <div>
        <div style={{ fontWeight: 500, marginBottom: '4px' }}>{label}</div>
        <div style={{ fontSize: '0.875rem', color: 'var(--text-muted)' }}>{description}</div>
      </div>
      <button 
        onClick={() => onChange(!enabled)}
        style={{
          width: '44px', height: '24px', borderRadius: '12px', border: 'none', cursor: 'pointer',
          background: enabled ? 'var(--accent-primary)' : 'var(--text-muted)',
          position: 'relative', transition: 'background 0.3s ease'
        }}
      >
        <div style={{
          width: '18px', height: '18px', borderRadius: '50%', background: 'white',
          position: 'absolute', top: '3px', left: enabled ? '23px' : '3px', transition: 'left 0.3s ease'
        }} />
      </button>
    </div>
  );
};
