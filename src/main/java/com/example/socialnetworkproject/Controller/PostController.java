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
    public String viewPosts(@ModelAttribute("post") Post post, User user, Model model,
                           @CookieValue(value = "userMemory", required = false) String userMemory) {
        if (userMemory != null) {
            model.addAttribute("user", userService.findUserById(Long.parseLong(userMemory)));
            model.addAttribute("posts", postService.findPostsByCreatedDate());
            return "posts";
        }
        model.addAttribute("error_msg", "You must be logged in to view Posts");
        return "login";
    }

    /**** ### Adderar inlägg som innehar tidstämpel, datum och cookie värde  ### ****/
    @PostMapping("/addPost")
    public String postSave(@ModelAttribute Post post, Model model,
                           @CookieValue("userMemory") String userMemory) {
        User postUser = userService.findUserById(Long.parseLong(userMemory));
        model.addAttribute("userMemory", userMemory);
        post.setAuthor(postUser);
        LocalDateTime date = LocalDateTime.now();
        Timestamp showtime = Timestamp.valueOf(date);
        post.setCreatedDate(showtime);
        postService.postSave(post);
        return "redirect:/posts";
    }

    /****** ### Raderar inlägg utifrån dess id ### ******/
    @GetMapping("/delete-post/{id}")
    public String postDelete(@PathVariable long id) {
        postService.postDeleteId(id);
        return "redirect:/posts";
    }

    /****** ### Raderar inlägg utifrån en kombination av id och användarnamn ### ******/
    @GetMapping("/delete-by-author/{id}")
    public String postsDeletesByAuthor(@PathVariable long id) {
        postService.postsDeleteByAuthorId(id);
        return "redirect:/posts";
    }

}
