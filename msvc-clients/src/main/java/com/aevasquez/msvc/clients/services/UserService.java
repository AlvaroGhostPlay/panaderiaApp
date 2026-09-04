package com.aevasquez.msvc.clients.services;

import com.aevasquez.msvc.clients.dto.UserCreateDto;
import com.aevasquez.msvc.clients.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "msvc-users-roles")
public interface UserService {
    @PostMapping("/user/new")
    User createUser(@RequestBody UserCreateDto user);

}
