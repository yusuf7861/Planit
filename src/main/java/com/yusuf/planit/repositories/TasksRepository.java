package com.yusuf.planit.repositories;

import com.yusuf.planit.entities.Task;
import com.yusuf.planit.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TasksRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByUserId(Integer userId);
    void deleteByIdAndUserId(Integer taskId, Integer userId);
    Task findByIdAndUserId(Integer taskId, Integer userId);
}
