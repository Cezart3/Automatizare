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

app.post('/api/appointments', async (req, res) => {
  try {
    const data = req.body;
    const appointment = await prisma.appointment.create({
      data: {
        title: data.title,
        date: new Date(data.date),
        location: data.location || '',
      }
    });
    res.json(appointment);
  } catch (err) {
    res.status(500).json({ error: 'Failed to create appointment' });
  }
});

app.post('/api/tickets', async (req, res) => {
  try {
    const ticket = await prisma.ticket.create({ data: req.body });
    res.json(ticket);
  } catch (err) {
    res.status(500).json({ error: 'Failed to create ticket' });
  }
});

app.patch('/api/tickets/:id', async (req, res) => {
  try {
    const ticket = await prisma.ticket.update({
      where: { id: parseInt(req.params.id) },
      data: req.body
    });
    res.json(ticket);
  } catch (err) {
    res.status(500).json({ error: 'Failed to update ticket' });
  }
});

app.get('/api/stats', async (req, res) => {
  try {
    const totalOrders = await prisma.order.count();
    const completedOrders = await prisma.order.count({ where: { status: 'COMPLETED' } });
    const successRate = totalOrders > 0 ? Math.round((completedOrders / totalOrders) * 100) : 100;
    
    // sum totalAmount
    const agg = await prisma.order.aggregate({ _sum: { totalAmount: true } });
    const revenue = agg._sum.totalAmount || 0;
    
    res.json({
      totalOrders,
      successRate,
      revenueGenerated: `€${revenue.toLocaleString()}`
    });
  } catch (err) {
    res.status(500).json({ error: 'Failed to fetch stats' });
  }
});

app.get('/api/settings', async (req, res) => {
  try {
    const settings = await prisma.setting.findMany();
    res.json(settings);
  } catch (err) {
    res.status(500).json({ error: 'Failed to fetch settings' });
  }
});

app.post('/api/settings', async (req, res) => {
  try {
    const { key, value } = req.body;
    const setting = await prisma.setting.upsert({
      where: { key },
      update: { value: JSON.stringify(value) },
      create: { key, value: JSON.stringify(value) }
    });
    res.json(setting);
  } catch (err) {
    res.status(500).json({ error: 'Failed to save setting' });
  }
});

export const startServer = (port: number = 3000) => {
  app.listen(port, () => {
    console.log(`[Express] Server is running on http://localhost:${port}`);
  });
};
