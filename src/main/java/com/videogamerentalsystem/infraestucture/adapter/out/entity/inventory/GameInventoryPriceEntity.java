package com.videogamerentalsystem.infraestucture.adapter.out.entity.inventory;

import com.videogamerentalsystem.domain.model.inventory.constant.GamePriceType;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "GAME_INVENTORY_PRICE")
public class GameInventoryPriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(name = "VERSION")
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private GamePriceType type;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "CURRENCY")
    private String currency;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "GAME_INVENTORY_ID")
    private GameInventoryEntity gameInventoryEntity;

    public void addJoin(GameInventoryEntity gameInventoryEntity) {
        this.gameInventoryEntity = gameInventoryEntity;
    }

}
