package com.videogamerentalsystem.infraestucture.adapter.out.mapper.rental;

import com.videogamerentalsystem.domain.model.rental.RentalCustomerModel;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.rental.RentalCustomerEntity;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.rental.RentalEntity;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.rental.RentalProductEntity;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class RentalMapper {

    public RentalEntity toRentalEntity(RentalModel rentalModel) {
        RentalEntity.RentalEntityBuilder rentalEntityBuilder = RentalEntity.builder();

        RentalCustomerModel customerModel = rentalModel.getCustomerModel();
        RentalCustomerEntity.RentalCustomerEntityBuilder customerEntityBuilder = RentalCustomerEntity.builder();

        RentalCustomerEntity rentalCustomerEntity = this.toRentalCustomerEntity(customerModel, customerEntityBuilder);

        Set<RentalProductEntity> rentalProducts = this.toRentalProductsEntity(rentalModel.getProductModels(), rentalEntityBuilder.build());
        RentalEntity rentalEntity = rentalEntityBuilder.date(rentalModel.getDate())
                .currency(rentalModel.getCurrency())
                .paymentType(rentalModel.getPaymentType())
                .rentalProducts(rentalProducts)
                .customer(rentalCustomerEntity)
                .build();

        this.joinRelationship(rentalCustomerEntity, rentalProducts, rentalEntity);

        return rentalEntity;
    }

    private  RentalCustomerEntity toRentalCustomerEntity(RentalCustomerModel customerModel, RentalCustomerEntity.RentalCustomerEntityBuilder customerEntityBuilder) {
        return customerEntityBuilder.firstName(customerModel.getFirstName())
                .latName(customerModel.getLatName())
                .loyaltyPoints(1)
                .build();
    }

    private void joinRelationship(RentalCustomerEntity customer, Set<RentalProductEntity> rentalProducts, RentalEntity rentalEntity) {
        customer.setRental(rentalEntity);
        this.jointProductEntityToRental(rentalProducts, rentalEntity);
    }


    private Set<RentalProductEntity> toRentalProductsEntity(Set<RentalProductModel> rentalProductModels, RentalEntity rentalEntity) {
        return CollectionUtils.isEmpty(rentalProductModels) ? null : rentalProductModels
                .stream()
                .map(rentalProductModel -> this.toRentalProductEntity(rentalProductModel, rentalEntity))
                .collect(Collectors.toSet());
    }

    private RentalProductEntity toRentalProductEntity(RentalProductModel rentalProductModel, RentalEntity rentalEntity) {
        return RentalProductEntity.builder()
                .title(rentalProductModel.getTitle())
                .type(rentalProductModel.getType())
                .endDate(rentalProductModel.getEndDate())
                .price(rentalProductModel.getPrice())
                .rental(rentalEntity)
                .build();
    }

    public RentalModel toRentalModel(RentalEntity rentalEntity) {
        return RentalModel.builder()
                .id(rentalEntity.getId())
                .date(rentalEntity.getDate())
                .currency(rentalEntity.getCurrency())
                .paymentType(rentalEntity.getPaymentType())
                .customerModel(this.toRentalCustomerModel(rentalEntity.getCustomer()))
                .productModels(this.toRentalProductsModel(rentalEntity.getRentalProducts()))
                .build();
    }

    public RentalCustomerModel toRentalCustomerModel(RentalCustomerEntity rentalCustomerEntity){
     return    RentalCustomerModel.builder()
                .firstName(rentalCustomerEntity.getFirstName())
                .latName(rentalCustomerEntity.getLatName())
                .loyaltyPoints(rentalCustomerEntity.getLoyaltyPoints())
                .build();
    }

    private Set<RentalProductModel> toRentalProductsModel(Set<RentalProductEntity> rentalProductEntities) {
        return CollectionUtils.isEmpty(rentalProductEntities) ? null : rentalProductEntities
                .stream()
                .map(this::toRentalProductModel)
                .collect(Collectors.toSet());
    }

    public RentalProductModel toRentalProductModel(RentalProductEntity rentalProductEntity) {
        return RentalProductModel.builder()
                .id(rentalProductEntity.getId())
                .title(rentalProductEntity.getTitle())
                .type(rentalProductEntity.getType())
                .endDate(rentalProductEntity.getEndDate())
                .price(rentalProductEntity.getPrice())
                .build();
    }


    private void jointProductEntityToRental(Set<RentalProductEntity> rentalProducts, RentalEntity rentalEntity) {
        if (CollectionUtils.isEmpty(rentalProducts) == Boolean.FALSE) {
            rentalProducts.forEach(join -> join.setRental(rentalEntity));
        }
    }

}
