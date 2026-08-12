export interface ParsedEmailData {
  customerName?: string;
  customerEmail?: string;
  orderType?: string;
  dimensions?: string;
  details?: string;
}

export class RegexParser {
  static parseEmailBody(subject: string, body: string, senderEmail: string): ParsedEmailData {
    const data: ParsedEmailData = {
      customerEmail: senderEmail,
      orderType: 'Comandă Generală',
      details: ''
    };

    // Extract Name
    const nameMatch = body.match(/(?:Nume Client|Client|Nume):\s*([^\n\r]+)/i);
    if (nameMatch) {
      data.customerName = nameMatch[1].trim();
    }

    // Extract Dimensions
    const dimMatch = body.match(/(?:Dimensiuni|Marime|L x H|L[aă]țime x [IÎî]n[aă]l[tț]ime):\s*([\d]+[xX*][\d]+)/i);
    if (dimMatch) {
      data.dimensions = dimMatch[1].trim();
    }

    // Extract generic details (everything after "Detalii:")
    const detailsMatch = body.match(/(?:Detalii|Descriere|Observa[tț]ii):\s*([\s\S]+)/i);
    if (detailsMatch) {
      data.details = detailsMatch[1].trim();
    } else {
      // fallback to subject if no details found
      data.details = subject;
    }

    // Try to classify order type from subject
    if (subject.toLowerCase().includes('cabina') || subject.toLowerCase().includes('cabină')) {
      data.orderType = 'Cabină de duș';
    } else if (subject.toLowerCase().includes('sticla') || subject.toLowerCase().includes('sticlă')) {
      data.orderType = 'Sticlă custom';
    } else if (subject.toLowerCase().includes('suport') || subject.toLowerCase().includes('problema')) {
      data.orderType = 'Tichet Suport';
    }

    return data;
  }
}
