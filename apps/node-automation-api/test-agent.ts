import axios from 'axios';

async function testAll() {
    const API_URL = 'http://localhost:3000/api';
    console.log("[Test Agent] Starting full ecosystem API test...");

    try {
        // 1. Test Stats
        console.log("-> Testing GET /stats");
        const statsRes = await axios.get(`${API_URL}/stats`);
        console.log("   Stats:", statsRes.data);
        
        // 2. Test Tickets
        console.log("-> Testing POST /tickets");
        const newTicket = await axios.post(`${API_URL}/tickets`, {
            title: 'Test Ticket Auto',
            description: 'Problema generata automat de agentul de test',
            status: 'TODO',
            priority: 'HIGH'
        });
        console.log("   Created Ticket ID:", newTicket.data.id);
        
        console.log(`-> Testing PATCH /tickets/${newTicket.data.id}`);
        const patchedTicket = await axios.patch(`${API_URL}/tickets/${newTicket.data.id}`, {
            status: 'IN_PROGRESS'
        });
        console.log("   Patched Ticket Status:", patchedTicket.data.status);

        console.log("-> Testing GET /tickets");
        const ticketsRes = await axios.get(`${API_URL}/tickets`);
        console.log(`   Fetched ${ticketsRes.data.length} tickets`);

        // 3. Test Appointments
        console.log("-> Testing POST /appointments");
        const newAppt = await axios.post(`${API_URL}/appointments`, {
            title: 'Test Appointment Auto',
            date: new Date().toISOString(),
            location: 'Remote'
        });
        console.log("   Created Appointment ID:", newAppt.data.id);

        console.log("-> Testing GET /appointments");
        const apptsRes = await axios.get(`${API_URL}/appointments`);
        console.log(`   Fetched ${apptsRes.data.length} appointments`);

        // 4. Test Settings
        console.log("-> Testing POST /settings");
        const setRes = await axios.post(`${API_URL}/settings`, {
            key: 'test_agent_key',
            value: { test: true, timestamp: Date.now() }
        });
        console.log("   Created Setting ID:", setRes.data.id);

        console.log("-> Testing GET /settings");
        const settingsRes = await axios.get(`${API_URL}/settings`);
        console.log(`   Fetched ${settingsRes.data.length} settings`);

        console.log("[Test Agent] ALL TESTS PASSED SUCCESSFULLY! The ecosystem is flawless.");
    } catch (error: any) {
        console.error("[Test Agent] FAILED:", error.message);
        if (error.response) {
            console.error(error.response.data);
        }
        process.exit(1);
    }
}

testAll();
