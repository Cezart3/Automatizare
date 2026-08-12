import * as dotenv from 'dotenv';
import { ImapListener } from './imap/ImapListener';
import { startServer } from './server';

dotenv.config();

async function bootstrap() {
  console.log('Starting OmniBusiness Automation Ecosystem...');

  // Start REST API
  startServer(3000);

  // Initialize and start IMAP Listener for email automation
  const listener = new ImapListener();
  listener.start();
}

bootstrap().catch(console.error);
