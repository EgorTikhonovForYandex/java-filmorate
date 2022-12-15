package ru.yandex.practicum.filmorate.controller;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@NonNull
@Valid
@NotEmpty
@NotBlank
public class UserController {
    private final UserService userService;
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;
    public UserController(@NonNull
                          @Valid
                          @NotEmpty
                          @NotBlank UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> getAllUsers() {
        log.info("Получен запрос GET /users. Список всех пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUser( @NotEmpty @Valid @NotBlank @PathVariable int userId) {
        log.info("Найдем пользователя по id = " + userId + ".");
        return userService.get(userId);
    }

    @PostMapping
    public User create(@NotBlank @RequestBody User user) {

        if (user.getId() == 0) {
            user.setId(id);
            id++;
        }
        if (user.getName() == null){
            user.setName(user.getLogin());
        }

        if (!users.containsKey(user.getId()) ) {
            users.put(user.getId(), user);
            log.info("User {} added successfully", user.getName());

            return users.put(user.getId(), user);
        } else {
            log.info("This User is already on the list");
            throw new ValidationException(user.getName());

        }
     /* log.info("Получен запрос POST /users. Создан пользователь {}.", user.getName());
        return userService.create(user);*/
    }

    @PutMapping
    public User update( @NotBlank @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("User {} update successfully", user.getName());
            return user;
        }
        log.info("This User is not already on the list");

        throw new ValidationException(user.getName() + " not exists");
        /*log.info("Получен запрос PUT /users. Данные пользователя {} обновлены.", user.getName());
        return userService.update(user);*/
    }

    //PUT /users/{id}/friends/{friendId} — добавление в друзья.
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(  @PathVariable int id,  @PathVariable int friendId) {
        log.info("Добавление в друзья.");
        userService.addFriend(id, friendId);
    }

    //DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(  @PathVariable int id,  @PathVariable int friendId) {
        log.info("Удаление из друзей.");
        userService.deleteFriend(id, friendId);
    }

    //GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    @GetMapping("/{id}/friends")
    public List<User> getAllFriends( @NotBlank @PathVariable int id) {
        log.info("Возвращаем список пользователей, являющихся друзьями.");
        return userService.getAllFriends(id);
    }

    // GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Возвращаем список друзей, общих с другим пользователем");
        return userService.getCommonFriends(id, otherId);
    }
}
