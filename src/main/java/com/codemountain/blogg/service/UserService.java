package com.codemountain.blogg.service;

import com.codemountain.blogg.exception.AccessDeniedException;
import com.codemountain.blogg.exception.ResourceNotFoundException;
import com.codemountain.blogg.exception.UnauthorizedException;
import com.codemountain.blogg.model.user.Address;
import com.codemountain.blogg.model.user.Company;
import com.codemountain.blogg.model.user.Geo;
import com.codemountain.blogg.model.user.User;
import com.codemountain.blogg.payload.request.InfoRequest;
import com.codemountain.blogg.payload.response.ApiResponse;
import com.codemountain.blogg.payload.response.UserProfile;
import com.codemountain.blogg.payload.response.UserSummary;
import com.codemountain.blogg.repository.PostRepository;
import com.codemountain.blogg.repository.UserRepository;
import com.codemountain.blogg.security.CurrentUser;
import com.codemountain.blogg.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserSummary getCurrentUser(UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getFirstName(),
                currentUser.getLastName());
    }

    @Transactional(readOnly = true)
    public UserProfile getUserProfile(String username) {
        User user = userRepository.getUserByName(username);

        Long postCount = postRepository.countByCreatedBy(user.getId());

        return new UserProfile(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(),
                user.getCreatedAt(), user.getEmail(), user.getBio(), user.getAddress(), user.getPhone(), user.getWebsite(),
                user.getCompany(), postCount);
    }

    @Transactional
    public User updateUser(User newUser, String username, UserPrincipal currentUser) {
        User user = userRepository.getUserByName(username);
        if (user.getId().equals(currentUser.getId())) {
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
            user.setAddress(newUser.getAddress());
            user.setPhone(newUser.getPhone());
            user.setWebsite(newUser.getWebsite());
            user.setCompany(newUser.getCompany());

            return userRepository.save(user);

        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE,
                "You don't have permission to update profile of: " + username);
        throw new UnauthorizedException(apiResponse);

    }

    @Transactional
    public UserProfile updateInfo(UserPrincipal currentUser, InfoRequest infoRequest) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getUsername()));

        Geo geo = new Geo(infoRequest.getLat(), infoRequest.getLng());

        Address address = new Address(infoRequest.getStreet(), infoRequest.getSuite(), infoRequest.getCity(),
                infoRequest.getZipcode(), geo);

        Company company = new Company(infoRequest.getCompanyName(), infoRequest.getCatchPhrase(), infoRequest.getBs());

        if (user.getId().equals(currentUser.getId())) {
            user.setAddress(address);
            user.setCompany(company);
            user.setWebsite(infoRequest.getWebsite());
            user.setPhone(infoRequest.getPhone());
            user.setBio(infoRequest.getBio());
            User updatedUser = userRepository.save(user);

            Long postCount = postRepository.countByCreatedBy(updatedUser.getId());

            return new UserProfile(updatedUser.getId(), updatedUser.getUsername(),
                    updatedUser.getFirstName(), updatedUser.getLastName(), updatedUser.getCreatedAt(),
                    updatedUser.getEmail(), updatedUser.getBio(), updatedUser.getAddress(), updatedUser.getPhone(), updatedUser.getWebsite(),
                    updatedUser.getCompany(), postCount);
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE,
                "You don't have permission to update users profile", HttpStatus.FORBIDDEN);
        throw new UnauthorizedException(apiResponse);
    }

    @Transactional
    public ApiResponse deleteUser(String username, UserPrincipal currentUser) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", username));
        if (user.getId().equals(currentUser.getId())) {
            userRepository.deleteById(user.getId());

            return new ApiResponse(Boolean.TRUE, "You successfully deleted profile of: " + username);
        }
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE,
                "You don't have permission to delete profile of: " + username);
        throw new UnauthorizedException(apiResponse);
    }

}
