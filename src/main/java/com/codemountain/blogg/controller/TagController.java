package com.codemountain.blogg.controller;

import com.codemountain.blogg.model.Tag;
import com.codemountain.blogg.payload.response.PagedResponse;
import com.codemountain.blogg.security.CurrentUser;
import com.codemountain.blogg.security.UserPrincipal;
import com.codemountain.blogg.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

	private final TagService tagService;

	/*@GetMapping
	public ResponseEntity<PagedResponse<Tag>> getAllTags(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		PagedResponse<Tag> response = tagService.getAllTags(page, size);

		return new ResponseEntity< >(response, HttpStatus.OK);
	}*/

	@PostMapping
	public ResponseEntity<Tag> createTag(@Valid @RequestBody Tag tag) {
		Tag newTag = tagService.createTag(tag);

		return new ResponseEntity< >(newTag, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Tag> getTag(@PathVariable(name = "id") Long id) {
		Tag tag = tagService.getTag(id);

		return new ResponseEntity< >(tag, HttpStatus.OK);
	}

	/*@PutMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<Tag> updateTag(@PathVariable(name = "id") Long id, @Valid @RequestBody Tag tag, @CurrentUser UserPrincipal currentUser) {

		Tag updatedTag = tagService.updateTag(id, tag, currentUser);

		return new ResponseEntity< >(updatedTag, HttpStatus.OK);
	}*/

	/*@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteTag(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal currentUser) {
		ApiResponse apiResponse = tagService.deleteTag(id, currentUser);

		return new ResponseEntity< >(apiResponse, HttpStatus.OK);
	}*/

}
