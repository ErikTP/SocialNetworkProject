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

    /*** ### Hitta användare via deras id ### ***/
    public User findUserById(long id) {
        return userRepository.findById(id).orElseThrow();
    }

    /*** ### Hitta användare via deras användarnamn ### ***/
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /*** ### Lista av alla användare ### ***/
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /*** ### Autentisera användare genom DB som letar användarnamn och jämför lösenord ***/
    public boolean authUser(String username, String password) {
        User userData = userRepository.findByUsername(username);
        if (userData == null) {
            System.out.println("Username is incorrect " + username);
            return false;
        }
        String compPass = createSecureHashPass(password, convertStringToByteForDB(userData.getSalt()));
        return userData.getPassword().equals(compPass);
    }

    /*** ### Uppdatera användaruppgifter och spara det till databas ### ***/
    public void updateUser(User user) {
        User dataUser = userRepository.findById(user.getId()).orElseThrow();
        dataUser.setUsername(user.getUsername());
        dataUser.setAddress(user.getAddress());
        userRepository.save(dataUser);
    }

    /*** ### Radera användare från databas via deras id ### ***/
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }


    /*** ### Spara användare och enkrypterad lösenord ### ***/
    public void saveUser(User user) {
        byte[] byteSalt = generateSalt();
        String strSalt = convertByteToStringForDB(byteSalt);
        String passHash = createSecureHashPass(user.getPassword(), byteSalt);

        if (!passHash.equals("")) {
            user.setSalt(strSalt);
            user.setPassword(passHash);
            userRepository.save(user);
        }
    }

    /*** ### Skapa Hash till lösenord ### ***/
    public String createSecureHashPass(String plainTextPassword, byte[] byteSalt) {
        try {
            MessageDigest msgDig = MessageDigest.getInstance("SHA-256");
            msgDig.update(byteSalt);
            byte[] passHash = msgDig.digest(plainTextPassword.getBytes());
            return convertByteToStringForDB(passHash);

        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /*** ### Konvertera lösenordets Hash till sträng ### ***/
    private String convertByteToStringForDB(byte[] passHash) {
        return DatatypeConverter.printHexBinary(passHash).toLowerCase();
    }

    /*** ### Konvertera lösenordets sträng till Byte ### ***/
    private byte[] convertStringToByteForDB(String dataPass) {
        return DatatypeConverter.parseHexBinary(dataPass);
    }

    /*** ### Generera lösenordets Salt ### ***/
    private byte[] generateSalt() {
        SecureRandom randSec = new SecureRandom();
        byte[] genSalt = randSec.generateSeed(12);
        return genSalt;
    }
}
