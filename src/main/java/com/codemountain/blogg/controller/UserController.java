package com.codemountain.blogg.controller;


import com.codemountain.blogg.model.user.User;
import com.codemountain.blogg.payload.request.InfoRequest;
import com.codemountain.blogg.payload.response.ApiResponse;
import com.codemountain.blogg.payload.response.UserProfile;
import com.codemountain.blogg.payload.response.UserSummary;
import com.codemountain.blogg.security.CurrentUser;
import com.codemountain.blogg.security.UserPrincipal;
import com.codemountain.blogg.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserSummary> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        UserSummary currentUser = userService.getCurrentUser(userPrincipal);
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @GetMapping("/{username}/profile")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable(value = "username") String username) {
        UserProfile userProfile = userService.getUserProfile(username);

        return new ResponseEntity< >(userProfile, HttpStatus.OK);
    }

    @PutMapping("/{username}")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User newUser,
                                           @PathVariable(value = "username") String username,
                                           @CurrentUser UserPrincipal currentUser) {
        User updatedUser = userService.updateUser(newUser, username, currentUser);

        return new ResponseEntity< >(updatedUser, HttpStatus.CREATED);
    }

    @PutMapping("/update-info")
    public ResponseEntity<UserProfile> setAddress(@CurrentUser UserPrincipal currentUser,
                                                  @Valid @RequestBody InfoRequest infoRequest) {
        UserProfile userProfile = userService.updateInfo(currentUser, infoRequest);

        return new ResponseEntity< >(userProfile, HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable(value = "username") String username,
                                                  @CurrentUser UserPrincipal currentUser) {
        ApiResponse apiResponse = userService.deleteUser(username, currentUser);

        return new ResponseEntity< >(apiResponse, HttpStatus.OK);
    }
}
