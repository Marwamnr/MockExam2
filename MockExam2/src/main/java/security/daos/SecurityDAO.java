package security.daos;

import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import security.entities.Role;
import security.entities.User;
import security.exceptions.ApiException;
import security.exceptions.ValidationException;

import java.util.stream.Collectors;

public class SecurityDAO implements ISecurityDAO {

    private static ISecurityDAO instance;
    private static EntityManagerFactory emf;

    public SecurityDAO(EntityManagerFactory _emf) {
        emf = _emf;
    }

    // Returnerer en ny EntityManager instans
    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }


    // verificere bruger baseret på brugernavn og kodeord
    // Returnerer en UserDTO hvis brugeren og adgangskoden er korrekt
    @Override
    public UserDTO getVerifiedUser(String username, String password) throws ValidationException {
        try (EntityManager em = getEntityManager()) {
            User user = em.find(User.class, username);  // Finder brugeren baseret på brugernavn
            if (user == null)
                throw new EntityNotFoundException("No user found with username: " + username); // Kaster fejl hvis brugeren ikke findes
            user.getRoles().size(); // Henter brugerens roller fra databasen
            if (!user.verifyPassword(password)) // Tjekker om adgangskoden er korrekt
                throw new ValidationException("Wrong password"); // Kaster fejl hvis adgangskoden er forkert
            return new UserDTO(user.getUsername(), user.getRoles().stream().map(r -> r.getRoleName()).collect(Collectors.toSet()));
        }
    }

    // Opretter en ny bruger i databasen med brugernavn og kodeord
    // Tildeler også brugerrollen til den nye bruger
    @Override
    public User createUser(String username, String password) {
        try (EntityManager em = getEntityManager()) {
            User userEntity = em.find(User.class, username); // Tjekker om brugeren allerede eksisterer
            if (userEntity != null)
                throw new EntityExistsException("User with username: " + username + " already exists"); // Fejl hvis brugeren allerede findes
            userEntity = new User(username, password); // Opretter ny bruger
            em.getTransaction().begin(); // Starter transaktion
            Role userRole = em.find(Role.class, "user"); // Finder brugerrollen
            if (userRole == null) // Opretter brugerrollen hvis den ikke findes
                userRole = new Role("user");
            em.persist(userRole); // Gemmer brugerrollen i databasen
            userEntity.addRole(userRole); // Tildeler rollen til brugeren
            em.persist(userEntity); // Gemmer brugeren i databasen
            em.getTransaction().commit(); // Bekræfter transaktionen
            return userEntity;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(400, e.getMessage()); // Generel fejlbehandling
        }
    }

    // Tilføjer en ny rolle til en eksisterende bruger
    @Override
    public User addRole(UserDTO userDTO, String newRole) {
        try (EntityManager em = getEntityManager()) {
            User user = em.find(User.class, userDTO.getUsername()); // Finder brugeren baseret på brugernavn
            if (user == null)
                throw new EntityNotFoundException("No user found with username: " + userDTO.getUsername()); // Fejl hvis brugeren ikke findes
            em.getTransaction().begin(); // Starter transaktion
            Role role = em.find(Role.class, newRole); // Finder den nye rolle
            if (role == null) { // Opretter rollen hvis den ikke findes
                role = new Role(newRole);
                em.persist(role); // Gemmer den nye rolle i databasen
            }
            user.addRole(role); // Tilføjer rollen til brugeren
            em.getTransaction().commit(); // Bekræfter transaktionen
            return user;
        }
    }
}
