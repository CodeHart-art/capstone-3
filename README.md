# The Store API

## Overview

The Store is a Spring Boot e-commerce application that provides the backend services for an online shopping platform. The application allows customers to browse products, manage shopping carts, register accounts, and place orders through a RESTful API connected to a MySQL database.

The primary focus of the project was backend development, bug fixing, API enhancements, and implementing new features while working within an existing codebase.

---

## Features

### User Features

* User registration and authentication
* Secure login with JWT authentication
* Browse product catalog
* Search and filter products
* View products by category
* Manage shopping cart
* Add items to cart
* Update cart quantities
* Remove items from cart

### Admin Features

* Create new categories
* Update existing categories
* Delete categories
* Create products
* Update product information
* Delete products
* Manage store inventory

### API Features

* RESTful API architecture
* Spring Security authorization
* Role-based access control
* MySQL database integration
* JPA Repository pattern
* Exception handling
* JSON request and response support

---

## Technologies Used

### Backend

* Java
* Spring Boot
* Spring Security
* Spring Data JPA
* Maven

### Database

* MySQL

### Testing Tools

* Insomnia

### Version Control

* Git
* GitHub

---

## Database Setup

1. Create a MySQL database.
2. Run the provided SQL script included with the starter project.
3. Update the `application.properties` file with your database credentials.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/easyshop
spring.datasource.username=root
spring.datasource.password=yourpassword
```

---

## Running the Application

1. Clone the repository:

```bash
git clone https://github.com/yourusername/the-store-api.git
```

2. Open the project in IntelliJ IDEA.

3. Configure the database connection.

4. Run the Spring Boot application.

5. Verify the API is running:

```text
http://localhost:8080
```

---

## API Endpoints

### Authentication

| Method | Endpoint  |
| ------ | --------- |
| POST   | /register |
| POST   | /login    |

### Categories

| Method | Endpoint         |
| ------ | ---------------- |
| GET    | /categories      |
| GET    | /categories/{id} |
| POST   | /categories      |
| PUT    | /categories/{id} |
| DELETE | /categories/{id} |

### Shopping Cart

| Method | Endpoint                   |
| ------ | -------------------------- |
| GET    | /cart                      |
| POST   | /cart/products/{productId} |
| PUT    | /cart/products/{productId} |
| DELETE | /cart                      |

---

## Bugs Fixed

During development several issues in the starter code were identified and corrected, including:

* Product filtering logic
* Category endpoint issues
* Shopping cart functionality
* Authentication and authorization errors
* Data validation improvements
* API response handling

---

## Challenging Code

One of the most challenging parts of this project was:

```java
 public ShoppingCart addToCart(int userId, int id){

    CartItem cartItem = new CartItem();

    cartItem.setUserId(userId);
    cartItem.setProductId(id);

    CartItem existingCartItem = shoppingCartRepository.findByUserIdAndProductId(userId,id);

    if (existingCartItem != null){
        existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
        shoppingCartRepository.save(existingCartItem);
    }
    else {
        shoppingCartRepository.save(cartItem);
    }

    return getByUserId(userId);
}
```

### Why It Was Challenging

Adding a user's item to the cart was challenging because I had to account for whether the item existed, and if it didn't exist, I had to add it to the cart.


---

## Favorite Code

The piece of code I am most proud of is:

```java
 @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        categoryService.delete(id);
        return ResponseEntity.status(204).build();
```

### Why It Is My Favorite

easiest function to implement


## Author

John Hart

Capstone Project
