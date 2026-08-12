"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.SmtpMailer = void 0;
const nodemailer_1 = __importDefault(require("nodemailer"));
class SmtpMailer {
    static async sendPdf(to, subject, pdfPath) {
        const transporter = nodemailer_1.default.createTransport({
            host: process.env.SMTP_HOST,
            port: parseInt(process.env.SMTP_PORT || "465"),
            secure: process.env.SMTP_SECURE === "true",
            auth: {
                user: process.env.EMAIL_USER,
                pass: process.env.EMAIL_PASS
            }
        });
        const mailOptions = {
            from: process.env.EMAIL_USER,
            to: to,
            subject: `Re: ${subject} - Oferta Generata Automat`,
            text: `Buna ziua,\n\nVa atsam oferta dumneavoastra in format PDF, generata automat in baza detaliilor din email.\n\nO zi frumoasa!`,
            attachments: [
                {
                    filename: 'Oferta.pdf',
                    path: pdfPath
                }
            ]
        };
        const info = await transporter.sendMail(mailOptions);
        console.log(`[SMTP] Sent PDF to ${to}: ${info.messageId}`);
    }
}
exports.SmtpMailer = SmtpMailer;
//# sourceMappingURL=SmtpMailer.js.map