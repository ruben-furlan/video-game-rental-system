
# Sistema de alquiler de videojuegos

Software para gestionar la administración del alquiler, envetario y puntos de clientes.





## Installation

Clona este repositorio en tu máquina local:

```bash
 git clone https://github.com/ruben-furlan/video-game-rental-system.git
```
    
Navega al directorio raíz del proyecto:

```bash
cd video-game-rental-system
```

Compila y ejecuta la aplicación usando Maven

```bash
mvn spring-boot:run
```

Ejecuta los test


```bash
mvn test
```


## Documentation

[Postman Documenter](https://documenter.getpostman.com/view/5508639/2sA35G4hCs#intro)







## Demo

### [API Inventario](#api-inventario-1)
- [Ejemplo de Creación de Inventario - Fallout 4](#ejemplo-de-creación-de-inventario---fallout-4)
- [Ejemplo de Creación de Inventario - Resident Evil 6](#ejemplo-de-creación-de-inventario---resident-evil-6)
- [Ejemplo de Creación de Inventario - Fallout 3](#ejemplo-de-creación-de-inventario---fallout-3)
- [Ejemplo de Creación de Inventario - No Man’s Sky](#ejemplo-de-creación-de-inventario---no-mans-sky)
- [Buscar un Juego en el Inventario por el ID](#buscar-un-juego-en-el-inventario-por-el-id)

### [API Renta](#api-renta)
- [Ejemplo de Creación de Renta](#ejemplo-de-creación-de-renta)
- [Obtener Estado de la Renta por ID](#obtener-estado-de-la-renta-por-id)
- [Retornar un Juego de la Venta](#retornar-un-juego-de-la-venta)
- [Obtener Estado de la Renta por ID (Ver el Producto Finalizado)](#obtener-estado-de-la-renta-por-id-ver-el-producto-finalizado)






### Api Inventario
- STANDARD -Fallout 4
```bash
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
}'
```
- STANDARD - Resident Evil 6
```bash
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
}'
```
- CLASSIC - Fallout 3
```bash
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
}'
```
- NEW_RELEASE No Mans sky
```bash
curl --location 'http://localhost:8080/v1/video-game-rental-system/inventory' \
--header 'Content-Type: application/json' \
--data '{
    "title": "No Man’s Sky",
    "type": "NEW_RELEASE",
    "stock": 10,
    "price": {
        "type": "PREMIUM",
        "amount": 4,
        "currency": "EUR"
    }
}'
```

- Buscar un  juego en el inventario por el ID
```bash
curl --location 'http://localhost:8080/v1/video-game-rental-system/inventory/1'
```

### Api Renta
- Creamos una renta con varios productos
```bash
curl --location 'http://localhost:8080/v1/video-game-rental-system/rental' \
--header 'Content-Type: application/json' \
--data '{
    "currency": "EUR",
    "payment_type": "CASH",
    "customer": {
        "first_name": "John",
        "last_name": "Doe",
        "document_number":"34213213-d"
    },
    "products": [
        {
            "title": "No Man’s Sky",
            "end_date": "2024-03-29T22:00:00"
        },
        {
            "title": "Resident Evil 6",
            "end_date": "2024-04-03T12:00:00"
        },
        {
            "title": "Fallout 4",
            "end_date": "2024-03-31T12:00:00"
        },
           {
            "title": "Fallout 3",
            "end_date": "2024-04-05T12:00:00"
        }
    ]
}'
```

- Obtener estado de la renta por ID

```bash
curl --location 'http://localhost:8080/v1/video-game-rental-system/rental/1'
```


- Retornar un Juego de la venta (RENTA_ID) Y (ID_PRODUCTO de la reserva))

```bash
curl --location --request PUT 'http://localhost:8080/v1/video-game-rental-system/rental/1/hand-back/game?product_id=4'
```

- Obtener estado de la renta por ID (Ver el producto finalizado)

```bash
curl --location 'http://localhost:8080/v1/video-game-rental-system/rental/1'
```


## Authors

- [@ruben-furlan](https://github.com/ruben-furlan)

