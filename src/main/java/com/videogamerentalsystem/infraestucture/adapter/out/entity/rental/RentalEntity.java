package com.videogamerentalsystem.infraestucture.adapter.out.entity.rental;


import com.videogamerentalsystem.domain.model.rental.constant.RentalPaymentType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "RENTAL")
public class RentalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(name = "VERSION")
    private Long version;

    @Column(name = "DATE")
    private LocalDateTime date;

    @Column(name = "CURRENCY")
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_TYPE")
    private RentalPaymentType paymentType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CUSTOMER_ID")
    private RentalCustomerEntity customer;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "rental", cascade = CascadeType.ALL)
    public List<RentalProductEntity> rentalProducts;

}
