package com.yusuf.planit.controllers;

import com.yusuf.planit.configurations.CustomUserDetails;
import com.yusuf.planit.entities.Task;
import com.yusuf.planit.entities.User;
import com.yusuf.planit.repositories.TasksRepository;
import com.yusuf.planit.services.TaskService;
import com.yusuf.planit.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/planit/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getId();

        List<Task> tasks = taskService.getTasksByUserId(userId);
        model.addAttribute("tasks", tasks);
        model.addAttribute("task", new Task());
        return "dashboard";
    }

    @PostMapping("/api/add-task")
    public String saveTask(@ModelAttribute Task task, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            taskService.addTask(task, user.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Task added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to add task: " + e.getMessage());
        }
        return "redirect:/planit/dashboard";
    }

    @PostMapping("/api/delete-task")
    public String deleteTask(@RequestParam Integer taskId, Authentication authentication, RedirectAttributes redirectAttributes) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getId();
        taskService.deleteTask(taskId, userId);
        redirectAttributes.addFlashAttribute("successMessage", "Task deleted successfully");
        return "redirect:/planit/dashboard";
    }

    @PostMapping("/api/complete-task")
    public String updateTask(@RequestParam Integer taskId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getId();
        taskService.markTaskAsCompleted(taskId, userId);
        return "redirect:/planit/dashboard";
    }

}
