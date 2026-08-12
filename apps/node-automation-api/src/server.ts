import express from 'express';
import cors from 'cors';
import { prisma } from './prisma';

const app = express();
app.use(cors());
app.use(express.json());

// --- ORDERS API ---
app.get('/api/orders', async (req, res) => {
  try {
    const orders = await prisma.order.findMany({ include: { customer: true } });
    res.json(orders);
  } catch (err) {
    res.status(500).json({ error: 'Failed to fetch orders' });
  }
});

// --- TICKETS API ---
app.get('/api/tickets', async (req, res) => {
  try {
    const tickets = await prisma.ticket.findMany({ include: { customer: true } });
    res.json(tickets);
  } catch (err) {
    res.status(500).json({ error: 'Failed to fetch tickets' });
  }
});

// --- APPOINTMENTS API ---
app.get('/api/appointments', async (req, res) => {
  try {
    const appointments = await prisma.appointment.findMany({ include: { customer: true } });
    res.json(appointments);
  } catch (err) {
    res.status(500).json({ error: 'Failed to fetch appointments' });
  }
});

export const startServer = (port: number = 3000) => {
  app.listen(port, () => {
    console.log(`[Express] Server is running on http://localhost:${port}`);
  });
};
