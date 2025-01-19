package com.yusuf.planit.services;

import com.yusuf.planit.entities.Task;
import com.yusuf.planit.entities.User;
import com.yusuf.planit.repositories.TasksRepository;
import com.yusuf.planit.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private UserRepository userRepository;


    public List<Task> getTasksByUserId(Integer userId) {
        return tasksRepository.findAllByUserId(userId);
    }

    @Transactional
    public void addTask(Task task, Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        task.setUser(user);
        task.setCreatedAt(java.time.Instant.now());
        tasksRepository.save(task);
    }

    @Transactional
    public void deleteTask(Integer taskId, Integer userId)
    {
        tasksRepository.deleteByIdAndUserId(taskId, userId);
    }

    public void markTaskAsCompleted(Integer taskId, Integer userId)
    {
        Task task = tasksRepository.findByIdAndUserId(taskId, userId);
        task.setCompleted(true);
        tasksRepository.save(task);
    }

}
