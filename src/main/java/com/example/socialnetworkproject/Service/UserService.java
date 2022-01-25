package com.example.socialnetworkproject.Service;

import com.example.socialnetworkproject.Entity.User;
import com.example.socialnetworkproject.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /*** ### Spara användare och enkrypterad lösenord ### ***/
    public void saveUser(User user) {
        byte[] salt = generateSalt();
        String saltString = convertByteToStringForDB(salt);
        String hashedPassword = createSecureHashPass(user.getPassword(), salt);

        if (!hashedPassword.equals("")) {
            user.setSalt(saltString);
            user.setPassword(hashedPassword);
            userRepository.save(user);
        }
    }

    /*** ### Skapa Hash till lösenord ### ***/
    public String createSecureHashPass(String plainTextPassword, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPass = md.digest(plainTextPassword.getBytes());
            return convertByteToStringForDB(hashedPass);

        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /*** ### Konvertera lösenordets Hash till sträng ### ***/
    private String convertByteToStringForDB(byte[] hashedPass) {
        return DatatypeConverter.printHexBinary(hashedPass).toLowerCase();
    }

    /*** ### Konvertera lösenordets sträng till Byte ### ***/
    private byte[] convertStringToByteForDB(String dbPassword) {
        return DatatypeConverter.parseHexBinary(dbPassword);
    }

    /*** ### Generera lösenordets Salt ### ***/
    private byte[] generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] hashedSalt = sr.generateSeed(12);
        return hashedSalt;
    }

    /*** ### Autentisera användare genom DB som letar användarnamn och jämför lösenord ***/
    public boolean authUser(String username, String password) {
        User dbUser = userRepository.findByUsername(username);
        if (dbUser == null) {
            System.out.println("Username is not correct " + username);
            return false;
        }
        String passwordToCompare = createSecureHashPass(password, convertStringToByteForDB(dbUser.getSalt()));
        return dbUser.getPassword().equals(passwordToCompare);
    }

    /*** ### Lista av alla användare ### ***/
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /*** ### Hitta användare via deras id ### ***/
    public User findUserById(long id) {
        return userRepository.findById(id).orElseThrow();
    }

    /*** ### Hitta användare via deras användarnamn ### ***/
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /*** ### Uppdatera användaruppgifter och spara det till databas ### ***/
    public void updateUser(User user) {
        User userDB = userRepository.findById(user.getId()).orElseThrow();
        userDB.setUsername(user.getUsername());
        userDB.setAddress(user.getAddress());
        userRepository.save(userDB);
    }

    /*** ### Radera användare från databas via deras id ### ***/
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

}
