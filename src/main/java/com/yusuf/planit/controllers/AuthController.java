package com.yusuf.planit.controllers;

import com.yusuf.planit.entities.User;
import com.yusuf.planit.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @GetMapping("/")
    public String planit()
    {
        return "home";
    }
    @GetMapping("/planit")
    public String planithome()
    {
        return "home";
    }


    @GetMapping("/user/login")
    public String showLoginForm(@RequestParam(value = "logout", required = false) String logout, @RequestParam(value = "error", required = false) String error, Model model, RedirectAttributes redirectAttributes)
    {
        if(logout != null)
        {
            String message = "You have been logged out successfully";
            redirectAttributes.addFlashAttribute("logout_message", message);
            return "redirect:/user/login";
        }

        if(error != null)
        {
            model.addAttribute("error", "Invalid email or password");
        }

        return "login";
    }

    @PostMapping("/user/login")
    public String loginUser(@ModelAttribute User user, Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/dashboard";
        } catch (AuthenticationException e) {
            model.addAttribute("error", e);
            return "login";
        }
    }

    @PostMapping("/user/register")
    public String registerUser(@Valid @ModelAttribute User user, Model model, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            return "register";
        }

        try
        {
            userService.registerUser(user);
            return "redirect:/user/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/user/register")
    public String showRegisterForm(Model model)
    {
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/user/logout")
    public String logout()
    {
        return "redirect:/user/login?logout=true";
    }

    @GetMapping("/user/profile")
    public String showProfile(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);
        model.addAttribute("user", currentUser);
        return "/profile";
    }
}
