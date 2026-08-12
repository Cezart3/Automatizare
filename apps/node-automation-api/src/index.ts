import * as dotenv from 'dotenv';
import { ImapListener } from './imap/ImapListener';

dotenv.config();

console.log("Starting Node Automation API...");

const listener = new ImapListener();
listener.start();
