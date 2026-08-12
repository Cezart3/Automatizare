import imaps from 'imap-simple';
import { simpleParser } from 'mailparser';
import { EmailParser } from '../parser/EmailParser';
import { JavaOrchestrator } from '../orchestrator/JavaOrchestrator';
import { SmtpMailer } from '../mailer/SmtpMailer';

export class ImapListener {
    private connection: imaps.ImapSimple | null = null;

    public async start() {
        const config = {
            imap: {
                user: process.env.EMAIL_USER as string,
                password: process.env.EMAIL_PASS as string,
                host: process.env.IMAP_HOST as string,
                port: parseInt(process.env.IMAP_PORT || "993"),
                tls: process.env.IMAP_TLS === "true",
                authTimeout: 3000
            }
        };

        try {
            this.connection = await imaps.connect(config);
            await this.connection.openBox('INBOX');
            console.log('[IMAP] Connected and listening for emails...');

            this.connection.on('mail', async (numNewMail: number) => {
                console.log(`[IMAP] ${numNewMail} new email(s) received`);
                await this.fetchNewEmails();
            });

            // Initial fetch in case there are unread emails
            await this.fetchNewEmails();

        } catch (error) {
            console.error('[IMAP] Connection error:', error);
            setTimeout(() => this.start(), 10000); // Reconnect logic
        }
    }

    private async fetchNewEmails() {
        if (!this.connection) return;

        const searchCriteria = ['UNSEEN'];
        const fetchOptions = { bodies: ['HEADER', 'TEXT'], struct: true, markSeen: true };
        
        try {
            const messages = await this.connection.search(searchCriteria, fetchOptions);
            
            for (const item of messages) {
                const allParts = imaps.getParts(item.attributes.struct);
                const textPart = allParts.find(part => part.which === 'TEXT');
                
                const headerPart = item.parts.find(part => part.which === 'HEADER');
                const bodyPart = item.parts.find(part => part.which === 'TEXT' || (textPart && part.which === textPart.which));
                
                if (headerPart && headerPart.body && bodyPart && bodyPart.body) {
                    const parsed = await simpleParser(headerPart.body + bodyPart.body);
                    const subject = parsed.subject || "Comanda";
                    const from = parsed.from?.value[0]?.address;
                    const text = parsed.text || "";

                    if (from) {
                        console.log(`[IMAP] Processing email from ${from}: ${subject}`);
                        await this.processEmail(from, subject, text);
                    }
                }
            }
        } catch (error) {
            console.error('[IMAP] Error fetching emails:', error);
        }
    }

    private async processEmail(from: string, subject: string, text: string) {
        try {
            // 1. Parse text
            const orderPayload = EmailParser.parse(text);
            
            // 2. Orchestrate Java App
            console.log(`[Orchestrator] Generating PDF for ${from}...`);
            const pdfPath = await JavaOrchestrator.run(orderPayload);
            
            // 3. Send email back
            console.log(`[Mailer] Sending PDF to ${from}...`);
            await SmtpMailer.sendPdf(from, subject, pdfPath);
            console.log(`[Workflow] Completed for ${from}`);
            
        } catch (error) {
            console.error(`[Workflow] Failed for ${from}:`, error);
        }
    }
}
