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
public class Categories extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	String name;

	@Column(length = 63, unique = true)
	String slug;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	Categories parent;

	@Column(columnDefinition = "TEXT")
	String config;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	Set<Categories> children = new HashSet<>();

	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
	Set<Products> products;
}
