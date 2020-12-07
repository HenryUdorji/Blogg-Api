package com.codemountain.blogg.payload.response;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class PagedResponse<T> {
	private List<T> content;
	private Integer page;
	private Integer size;
	private Long totalElements;
	private Integer totalPages;
	private Boolean last;

	public PagedResponse() {

	}

	public PagedResponse(List<T> content, Integer page, Integer size, Long totalElements, Integer totalPages, Boolean last) {
		setContent(content);
		this.page = page;
		this.size = size;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.last = last;
	}

	public List<T> getContent() {
		return content == null ? null : new ArrayList<>(content);
	}

	public final void setContent(List<T> content) {
		if (content == null) {
			this.content = null;
		} else {
			this.content = Collections.unmodifiableList(content);
		}
	}



	public Boolean isLast() {
		return last;
	}
}
