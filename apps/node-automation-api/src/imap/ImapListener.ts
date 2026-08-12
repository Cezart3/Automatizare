import { ImapFlow } from 'imapflow';
import { simpleParser } from 'mailparser';
import { RegexParser } from '../parser/RegexParser';
import { prisma } from '../prisma';

export class ImapListener {
    private client: ImapFlow;

    constructor() {
        this.client = new ImapFlow({
            host: process.env.IMAP_HOST || 'imap.gmail.com',
            port: parseInt(process.env.IMAP_PORT || '993'),
            secure: process.env.IMAP_TLS !== 'false',
            auth: {
                user: process.env.EMAIL_USER as string,
                pass: process.env.EMAIL_PASS as string
            },
            logger: false // set to true for debugging
        });
    }

    public async start() {
        try {
            await this.client.connect();
            console.log('[IMAP] Connected and listening for emails...');

            // Select and lock INBOX
            let lock = await this.client.getMailboxLock('INBOX');
            try {
                // Fetch initially
                await this.fetchUnseen();

                // Listen for new messages
                this.client.on('exists', async (data) => {
                    console.log(`[IMAP] Message count changed, fetching unseen...`);
                    await this.fetchUnseen();
                });
            } finally {
                lock.release();
            }

        } catch (error) {
            console.error('[IMAP] Connection error:', error);
            setTimeout(() => this.start(), 10000); // Reconnect logic
        }
    }

    private async fetchUnseen() {
        try {
            for await (const message of this.client.fetch({ seen: false }, { source: true })) {
                if (message.source) {
                    const parsed = await simpleParser(message.source);
                    const subject = parsed.subject || "Fără subiect";
                    const from = parsed.from?.value[0]?.address || "unknown@client.com";
                    const text = parsed.text || "";

                    console.log(`[IMAP] Processing email from ${from}: ${subject}`);
                    await this.processEmail(from, subject, text);

                    // Mark as seen
                    await this.client.messageFlagsAdd(message.seq.toString(), ['\\Seen']);
                }
            }
        } catch (error) {
            console.error('[IMAP] Error fetching unseen emails:', error);
        }
    }

    private async processEmail(from: string, subject: string, text: string) {
        try {
            // 1. Parse text via Regex
            const parsedData = RegexParser.parseEmailBody(subject, text, from);
            console.log('[Parser] Extracted Data:', parsedData);
            
            // 2. Determine Customer
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

            // 3. Save to DB
            const order = await prisma.order.create({
                data: {
                    orderNumber: `ORD-${Date.now()}`,
                    status: 'PENDING',
                    details: `[${parsedData.orderType}] Dimensiuni: ${parsedData.dimensions || 'N/A'}. ${parsedData.details}`,
                    customerId: customer.id
                }
            });
            
            console.log(`[DB] Created order ${order.orderNumber} for ${customerEmail}`);
            
        } catch (error) {
            console.error(`[Workflow] Failed to process email from ${from}:`, error);
        }
    }
}
