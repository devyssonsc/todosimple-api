package com.devyssonsc.todosimple.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devyssonsc.todosimple.models.Task;
import com.devyssonsc.todosimple.models.User;
import com.devyssonsc.todosimple.repositories.TaskRepository;

@Service
public class TaskService {
    
    
    // A notação @Autowired é usada no contexto do Spring Framework em Java para realizar a injeção de dependência automática.
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById(Long id)
    {
        Optional<Task> task = this.taskRepository.findById(id);

        return task.orElseThrow(() -> new RuntimeException(
            "Tarefa não encontrada! Id: " + id + ", Tipo: " + Task.class.getName()
        ));
    }

    @Transactional
    public Task create(Task obj)
    {
        //pega o user da task
        // User user = obj.getUser();
        User user = userService.findById(obj.getUser().getId());

        // //pega a lista de tasks do user e coloca em uma nova lista
        // List<Task> newTasks = user.getTasks();

        // //adiciona a nova task na nova lista
        // newTasks.add(obj);

        // //seta a lista do user como sendo a nova lista
        // user.setTasks(newTasks);

        // //atualiza o user
        // userService.update(user);

        //seta de qual user é a task
        obj.setUser(user);

        //zera o id da task
        obj.setId(null);

        //salva a task
        return this.taskRepository.save(obj);
    }

    @Transactional
    public Task update(Task obj)
    {
        Task newObj = findById(obj.getId());

        newObj.setDescription(obj.getDescription());

        return this.taskRepository.save(newObj);
    }

    @Transactional
    public void delete(Long id)
    {
        findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Não é possível excluir pois há entidades relacionadas!");
        }
    }
}
