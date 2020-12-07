package com.codemountain.blogg.payload.response;
import com.codemountain.blogg.model.user.Address;
import com.codemountain.blogg.model.user.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfile {
	private Long id;
	private String username;
	private String firstName;
	private String lastName;
	private Instant joinedAt;
	private String email;
	private String bio;
	private Address address;
	private String phone;
	private String website;
	private Company company;
	private Long postCount;
}
