package com.videogamerentalsystem.infraestucture.adapter.out.entity.rental;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter

@Entity
@Table(name = "RENTAL_CUSTOMER")
public class RentalCustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(name = "VERSION")
    private Long version;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String latName;

    @Column(name = "DOCUMENT_NUMBER")
    private String documentNumber;

    @Column(name = "LOYALTY_POINTS")
    private Integer loyaltyPoints;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "RENTAL_ID")
    public RentalEntity rental;

    public void addJoin(RentalEntity rental) {
        this.rental = rental;
    }
}
