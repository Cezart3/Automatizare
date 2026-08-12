import { RegexParser } from './parser/RegexParser';
import { prisma } from './prisma';

async function test() {
    const text = `
Bună ziua,
Doresc să plasez o comandă pentru o cabină de duș.
Nume Client: Ion Popescu
Dimensiuni: 900x2000
Detalii: Feronerie neagră mat, sticlă clară tratată anticalcar.
Vă mulțumesc!
    `;
    const from = 'ion.popescu@yahoo.com';
    const subject = 'Comanda Cabina de dus';

    console.log("Parsing email...");
    const parsedData = RegexParser.parseEmailBody(subject, text, from);
    console.log("Parsed Data:", parsedData);

    const customerEmail = parsedData.customerEmail || from;
    const customerName = parsedData.customerName || customerEmail.split('@')[0];

    let customer = await prisma.customer.findUnique({ where: { email: customerEmail }});
    if (!customer) {
        customer = await prisma.customer.create({
            data: {
                name: customerName,
                email: customerEmail,
                phone: ''
            }
        });
    }

    const order = await prisma.order.create({
        data: {
            orderNumber: `ORD-${Date.now()}`,
            status: 'PENDING',
            details: `[${parsedData.orderType}] Dimensiuni: ${parsedData.dimensions || 'N/A'}. ${parsedData.details}`,
            customerId: customer.id
        }
    });

    console.log("Created Order:", order);
}

test().catch(console.error).finally(() => prisma.$disconnect());
