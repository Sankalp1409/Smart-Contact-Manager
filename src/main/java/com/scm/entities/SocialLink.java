// package com.scm.entities;

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;
// import lombok.Setter;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;

// @Entity
// @Table(name = "sociallinks")
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class SocialLink {

// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private long id;
// private String link;
// private String title;

// @ManyToOne
// private Contact contact;
// }