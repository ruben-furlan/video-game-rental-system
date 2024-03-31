package com.videogamerentalsystem.infraestucture.exception.custom;

public abstract class ApiExceptionConstantsMessagesError {
    public final class Generic {
        public final static String MESSAGE_GENERIC = "Oops - internal error, product not found, please contact the development team.";
    }
    public final class Rental {
        public final static String PRODUCT_FINISH = "The product  %d finished, please check the rent: %d ";
        public final static String RENTAl_NOT_FOUND = "RENTAL_ID_NOT_FOUND: %d";
        public final static String PRODUCT_TITLE_NOT_FOUND = "PRODUCT_TITLE_NOT_FOUND";

    }

    public final class GameInventory {
        public final static String TITLE_ALREADY_PRESENT = "The title '%s' is already present. Please check the inventory, as it may not have enough stock.";
        public final static String NOT_STOCK = "There is not enough stock available to create the rental. Please, review the inventory.";

    }
}


