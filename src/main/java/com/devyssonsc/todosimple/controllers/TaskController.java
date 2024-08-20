package com.devyssonsc.todosimple.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devyssonsc.todosimple.models.Task;
import com.devyssonsc.todosimple.models.User.CreateUser;
import com.devyssonsc.todosimple.models.User.UpdateUser;
import com.devyssonsc.todosimple.services.TaskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/task")
@Validated
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id)
    {
        Task obj = taskService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    @Validated(CreateUser.class)
    public ResponseEntity<Void> create(@Valid @RequestBody Task obj)
    {
        this.taskService.create(obj);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @Validated(UpdateUser.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Task obj, @PathVariable Long id)
    {
        obj.setId(id);
        taskService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> update(@Valid Long id)
    {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> findAllByUserId(@PathVariable Long id)
    {
        List<Task> tasks = this.taskService.findAllByUserId(id);
        return ResponseEntity.ok().body(tasks);
    }

}
