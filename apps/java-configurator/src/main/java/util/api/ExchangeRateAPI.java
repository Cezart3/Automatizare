package util.api;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class ExchangeRateAPI {

    private static final String TRUSTSTORE_RESOURCE = "/certs/bnr-truststore.jks";
    private static final String TRUSTSTORE_PASSWORD = "changeit";

    private static void configureTrustStore() throws Exception {
        // Creează un fișier temporar pentru truststore
        InputStream is = ExchangeRateAPI.class.getResourceAsStream(TRUSTSTORE_RESOURCE);
        if (is == null) throw new RuntimeException("Nu am găsit truststore-ul în resurse!");

        File tempFile = File.createTempFile("bnr-truststore", ".jks");
        tempFile.deleteOnExit();

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[4096];
            int read;
            while ((read = is.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
        }

        System.setProperty("javax.net.ssl.trustStore", tempFile.getAbsolutePath());
        System.setProperty("javax.net.ssl.trustStorePassword", TRUSTSTORE_PASSWORD);
    }

    public static double getEuroToRon() {
        final double FALLBACK = 5.0;
        try {
            configureTrustStore();

            URL url = new URL("https://www.bnr.ro/nbrfxrates.xml");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            try (InputStream in = conn.getInputStream()) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setNamespaceAware(false);
                dbf.setIgnoringElementContentWhitespace(true);
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(in);

                NodeList rates = doc.getElementsByTagName("Rate");
                for (int i = 0; i < rates.getLength(); i++) {
                    String currency = rates.item(i).getAttributes()
                            .getNamedItem("currency").getTextContent();
                    if ("EUR".equalsIgnoreCase(currency)) {
                        String value = rates.item(i).getTextContent().trim().replace(",", ".");
                        return Double.parseDouble(value);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Nu s-a putut obține cursul BNR, folosim fallback " + FALLBACK);
            e.printStackTrace();
        }
        return FALLBACK;
    }

    public static void main(String[] args) {
        System.out.println("Curs EUR->RON: " + getEuroToRon());
    }
}
