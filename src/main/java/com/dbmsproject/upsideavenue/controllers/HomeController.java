package com.dbmsproject.upsideavenue.controllers;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbmsproject.upsideavenue.models.Photo;
import com.dbmsproject.upsideavenue.models.User;
import com.dbmsproject.upsideavenue.repositories.PhotoRepository;
import com.dbmsproject.upsideavenue.repositories.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @GetMapping
    public String home() {
        return "home";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
            userRepository.save(user);
        } else {

        }

        // System.out.println("Registration successfull");
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        // System.out.println();
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        // SecurityContextHolder.clearContext();
        return "logout";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "dashboard";
    }

    @GetMapping("/error")
    public String error(Model model, @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("error", error);
        return "error";
    }

    @GetMapping("/image/display/{id}")
    @ResponseBody
    void showImage(@PathVariable("id") UUID id, HttpServletResponse response)
            throws ServletException, IOException {
        Photo photo = photoRepository.findById(id).orElse(null);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(photo.getPhoto());
        response.getOutputStream().close();
    }
}
