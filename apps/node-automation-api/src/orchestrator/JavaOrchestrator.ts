import { exec } from 'child_process';
import * as fs from 'fs';
import * as path from 'path';

export class JavaOrchestrator {
    private static JAR_PATH = path.resolve(__dirname, '../../../../java-configurator/target/ShowerConfiguratoir-1.0-SNAPSHOT-jar-with-dependencies.jar');

    public static async run(orderPayload: any): Promise<string> {
        return new Promise((resolve, reject) => {
            const timestamp = Date.now();
            const jsonPath = path.resolve(__dirname, `../../temp/order_${timestamp}.json`);
            const outPdfPath = path.resolve(__dirname, `../../temp/result_${timestamp}.pdf`);

            // Ensure temp directory exists
            const tempDir = path.dirname(jsonPath);
            if (!fs.existsSync(tempDir)) {
                fs.mkdirSync(tempDir, { recursive: true });
            }

            // Write JSON payload
            fs.writeFileSync(jsonPath, JSON.stringify(orderPayload, null, 2));

            const command = `java -jar "${this.JAR_PATH}" -h -j "${jsonPath}" -o "${outPdfPath}"`;
            console.log(`Executing: ${command}`);

            exec(command, (error, stdout, stderr) => {
                if (error) {
                    console.error(`Exec error: ${error}`);
                    reject(error);
                    return;
                }

                // Check if PDF was generated successfully by reading stdout for the marker
                if (stdout.includes('PDF_GENERATED_AT')) {
                    resolve(outPdfPath);
                } else {
                    console.error(`Standard output: ${stdout}`);
                    console.error(`Standard error: ${stderr}`);
                    reject(new Error("PDF generation failed or marker not found in output."));
                }
            });
        });
    }
}
