"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.ImapListener = void 0;
const imap_simple_1 = __importDefault(require("imap-simple"));
const mailparser_1 = require("mailparser");
const EmailParser_1 = require("../parser/EmailParser");
const JavaOrchestrator_1 = require("../orchestrator/JavaOrchestrator");
const SmtpMailer_1 = require("../mailer/SmtpMailer");
class ImapListener {
    connection = null;
    async start() {
        const config = {
            imap: {
                user: process.env.EMAIL_USER,
                password: process.env.EMAIL_PASS,
                host: process.env.IMAP_HOST,
                port: parseInt(process.env.IMAP_PORT || "993"),
                tls: process.env.IMAP_TLS === "true",
                authTimeout: 3000
            }
        };
        try {
            this.connection = await imap_simple_1.default.connect(config);
            await this.connection.openBox('INBOX');
            console.log('[IMAP] Connected and listening for emails...');
            this.connection.on('mail', async (numNewMail) => {
                console.log(`[IMAP] ${numNewMail} new email(s) received`);
                await this.fetchNewEmails();
            });
            // Initial fetch in case there are unread emails
            await this.fetchNewEmails();
        }
        catch (error) {
            console.error('[IMAP] Connection error:', error);
            setTimeout(() => this.start(), 10000); // Reconnect logic
        }
    }
    async fetchNewEmails() {
        if (!this.connection)
            return;
        const searchCriteria = ['UNSEEN'];
        const fetchOptions = { bodies: ['HEADER', 'TEXT'], struct: true, markSeen: true };
        try {
            const messages = await this.connection.search(searchCriteria, fetchOptions);
            for (const item of messages) {
                const allParts = imap_simple_1.default.getParts(item.attributes.struct);
                const textPart = allParts.find(part => part.which === 'TEXT');
                const headerPart = item.parts.find(part => part.which === 'HEADER');
                const bodyPart = item.parts.find(part => part.which === 'TEXT' || (textPart && part.which === textPart.which));
                if (headerPart && headerPart.body && bodyPart && bodyPart.body) {
                    const parsed = await (0, mailparser_1.simpleParser)(headerPart.body + bodyPart.body);
                    const subject = parsed.subject || "Comanda";
                    const from = parsed.from?.value[0]?.address;
                    const text = parsed.text || "";
                    if (from) {
                        console.log(`[IMAP] Processing email from ${from}: ${subject}`);
                        await this.processEmail(from, subject, text);
                    }
                }
            }
        }
        catch (error) {
            console.error('[IMAP] Error fetching emails:', error);
        }
    }
    async processEmail(from, subject, text) {
        try {
            // 1. Parse text
            const orderPayload = EmailParser_1.EmailParser.parse(text);
            // 2. Orchestrate Java App
            console.log(`[Orchestrator] Generating PDF for ${from}...`);
            const pdfPath = await JavaOrchestrator_1.JavaOrchestrator.run(orderPayload);
            // 3. Send email back
            console.log(`[Mailer] Sending PDF to ${from}...`);
            await SmtpMailer_1.SmtpMailer.sendPdf(from, subject, pdfPath);
            console.log(`[Workflow] Completed for ${from}`);
        }
        catch (error) {
            console.error(`[Workflow] Failed for ${from}:`, error);
        }
    }
}
exports.ImapListener = ImapListener;
//# sourceMappingURL=ImapListener.js.map