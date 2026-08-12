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
            if (lowerLine.startsWith('tip cabina:')) {
                order.tipCabina = line.split(':')[1].trim();
                // Infer productType from tipCabina
                if (order.tipCabina.startsWith('tipu')) {
                    order.productType = "batanta";
                }
            } else if (lowerLine.startsWith('dimensiuni:')) {
                order.dimensiuni = line.split(':')[1].trim();
            } else if (lowerLine.startsWith('sticla:')) {
                order.sticlaNume = line.split(':')[1].trim();
            } else if (lowerLine.startsWith('finisaj:')) {
                const finisaj = line.split(':')[1].trim().toLowerCase();
                if (finisaj.includes('lucios')) order.finisajID = 1;
                else if (finisaj.includes('mat')) order.finisajID = 2;
                else if (finisaj.includes('negru')) order.finisajID = 3;
                else if (finisaj.includes('auriu')) order.finisajID = 4;
            }
        }

        return order;
    }
}
