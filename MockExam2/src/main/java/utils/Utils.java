package utils;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.http.Context;
import security.exceptions.ApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Utils {

    public static void main(String[] args) {
        System.out.println(getPropertyValue("db.name", "properties-from-pom.properties"));
    }

    public static String getPropertyValue(String propName, String resourceName) {
        // Henter en egenskabsværdi fra en angivet ressourcename og propName
        try (InputStream is = Utils.class.getClassLoader().getResourceAsStream(resourceName)) {
            Properties prop = new Properties();
            prop.load(is);  // Indlæs egenskaber fra den specificerede ressource

            String value = prop.getProperty(propName);  // Få værdien for den ønskede egenskab
            if (value != null) {
                return value.trim();  // Trimmer mellemrum
            } else {
                // Kaster en ApiException hvis egenskaben ikke findes i filen
                throw new ApiException(500, String.format("Egenskab %s blev ikke fundet i %s", propName, resourceName));
            }
        } catch (IOException ex) {
            // Kaster en ApiException hvis der opstår en IO fejl under læsningen af egenskaben
            ex.printStackTrace();
            throw new ApiException(500, String.format("Kunne ikke læse egenskaben %s. Huskede du at bygge projektet med MAVEN?", propName));
        }
    }

    public ObjectMapper getObjectMapper() {
        // Konfigurerer og returnerer en ObjectMapper til JSON håndtering
        ObjectMapper objectMapper = new ObjectMapper();
        // Ignorerer ukendte egenskaber i JSON data for at forhindre fejl
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Tilføjer modulet til håndtering af Java Time API-typer i JSON serialisering/deserialisering
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.writer(new DefaultPrettyPrinter()); // Indstiller en standard formattering
        return objectMapper; // Returnerer den konfigurerede ObjectMapper
    }

    public static String convertToJsonMessage(Context ctx, String property, String message) {
        // Konverterer en statusbesked og egenskab til en JSON streng

        Map<String, String> msgMap = new HashMap<>();
        msgMap.put(property, message);  // Indsætter beskeden i mappen
        msgMap.put("status", String.valueOf(ctx.status()));  // Tilføjer statuskode til mappen
        ObjectMapper objectMapper = new ObjectMapper();  // Opretter en ObjectMapper til konverteringen
        try {
            // Konverterer mappen til en JSON streng
            return objectMapper.writeValueAsString(msgMap);
        } catch (Exception e) {
            // Returnerer en fejlmeddelelse hvis konverteringen fejler
            return "{\"error\": \"Kunne ikke konvertere besked til JSON\"}";
        }
    }
}
