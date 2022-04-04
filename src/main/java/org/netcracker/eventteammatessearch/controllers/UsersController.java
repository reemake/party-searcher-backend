package org.netcracker.eventteammatessearch.controllers;

import org.netcracker.eventteammatessearch.Services.UserService;
import org.netcracker.eventteammatessearch.entity.User;
import org.netcracker.eventteammatessearch.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequestMapping(value = "/api/users")
@RestController
public class UsersController {

    @Autowired
    UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getUsers")
    public List<User> getUsers() {
        return userService.getAll();
    }

    @GetMapping("/getUserByLogin")
    public User getByLogin(@RequestParam String userLogin) {
        return userService.getUserByLogin(userLogin);
    }

    @PatchMapping("/updateUser")
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

    @GetMapping("/approvePassword")
    public boolean approvePassword(@RequestParam String login, @RequestParam String password) {
        return userService.approvePassword(login, password);
    }
    @GetMapping("/api/usersList")
    public List<User> get(HttpServletRequest request, Principal principal) {
        return userRepository.getUsersByLogin(request.getHeader("login"));
    }
}