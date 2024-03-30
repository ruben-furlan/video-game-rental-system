
# Sistema de alquiler de videojuegos

Software para gestionar la administración del alquiler, envetario y puntos de clientes.

## Installation

- Clona este repositorio en tu máquina local:

```bash
 git clone https://github.com/ruben-furlan/video-game-rental-system.git
```
    
- Navega al directorio raíz del proyecto:

```bash
cd video-game-rental-system
```

- Compila y ejecuta la aplicación usando Maven

```bash
mvn spring-boot:run
```

- Ejecuta los test


```bash
mvn test
```

- Acceso a la base H2 en Memoria:

```bash
http://localhost:8080/h2-console
```


## Documentation

[POSTMAN](https://documenter.getpostman.com/view/5508639/2sA35G4hCs#intro)



## Demo


### Api-Inventario
> [!NOTE] 
> - Agrega por items individuales
> - Consulta por items individuales

> [!TIP] 
> - AGREGAR JUEGOS AL INVENTARIO
> - Te genera un ID unico que representa el id del juego en el inventario

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

```bash
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
}'
```
> [!TIP] 
> - BUSCAR UN JUEGO EN EL INVENTARIO

```bash
curl --location 'http://localhost:8080/v1/video-game-rental-system/inventory/1'
```

### Api-Renta
> [!NOTE] 
> - Cada producto en la renta tiene estado (en proceso/finalizado) una vez se devuelva
> - Las rentas impactan sobre el stock
> - No puede comprar mismo titulo dos veces
> - No puedes comprar si no hay stocl
> - No puedes crear reservas con fechas/hora a la actual

> [!TIP] 
> - CREAMOS UNA NUEVA RENTA CON VARIOS PRODUCTOS
> - Generar una nueva renta de juego
> - Con la compra se resta del stock del inventario el juego
> - Se combra impuesto por dias falla en entrega
> - Los Ids de los productos en la RESPUESTA estan atados a A TU RESERVA NO al inventario
> - La respuesta del servicio te retorna los precios y estado de los productos

> [!WARNING] 
> - EDITA LA FECHA DE ENTREGA DE PRODUCTO (end_date)
>  - RESPETA EL FORMATO

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
            "title": "No Mans Sky",
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

> [!TIP] 
> - OBTENER ESTADO DE LA RENTA POR RETANL_ID
```bash
curl --location 'http://localhost:8080/v1/video-game-rental-system/rental/1'
```

> [!TIP] 
> - DEVOLVER UN JUEGO DE LA RENTA (RENTA_ID) Y (ID_PRODDUCT DE LA RENTA)

> [!IMPORTANT] 
> - Los datos que se solicitan son de la reserva (id de renta y de prodcuto que esta en la renta)
> - Una vez retornas el producto
> - Se agrega el producto al stock
> - Se tiene impuesto por demora
> - El estado del producto en la reserva pasa a FINISH
> - (Cuando todos los productos de una renta estan finalizados, se asume que la renta termino)

```bash
curl --location --request PUT 'http://localhost:8080/v1/video-game-rental-system/rental/1/hand-back/game?product_id=4'
```


> [!TIP] 
> - OBTENER ESTADO DE LA RENTA POR RETANL_ID  (Ver el producto finalizado)
```bash
curl --location 'http://localhost:8080/v1/video-game-rental-system/rental/1'
```


> [!WARNING] 
> VER PUNTOS LOYALTY ES NECESARIO DEJECUTAR UNA QUERY 

> [!CAUTION] 
> PARA PROBAR ENTREGA DEMORADA INGRESA A LA BASE DE DATOS Y ACTUALIZA EL REGISTRO DE LA FECHA
> POR API PIERDE EL SENTIDO CREAR REGISTROS DE RENTA CON FECHAS PASADAS


## Authors

- [@ruben-furlan](https://github.com/ruben-furlan)

