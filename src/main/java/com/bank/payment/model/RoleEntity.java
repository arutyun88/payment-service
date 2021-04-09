package com.bank.payment.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "role_table")
@Data
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
}
