package com.videogamerentalsystem.infraestucture.adapter.out.entity.rental;

import com.videogamerentalsystem.domain.model.inventory.constant.GameType;
import com.videogamerentalsystem.domain.model.rental.constant.RentalProductStatus;
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
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "RENTAL_PRODUCT")
public class RentalProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(name = "VERSION")
    private Long version;


    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private RentalProductStatus status;

    @Column(name = "TITLE")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private GameType type;

    @Column(name = "END_DATE")
    private LocalDateTime endDate;

    @Column(name = "PRICE")
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "RENTAL_ID")
    private RentalEntity rental;

    public void addJoin(RentalEntity rentalEntity) {
        this.rental = rentalEntity;
    }

    public void updatePriceAndStatus(BigDecimal price,RentalProductStatus status){
        this.price=price;
        this.status=status;
    }
}
