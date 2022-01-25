package com.example.socialnetworkproject.Controller;

import com.example.socialnetworkproject.Entity.Post;
import com.example.socialnetworkproject.Entity.User;
import com.example.socialnetworkproject.Service.PostService;
import com.example.socialnetworkproject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Controller
public class PostController {

    /****** ### Dependency injektioner för att tillkalla Spring Security ### ******/
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    /********* ### Renderar vy av användarinlägg som innehar cookie värde ### *********/
    @GetMapping("/posts")
    public String allPosts(@ModelAttribute("post") Post post, User user, Model model,
                           @CookieValue(value = "currentUser", required = false) String currentUser) {
        if (currentUser != null) {
            model.addAttribute("posts", postService.findPostsByCreatedDate());
            model.addAttribute("user", userService.findUserById(Long.parseLong(currentUser)));
            return "posts";
        }
        model.addAttribute("msg", "You must login to view Posts");
        return "signin";
    }

    /**** ### Adderar inlägg som innehar tidstämpel, datum och cookie värde  ### ****/
    @PostMapping("/addpost")
    public String savePost(@ModelAttribute Post post, Model model,
                           @CookieValue("currentUser") String currentUser) {
        User curUser = userService.findUserById(Long.parseLong(currentUser));
        model.addAttribute("currentUser", currentUser);
        post.setAuthor(curUser);
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        post.setCreatedDate(timestamp);
        postService.savePost(post);
        return "redirect:/posts";
    }

    /****** ### Raderar inlägg utifrån dess id ### ******/
    @GetMapping("/delete-post/{id}")
    public String deletePost(@PathVariable long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }

    /****** ### Raderar inlägg utifrån en kombination av id och användarnamn ### ******/
    @GetMapping("/delete-by-author/{id}")
    public String deleteByAuthor(@PathVariable long id) {
        postService.deletePostsByAuthorId(id);
        return "redirect:/posts";
    }

}
