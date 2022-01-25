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
    public String welcome(@ModelAttribute("user") User user, Model model,
                          @CookieValue(value = "currentUser", required = false) String currentUser) {
        if (currentUser != null && currentUser != "") {
            model.addAttribute("user", userService.findUserById(Long.parseLong(currentUser)));
            return "index";
        }
        return "index";
    }

    /**** ### Inloggningssida som autentiserar användare och innehar en cookie värde ### ****/
    @GetMapping("/signin")
    public String signIn(@ModelAttribute("user") User user, Model model,
                         @CookieValue(value = "currentUser", required = false) String currentUser) {
        if (currentUser != null && currentUser != "") {
            model.addAttribute("user", userService.findUserById(Long.parseLong(currentUser)));
            return "signin";
        }
        return "signin";
    }

    /**** ### Autentiserar användarkontot som innehar cookie värde & id för profilsidan ### ****/
    @PostMapping("/authenticate-user")
    public String authUser(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           HttpServletResponse response) {

        User user = userService.findUserByUsername(username);

        if (user != null && userService.authUser(username, password)) {
            Long id = user.getId();
            Cookie cookie = new Cookie("currentUser", id.toString());
            cookie.setMaxAge(5000);
            response.addCookie(cookie);
            return "redirect:/profile/" + id;
        }
        return "redirect:/authError";
    }

    /**************** ### Felmeddelande vid misslyckad inloggning ### ****************/
    @GetMapping("/authError")
    public String authError(User user, Model model) {
        model.addAttribute("msg", "The username and password you entered is incorrect. No Account? Register using the link below.");
        return "signin";
    }

    /**** ### Autentiserar både användare och inlägg utifrån id på profilsidan ### ****/
    @GetMapping("/profile/{id}")
    public String showProfile(@ModelAttribute("user")User user, Post post, Model model,
                              @CookieValue(value = "currentUser", required = false) String currentUser,
                              @PathVariable long id) {
        if (currentUser != null && currentUser != "") {
            model.addAttribute("posts", postService.findPostByAuthorIdCreatedDate(id));
            model.addAttribute("user", userService.findUserById(Long.parseLong(currentUser)));
            return "profile";
        }
        model.addAttribute("user", userService.findUserById(id));
        return "profile";
    }

    /********** ### Registrerar och sparar användare till databasen ### **********/
    @GetMapping("/signup")
    public String signUp(@ModelAttribute("user") User user, Model model,
                         @CookieValue(value = "currentUser", required = false) String currentUser) {
        if (currentUser != null && currentUser != "") {
            model.addAttribute("user", userService.findUserById(Long.parseLong(currentUser)));
            return "redirect:/";
        }
        return "signup";
    }

    /***** ### Sparar användare och tillsätter placeholder vid bekräftad lösenord ### *****/
    @PostMapping("/save-user")
    public String saveUser(User user,
                           @RequestParam("password") String password,
                           @RequestParam("password_confirm") String password_confirm) {

        if (password.equals(password_confirm)) {
            user.setImg("https://via.placeholder.com/150");
            userService.saveUser(user);
            return "redirect:/success";
        }
        return "redirect:/failed";
    }

    /****** ### Lyckad registrering som vägleder till inloggningssidan ### ******/
    @GetMapping("/success")
    public String success(@ModelAttribute("user") User user) {
        return "redirect:/signin";
    }

    /**************** ### Felmeddelande vid misslyckad registrering ### ****************/
    @GetMapping("/failed")
    public String failed(@ModelAttribute("user") User user,
                         Model model) {
        model.addAttribute("msg", "Registration failed.");
        return "signup";
    }

    /**************** ### Utloggning som tömmer cookie värden ### ****************/
    @GetMapping("/signout")
    public String signOut(HttpServletResponse response) {
        Cookie cookie = new Cookie("currentUser", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

    /******** ### Renderar lista av databasens användare med cookie värde ### ********/
    @GetMapping("/profiles")
    public String showProfiles(@ModelAttribute("user") User user, Model model,
                               @CookieValue(value = "currentUser", required = false) String currentUser) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        if (currentUser != null && currentUser != "") {
            model.addAttribute("user", userService.findUserById(Long.parseLong(currentUser)));
            return "profiles";
        }
        return "profiles";
    }

    /***** ### Möjliggör redigering av en användarkonto som innehar cookie värde ### *****/
    @GetMapping("/edit/{id}")
    public String editUser(Model model,
                           @PathVariable long id,
                           @CookieValue("currentUser") String currentUser) {
        userService.findUserById(id);
        User thisUser = userService.findUserById(Long.parseLong(currentUser));
        model.addAttribute("currentUser", currentUser);
        model.addAttribute(thisUser);
        return "edit";
    }

    /******** ### Uppdaterar användarkontot utifrån dess id ### ********/
    @PostMapping("/update-user")
    public String updateUser(@ModelAttribute User user) {
        userService.updateUser(user);
        Long id = user.getId();
        return "redirect:/profile/" + id;
    }

    /******** ### Raderar användarkontot utifrån dess id ### ********/
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable long id) {
        postService.deletePostsByAuthorId(id);
        userService.deleteUser(id);
        return "redirect:/signout";
    }
}
