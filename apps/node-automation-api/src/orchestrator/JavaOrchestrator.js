"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
exports.JavaOrchestrator = void 0;
const child_process_1 = require("child_process");
const fs = __importStar(require("fs"));
const path = __importStar(require("path"));
class JavaOrchestrator {
    static JAR_PATH = path.resolve(__dirname, '../../../../java-configurator/target/ShowerConfiguratoir-1.0-SNAPSHOT-jar-with-dependencies.jar');
    static async run(orderPayload) {
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
            (0, child_process_1.exec)(command, (error, stdout, stderr) => {
                if (error) {
                    console.error(`Exec error: ${error}`);
                    reject(error);
                    return;
                }
                // Check if PDF was generated successfully by reading stdout for the marker
                if (stdout.includes('PDF_GENERATED_AT')) {
                    resolve(outPdfPath);
                }
                else {
                    console.error(`Standard output: ${stdout}`);
                    console.error(`Standard error: ${stderr}`);
                    reject(new Error("PDF generation failed or marker not found in output."));
                }
            });
        });
    }
}
exports.JavaOrchestrator = JavaOrchestrator;
//# sourceMappingURL=JavaOrchestrator.js.map