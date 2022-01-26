package com.example.socialnetworkproject.Controller;

import com.example.socialnetworkproject.Entity.Post;
import com.example.socialnetworkproject.Entity.User;
import com.example.socialnetworkproject.Service.PostService;
import com.example.socialnetworkproject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class UserController {

    /****** ### Dependency injektioner för att tillkalla Spring Security ### ******/
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    /**************** ### Webbapplikationens hemsida ### ****************/
    @GetMapping("/")
    public String Homepage(@ModelAttribute("user") User user, Model model,
                          @CookieValue(value = "userMemory", required = false) String userMemory) {
        if (userMemory != null && userMemory != "") {
            model.addAttribute("user", userService.findUserById(Long.parseLong(userMemory)));
            return "index";
        }
        return "index";
    }

    /********** ### Registrerar och sparar användare till databasen ### **********/
    @GetMapping("/register")
    public String register(@ModelAttribute("user") User user, Model model,
                         @CookieValue(value = "userMemory", required = false) String userMemory) {
        if (userMemory != null && userMemory != "") {
            model.addAttribute("user", userService.findUserById(Long.parseLong(userMemory)));
            return "redirect:/";
        }
        return "register";
    }

    /***** ### Sparar användare och tillsätter placeholder vid bekräftad lösenord ### *****/
    @PostMapping("/save-user")
    public String userSave(User user,
                           @RequestParam("password") String password,
                           @RequestParam("password_verify") String password_verify) {

        if (password.equals(password_verify)) {
            user.setImg("https://via.placeholder.com/150");
            userService.saveUser(user);
            return "redirect:/success";
        }
        return "redirect:/failed";
    }

    /**************** ### Felmeddelande vid misslyckad registrering ### ****************/
    @GetMapping("/failed")
    public String regFailed(@ModelAttribute("user") User user,
                         Model model) {
        model.addAttribute("error_msg", "Registration failed.");
        return "register";
    }

    /****** ### Lyckad registrering som vägleder till inloggningssidan ### ******/
    @GetMapping("/success")
    public String regSuccess(@ModelAttribute("user") User user) {
        return "redirect:/login";
    }

    /**** ### Inloggningssida som autentiserar användare och innehar en cookie värde ### ****/
    @GetMapping("/login")
    public String login(@ModelAttribute("user") User user, Model model,
                         @CookieValue(value = "userMemory", required = false) String userMemory) {
        if (userMemory != null && userMemory != "") {
            model.addAttribute("user", userService.findUserById(Long.parseLong(userMemory)));
            return "login";
        }
        return "login";
    }

    /**************** ### Felmeddelande vid misslyckad inloggning ### ****************/
    @GetMapping("/loginError")
    public String loginError(User user, Model model) {
        model.addAttribute("error_msg", "The username or password is incorrect. No Account? Register now!");
        return "login";
    }

    /**** ### Autentiserar användarkontot som innehar cookie värde & id för profilsidan ### ****/
    @PostMapping("/authenticate-user")
    public String userAuth(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           HttpServletResponse resp) {

        User user = userService.findUserByUsername(username);

        if (user != null && userService.authUser(username, password)) {
            Long id = user.getId();
            Cookie memory = new Cookie("userMemory", id.toString());
            memory.setMaxAge(7000);
            resp.addCookie(memory);
            return "redirect:/profile/" + id;
        }
        return "redirect:/loginError";
    }

    /**** ### Autentiserar både användare och inlägg utifrån id på profilsidan ### ****/
    @GetMapping("/profile/{id}")
    public String viewProfile(@ModelAttribute("user")User user, Post post, Model model,
                              @CookieValue(value = "userMemory", required = false) String userMemory,
                              @PathVariable long id) {
        if (userMemory != null && userMemory != "") {
            model.addAttribute("user", userService.findUserById(Long.parseLong(userMemory)));
            model.addAttribute("posts", postService.findPostByAuthorIdCreatedDate(id));
            return "profile";
        }
        model.addAttribute("user", userService.findUserById(id));
        return "profile";
    }

    /******** ### Renderar lista av databasens användare med cookie värde ### ********/
    @GetMapping("/profiles")
    public String viewProfiles(@ModelAttribute("user") User user, Model model,
                               @CookieValue(value = "userMemory", required = false) String userMemory) {
        List<User> members = userService.findAllUsers();
        model.addAttribute("users", members);
        if (userMemory != null && userMemory != "") {
            model.addAttribute("user", userService.findUserById(Long.parseLong(userMemory)));
            return "profiles";
        }
        return "profiles";
    }

    /***** ### Möjliggör redigering av en användarkonto som innehar cookie värde ### *****/
    @GetMapping("/edit/{id}")
    public String userEdit(Model model,
                           @PathVariable long id,
                           @CookieValue("userMemory") String userMemory) {
        userService.findUserById(id);
        User traceUser = userService.findUserById(Long.parseLong(userMemory));
        model.addAttribute("userMemory", userMemory);
        model.addAttribute(traceUser);
        return "edit";
    }

    /******** ### Uppdaterar användarkontot utifrån dess id ### ********/
    @PostMapping("/update-user")
    public String userUpdate(@ModelAttribute User user) {
        userService.updateUser(user);
        Long id = user.getId();
        return "redirect:/profile/" + id;
    }

    /******** ### Raderar användarkontot utifrån dess id ### ********/
    @GetMapping("/delete/{id}")
    public String userDelete(@PathVariable long id) {
        userService.deleteUser(id);
        postService.deletePostsByAuthorId(id);
        return "redirect:/logout";
    }

    /**************** ### Utloggning som tömmer cookie värden ### ****************/
    @GetMapping("/logout")
    public String logout(HttpServletResponse resp) {
        Cookie memory = new Cookie("userMemory", "");
        memory.setMaxAge(0);
        resp.addCookie(memory);
        return "redirect:/";
    }
}
