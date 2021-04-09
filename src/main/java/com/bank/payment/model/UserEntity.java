package com.bank.payment.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "user_table")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;

    @Column(name = "status")
    private boolean isActive;

    @Column(name = "balance")
    private double balance;

    @Column(name = "currency_type")
    private String currencyType;
}
