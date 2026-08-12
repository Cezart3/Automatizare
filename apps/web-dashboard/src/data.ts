export const mockOrders = [
  {
    id: "ORD-2026-001",
    customerEmail: "client@yahoo.com",
    subject: "Comanda Cabina Tip 1",
    date: "2026-08-12T10:30:00Z",
    status: "success",
    pdfUrl: "#",
    details: "900x2000, Feronerie Inox, Finisaj Lucios"
  },
  {
    id: "ORD-2026-002",
    customerEmail: "office@construct.ro",
    subject: "Cerere oferta paravan sticla",
    date: "2026-08-12T11:45:00Z",
    status: "success",
    pdfUrl: "#",
    details: "1200x2000, Feronerie Neagra, Sticla Fumurie"
  },
  {
    id: "ORD-2026-003",
    customerEmail: "persoana.fizica@gmail.com",
    subject: "Re: Detalii comanda",
    date: "2026-08-12T14:15:00Z",
    status: "failed",
    pdfUrl: null,
    details: "Eroare la parsarea dimensiunilor. Format necunoscut."
  },
  {
    id: "ORD-2026-004",
    customerEmail: "achizitii@dezvoltator.ro",
    subject: "Comanda 5 cabine tip 3",
    date: "2026-08-12T15:20:00Z",
    status: "processing",
    pdfUrl: null,
    details: "Extragere date in curs..."
  }
];

export const mockStats = {
  totalOrders: 142,
  successRate: 94.5,
  revenueGenerated: "€12,450",
  activeAutomations: 3
};
