package com.foodsquad.FoodSquad.model.entity;


import com.foodsquad.FoodSquad.service.admin.dec.PaymentMethodStrategy;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private BigDecimal amount;
   @Column(name = "payment_date"  , nullable = false)
    private LocalDateTime paymentDate;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @Transient
    private PaymentMethodStrategy paymentMethodStrategy;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
