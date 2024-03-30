package com.videogamerentalsystem.infraestucture.adapter.out.mapper.rental;

import com.videogamerentalsystem.domain.model.rental.RentalCustomerModel;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductChargeModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.rental.RentalCustomerEntity;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.rental.RentalEntity;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.rental.RentalProductEntity;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class RentalMapper {

    public RentalEntity toRentalEntity(RentalModel rentalModel) {
        RentalEntity.RentalEntityBuilder rentalEntityBuilder = RentalEntity.builder();
        RentalCustomerEntity rentalCustomerEntity = this.toRentalCustomerEntity(rentalModel.getCustomerModel());
        List<RentalProductEntity> rentalProductEntities = this.toRentalProductsEntity(rentalModel.getProductModels());
        RentalEntity rentalEntity = rentalEntityBuilder.date(rentalModel.getDate())
                .currency(rentalModel.getCurrency())
                .paymentType(rentalModel.getPaymentType())
                .rentalProducts(rentalProductEntities)
                .customer(rentalCustomerEntity)
                .build();

        this.joinRelationship(rentalCustomerEntity, rentalProductEntities, rentalEntity);

        return rentalEntity;
    }

    private RentalCustomerEntity toRentalCustomerEntity(RentalCustomerModel customerModel) {
        return RentalCustomerEntity.builder()
                .firstName(customerModel.getFirstName())
                .latName(customerModel.getLatName())
                .documentNumber(customerModel.getDocumentNumber())
                .loyaltyPoints(customerModel.getLoyaltyPoints())
                .build();
    }

    private void joinRelationship(RentalCustomerEntity customer, List<RentalProductEntity> rentalProducts, RentalEntity rentalEntity) {
        customer.addJoin(rentalEntity);
        this.jointProductEntityToRental(rentalProducts, rentalEntity);
    }


    private List<RentalProductEntity> toRentalProductsEntity(List<RentalProductModel> rentalProductModels) {
        return CollectionUtils.isEmpty(rentalProductModels) ? null : rentalProductModels
                .stream()
                .map(this::toRentalProductEntity)
                .collect(Collectors.toList());
    }

    private RentalProductEntity toRentalProductEntity(RentalProductModel rentalProductModel) {
        RentalProductEntity.RentalProductEntityBuilder builder = RentalProductEntity.builder();
        builder.title(rentalProductModel.getTitle())
                .status(rentalProductModel.getStatus())
                .type(rentalProductModel.getType())
                .endDate(rentalProductModel.getEndDate());

        RentalProductChargeModel charges = rentalProductModel.getCharges();
        if(Objects.nonNull(charges)){
            builder.price(charges.getPrice());
        }

        return builder.build();
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

    public RentalCustomerModel toRentalCustomerModel(RentalCustomerEntity rentalCustomerEntity) {
        return RentalCustomerModel.builder()
                .firstName(rentalCustomerEntity.getFirstName())
                .latName(rentalCustomerEntity.getLatName())
                .loyaltyPoints(rentalCustomerEntity.getLoyaltyPoints())
                .build();
    }

    private List<RentalProductModel> toRentalProductsModel(List<RentalProductEntity> rentalProductEntities) {
        return CollectionUtils.isEmpty(rentalProductEntities) ? null : rentalProductEntities
                .stream()
                .map(this::toRentalProductModel)
                .collect(Collectors.toList());
    }

    public RentalProductModel toRentalProductModel(RentalProductEntity rentalProductEntity) {
        return RentalProductModel.builder()
                .id(rentalProductEntity.getId())
                .title(rentalProductEntity.getTitle())
                .status(rentalProductEntity.getStatus())
                .type(rentalProductEntity.getType())
                .endDate(rentalProductEntity.getEndDate())
                .charges(RentalProductChargeModel.builder().price(rentalProductEntity.getPrice()).build())
                .build();
    }


    private void jointProductEntityToRental(List<RentalProductEntity> rentalProducts, RentalEntity rentalEntity) {
        if (CollectionUtils.isEmpty(rentalProducts) == Boolean.FALSE) {
            rentalProducts.forEach(rentalProduct -> rentalProduct.addJoin(rentalEntity));
        }
    }

}
