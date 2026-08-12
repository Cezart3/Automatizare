export class EmailParser {
    public static parse(emailBody: string): any {
        // Default values
        const order: any = {
            tipCabina: "tipu_1",
            productType: "batanta",
            material: "Inox",
            finisajID: 1,
            dimensiuni: "900x2000",
            sticlaNume: "Clara",
            sticlaGrosime: "8",
            sticlaTip: "Securizata",
            sticlaGaurire: "4-20",
            sticlaForma: "Dreptunghi",
            hardware: {},
            hardwareMulti: {},
            hardwareQuantities: {},
            sticlaFormaSablonMap: {},
            sticlaNumarGauririExtra: "0",
            sticlaNumarDecupajeExtra: "0",
            sticlaDecupajeExtra: "Fara"
        };

        const lines = emailBody.split('\n');

        for (const line of lines) {
            const lowerLine = line.toLowerCase().trim();
            const parts = line.split(':');
            if (parts.length < 2) continue;
            
            const value = parts.slice(1).join(':').trim();

            if (lowerLine.startsWith('tip cabina:')) {
                order.tipCabina = value;
                // Infer productType from tipCabina
                if (order.tipCabina.startsWith('tipu')) {
                    order.productType = "batanta";
                }
            } else if (lowerLine.startsWith('dimensiuni:')) {
                order.dimensiuni = value;
            } else if (lowerLine.startsWith('sticla:')) {
                order.sticlaNume = value;
            } else if (lowerLine.startsWith('finisaj:')) {
                const finisaj = value.toLowerCase();
                if (finisaj.includes('lucios')) order.finisajID = 1;
                else if (finisaj.includes('mat')) order.finisajID = 2;
                else if (finisaj.includes('negru')) order.finisajID = 3;
                else if (finisaj.includes('auriu')) order.finisajID = 4;
            }
        }

        return order;
    }
}
