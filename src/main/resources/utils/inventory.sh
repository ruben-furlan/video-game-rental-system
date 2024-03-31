#!/bin/bash


handle_error() {
    echo "Error: La solicitud curl ha fallado en la línea $1"
    exit 1
}


curl --location 'http://localhost:8080/v1/video-game-rental-system/inventory' \
--header 'Content-Type: application/json' \
--data '{
   "title": "Fallout 4",
    "type": "STANDARD",
    "stock": 12,
    "price": {
        "type": "BASIC",
        "amount": 3,
        "currency": "EUR"
    }
}' || handle_error $LINENO &


curl --location 'http://localhost:8080/v1/video-game-rental-system/inventory' \
--header 'Content-Type: application/json' \
--data '{
    "title": "Resident Evil 6",
    "type": "STANDARD",
    "stock": 8,
    "price": {
        "type": "BASIC",
        "amount": 3,
        "currency": "EUR"
    }
}' || handle_error $LINENO &


curl --location 'http://localhost:8080/v1/video-game-rental-system/inventory' \
--header 'Content-Type: application/json' \
--data '{
    "title": "Fallout 3",
    "type": "CLASSIC",
    "stock": 5,
    "price": {
        "type": "BASIC",
        "amount": 3,
        "currency": "EUR"
    }
}' || handle_error $LINENO &


curl --location 'http://localhost:8080/v1/video-game-rental-system/inventory' \
--header 'Content-Type: application/json' \
--data '{
    "title": "No Mans Sky",
    "type": "NEW_RELEASE",
    "stock": 10,
    "price": {
        "type": "PREMIUM",
        "amount": 4,
        "currency": "EUR"
    }
}' || handle_error $LINENO &


wait

echo "Todas las solicitudes han sido completadas correctamente."