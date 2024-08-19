package com.devyssonsc.todosimple.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devyssonsc.todosimple.models.User;
import com.devyssonsc.todosimple.repositories.TaskRepository;
import com.devyssonsc.todosimple.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    public User findById(Long id)
    {
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new RuntimeException(
            "Usuário não encontrado! Id: " + id + ", Tipo: " + User.class.getName()
        ));
    }

    // A notação @Transactional é uma anotação usada em Java para indicar que um método ou uma classe deve ser executado dentro de uma transação. Uma transação é uma unidade lógica de trabalho que consiste em uma ou mais operações de banco de dados.

    // Quando um método ou uma classe é marcado com @Transactional, o framework de persistência (como o Hibernate) garante que todas as operações de banco de dados realizadas dentro desse método ou classe sejam tratadas como uma única transação. Isso significa que, se alguma operação de banco de dados falhar, todas as alterações feitas até aquele ponto serão revertidas, mantendo o banco de dados em um estado consistente.
    @Transactional
    public User create(User obj)
    {
        //zera o id para ngm conseguir alterar a senha de um user pelo create
        obj.setId(null);
        //o método save verifica se o objeto a ser salvo é nulo
        //depois verifica se é um novo objeto ou se já existe
        //se existe, atualiza os dados
        //se não existe, cria o user
        //nesse caso nunca vai criar pq na linha anterior o id está sendo anulado
        obj = this.userRepository.save(obj);

        //caso o user já seja criado com tasks
        this.taskRepository.saveAll(obj.getTasks());
        return obj;
    }

    @Transactional
    public User update(User obj)
    {
        //procura, no banco, o user que foi recebido (pelo id)
        //e referencia ele em newObj
        User newObj = findById(obj.getId());

        //põe, no newObj, a senha que foi recebida no objeto obj
        newObj.setPassword(obj.getPassword());

        //Atualiza a senha do user que foi recebido
        return this.userRepository.save(newObj);
    }
    
    @Transactional
    public void delete(Long id)
    {
        findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Não é possível excluir pois há entidades relacionadas!");
        }
    }
}
