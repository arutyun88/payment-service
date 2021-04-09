package com.bank.payment.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "user_token")
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user_id;

    @Column(name = "token_name")
    private String tokenName;

    @Column(name = "expire_date")
    private Date expireDate;
}
