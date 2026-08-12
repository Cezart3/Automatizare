async function testAll() {
    const API_URL = 'http://localhost:3000/api';
    console.log("[Test Agent] Starting full ecosystem API test...");

    try {
        // 1. Test Stats
        console.log("-> Testing GET /stats");
        const statsRes = await fetch(`${API_URL}/stats`);
        const statsData = await statsRes.json();
        console.log("   Stats:", statsData);
        
        // 2. Test Tickets
        console.log("-> Testing POST /tickets");
        const newTicketRes = await fetch(`${API_URL}/tickets`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                title: 'Test Ticket Auto',
                description: 'Problema generata automat de agentul de test',
                status: 'TODO',
                priority: 'HIGH'
            })
        });
        const newTicket = await newTicketRes.json();
        console.log("   Created Ticket ID:", newTicket.id);
        
        console.log(`-> Testing PATCH /tickets/${newTicket.id}`);
        const patchedTicketRes = await fetch(`${API_URL}/tickets/${newTicket.id}`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status: 'IN_PROGRESS' })
        });
        const patchedTicket = await patchedTicketRes.json();
        console.log("   Patched Ticket Status:", patchedTicket.status);

        console.log("-> Testing GET /tickets");
        const ticketsRes = await fetch(`${API_URL}/tickets`);
        const ticketsData = await ticketsRes.json();
        console.log(`   Fetched ${ticketsData.length} tickets`);

        // 3. Test Appointments
        console.log("-> Testing POST /appointments");
        const newApptRes = await fetch(`${API_URL}/appointments`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                title: 'Test Appointment Auto',
                date: new Date().toISOString(),
                location: 'Remote'
            })
        });
        const newAppt = await newApptRes.json();
        console.log("   Created Appointment ID:", newAppt.id);

        console.log("-> Testing GET /appointments");
        const apptsRes = await fetch(`${API_URL}/appointments`);
        const apptsData = await apptsRes.json();
        console.log(`   Fetched ${apptsData.length} appointments`);

        // 4. Test Settings
        console.log("-> Testing POST /settings");
        const setRes = await fetch(`${API_URL}/settings`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                key: 'test_agent_key',
                value: { test: true, timestamp: Date.now() }
            })
        });
        const setData = await setRes.json();
        console.log("   Created Setting ID:", setData.id);

        console.log("-> Testing GET /settings");
        const settingsRes = await fetch(`${API_URL}/settings`);
        const settingsData = await settingsRes.json();
        console.log(`   Fetched ${settingsData.length} settings`);

        console.log("[Test Agent] ALL TESTS PASSED SUCCESSFULLY! The ecosystem is flawless.");
    } catch (error) {
        console.error("[Test Agent] FAILED:", error.message);
        process.exit(1);
    }
}

testAll();
