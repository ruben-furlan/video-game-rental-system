package com.videogamerentalsystem.infraestucture.exception.custom;

public abstract class ApiExceptionConstantsMessagesError {
    public final static String MESSAGE_GENERIC = "Oops - internal error, product not found, please contact the development team.";
    public final static String RENTAL_NOT_FOUND = "RENTAL_NOT_FOUND";
    public final static String PRODUCT_TITLE_NOT_FOUND = "PRODUCT_TITLE_NOT_FOUND";

    public final static String NOT_STOCK = "There is not enough stock available to create the rental. Please, review the inventory.";
}
