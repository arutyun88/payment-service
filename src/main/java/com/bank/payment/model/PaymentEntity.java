package com.bank.payment.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "payment_table")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_value")
    private double paymentValue;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
