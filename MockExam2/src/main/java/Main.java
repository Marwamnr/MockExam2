import config.ApplicationConfig;
import config.HibernateConfig;
import config.Populator;
import jakarta.persistence.EntityManagerFactory;

public class Main {

    // Opretter en EntityManagerFactory med database navn
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("doctor");

    public static void main(String[] args) {
        // Populer databasen før serveren startes
        Populator populator = new Populator(emf);
        populator.populate(); // Opdateret metodekald for at matche Populator

        // Start serveren
        ApplicationConfig.startServer(7070);
    }
}
