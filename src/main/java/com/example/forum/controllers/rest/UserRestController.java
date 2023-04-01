package com.example.forum.controllers.rest;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.helpers.UserMapper;
import com.example.forum.models.FilterOptions;
import com.example.forum.models.Phone;
import com.example.forum.models.User;
import com.example.forum.models.UserDto;
import com.example.forum.services.PhoneService;
import com.example.forum.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// Postman link in README
// ^^^^^^^^^^^^^^^^^^^^^^

@RestController
@Tag(name = "User Controller", description = "Operations with users")
@RequestMapping("/api/users")

public class UserRestController {
    private final UserService userService;

    private final PhoneService phoneService;

    private final UserMapper userMapper;

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserRestController(UserService userService, PhoneService phoneService, UserMapper userMapper, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.phoneService = phoneService;
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;
    }


    @ApiResponse(responseCode = "200", description = "Successful operation")
    @Operation(summary = "Get all users",
            description = "Retrieve a list of all users with option to filter and sort")
        @GetMapping
        public List<User> getUsers(@RequestParam (required = false) String firstName,
                                   @RequestParam (required = false) String username,
                                   @RequestParam (required = false) String email,
                                   @RequestParam (required = false) String sortBy,
                                   @RequestParam (required = false) String sortOrder) {
            try {
                FilterOptions filterOptions = new FilterOptions(firstName, username, email, sortBy, sortOrder);
                return userService.getAll(filterOptions);
            }catch (UnsupportedOperationException e){
                throw new ResponseStatusException(HttpStatus.CONFLICT,e.getMessage());
            }
        }
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @Operation(summary = "Get user by id",
            description = "Retrieve a single user")
    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        try {
            return userService.getByID(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "409", description = "Duplicate fields")
    @Operation(summary = "Create user")
    @PostMapping
    private User createUser(@Valid @RequestBody UserDto dto) {
        try {
            User user = userMapper.fromDto(dto);
            return userService.create(user);
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }


    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @Operation(summary = "Delete user")
    @DeleteMapping("/{id}")
    private void deleteUser(@RequestHeader String authHeader, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(authHeader);
            userService.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "409", description = "Duplicate fields")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @Operation(summary = "Update user")
    @PutMapping("/{id}")
    private User update(@RequestHeader String authHeader, @PathVariable int id, @Valid @RequestBody UserDto dto) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(authHeader);
            User userToUpdate = userMapper.fromDto(id, dto);
            return userService.update(userToUpdate, loggedUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @Operation(summary = "Make admin")
    @PutMapping("/{id}/make-admin")
    private User makeAdmin(@RequestHeader String authHeader, @PathVariable int id) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(authHeader);
            return userService.setAdmin(id, loggedUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @Operation(summary = "Block user")
    @PutMapping("/{id}/block")
    private User blockUser(@RequestHeader String authHeader, @PathVariable int id) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(authHeader);
            return userService.blockUser(id, loggedUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @Operation(summary = "Get all phones")
    @GetMapping(("/phones"))
    private List<Phone> getPhones() {
        try {
            return phoneService.getPhones();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @Operation(summary = "Get user's phone")
    @GetMapping(("/{id}/phones"))
    private Phone getUserPhone(@PathVariable int id) {
        try {
            userService.getByID(id);
            return phoneService.getPhoneByUser(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "409", description = "Duplicate fields")
    @Operation(summary = "Add phone to user")
    @PostMapping("/{id}/phones")
    private Phone addPhone(@RequestHeader String authHeader, @PathVariable int id,
                           @Valid @RequestBody Phone phone) {
        try {
            userService.getByID(id);
            User user = authenticationHelper.tryGetUser(authHeader);
            return phoneService.addPhone(phone, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @Operation(summary = "Delete phone from user")
    @DeleteMapping("/{id}/phones")
    private void deletePhone(@RequestHeader String authHeader, @PathVariable int id) {
        try {
            userService.getByID(id);
            User user = authenticationHelper.tryGetUser(authHeader);
            Phone phoneToDelete = phoneService.getPhoneByUser(id);
            phoneService.deletePhone(phoneToDelete, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "409", description = "Duplicate fields")
    @Operation(summary = "Update user's phone")
    @PutMapping("/{id}/phones")
    private Phone updatePhone(@RequestHeader String authHeader, @PathVariable int id,
                              @Valid @RequestBody Phone phone) {
        try {
            userService.getByID(id);
            User user = authenticationHelper.tryGetUser(authHeader);
            Phone phoneToUpdate = phoneService.getPhoneByUser(id);
            phoneToUpdate.setPhoneNumber(phone.getPhoneNumber());
            return phoneService.updatePhone(phoneToUpdate, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


}
