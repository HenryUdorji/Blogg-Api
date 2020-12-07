package com.codemountain.blogg.service;

import com.codemountain.blogg.exception.ResourceNotFoundException;
import com.codemountain.blogg.model.Category;
import com.codemountain.blogg.model.Post;
import com.codemountain.blogg.model.Tag;
import com.codemountain.blogg.model.user.User;
import com.codemountain.blogg.payload.request.PostRequest;
import com.codemountain.blogg.payload.response.PostResponse;
import com.codemountain.blogg.repository.CategoryRepository;
import com.codemountain.blogg.repository.PostRepository;
import com.codemountain.blogg.repository.TagRepository;
import com.codemountain.blogg.repository.UserRepository;
import com.codemountain.blogg.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.codemountain.blogg.utils.Constant.*;

@AllArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public PostResponse createPost(PostRequest postRequest, UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException(USER, ID, 1L));

        Category category = categoryRepository.findById(postRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY, ID, postRequest.getCategoryId()));

        List<Tag> tags = new ArrayList<>(postRequest.getTags().size());

        for (String name : postRequest.getTags()) {
            Tag tag = tagRepository.findByName(name);
            tag = tag == null ? tagRepository.save(new Tag(name)) : tag;

            tags.add(tag);
        }

        Post post = new Post();
        post.setBody(postRequest.getBody());
        post.setTitle(postRequest.getTitle());
        post.setCategory(category);
        post.setIsPublished(postRequest.getIsPublished());
        post.setUser(user);
        post.setTags(tags);

        Post newPost = postRepository.save(post);

        PostResponse postResponse = new PostResponse();

        postResponse.setTitle(newPost.getTitle());
        postResponse.setBody(newPost.getBody());
        postResponse.setCategory(newPost.getCategory().getName());

        List<String> tagNames = new ArrayList<>(newPost.getTags().size());

        for (Tag tag : newPost.getTags()) {
            tagNames.add(tag.getName());
        }

        postResponse.setTags(tagNames);

        return postResponse;
    }
}
