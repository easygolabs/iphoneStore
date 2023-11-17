# Getting Started

### 1. Up docker containers:

   ```bash
   cd src/ 
   docker compose up -d --build
   ```

### 2. Registration

#### **Register a new manager:**

 ```bash
 curl -X POST -H "Content-Type: application/json" -d '{
     "username": "manager", 
     "password": "123", 
     "role": "MANAGER"
 }' 'http://localhost:8080/registration'
 ```

#### **Register a new client:**

 ```bash
 curl -X POST -H "Content-Type: application/json" -d '{
     "username": "client", 
     "password": "123", 
     "role": "CLIENT"
 }' 'http://localhost:8080/registration'
 ```

### 3. Add goods
First:
 ```bash
 curl -X POST -H "Content-Type: application/json" -H "Authorization: Basic bWFuYWdlcjoxMjM=" -d '{
     "name": "Iphone 13", 
     "price": 900, 
     "quantity": 5
 }' 'http://localhost:8080/api/v1/goods/'
 ```
Second:
 ```bash
 curl -X POST -H "Content-Type: application/json" -H "Authorization: Basic bWFuYWdlcjoxMjM=" -d '{
     "name": "Iphone 14", 
     "price": 1000, 
     "quantity": 10
 }' 'http://localhost:8080/api/v1/goods/'
 ```
Third:
 ```bash
 curl -X POST -H "Content-Type: application/json" -H "Authorization: Basic bWFuYWdlcjoxMjM=" -d '{
     "name": "Iphone 15 Pro", 
     "price": 1600, 
     "quantity": 20
 }' 'http://localhost:8080/api/v1/goods/'
 ```
! Make sure it will not be added as the client's authority:
 ```bash
 curl -X POST -H "Content-Type: application/json" -H "Authorization: Basic Y2xpZW50OjEyMw==" -d '{
     "name": "client haha", 
     "price": 900, 
     "quantity": 5
 }' 'http://localhost:8080/api/v1/goods/'
 ```

### 4. To show the list of goods:

```bash
curl -X GET 'localhost:8080/api/v1/goods/'
```

### 5. Place order:

```bash
curl -X POST -H "Content-Type: application/json" -H "Authorization: Basic Y2xpZW50OjEyMw==" -d '[
    {
        "id": 1,
        "quantity": 1
    },
    {
        "id": 2,
        "quantity": 5
    }
]' 'http://localhost:8080/api/orders/?userId=1'
```

```bash
curl -X POST -H "Content-Type: application/json" -H "Authorization: Basic Y2xpZW50OjEyMw==" -d '[
    {
        "id": 3,
        "quantity": 5
    }
]' 'http://localhost:8080/api/orders/?userId=2'
```

### 5. Pay for order:
```bash
curl -X PUT -H "Authorization: Basic bWFuYWdlcjoxMjM=" 'http://localhost:8080/api/orders/1/payment'
```