package com.example.pancharm.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Collections extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	@Column(unique = true)
	String name;

	@Column(length = 63)
	String slug;

	int status;

	@Column(columnDefinition = "TEXT")
	String description;

	@OneToMany(mappedBy = "collection", fetch = FetchType.LAZY)
	Set<CollectionImages> images = new HashSet<>();

	@ManyToMany(mappedBy = "collections")
	Set<Products> products = new HashSet<>();
}
