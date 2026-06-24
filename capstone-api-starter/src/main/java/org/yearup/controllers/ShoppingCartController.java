package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("cart")
@CrossOrigin("*")
@PreAuthorize("hasRole('ROLE_USER')")
public class ShoppingCartController
{
    // a shopping cart controller depends on the service layer
    private ShoppingCartService shoppingCartService;
    private UserService userService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ShoppingCart> getCart(Principal principal)
    {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();



        return ResponseEntity.ok(shoppingCartService.getByUserId(userId));
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15  (15 is the productId to be added)
    // return the updated cart with status 201 Created
    @PostMapping("products/{id}")
    public ResponseEntity<ShoppingCart> addToCart(Principal principal,@PathVariable int id){

        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();


        return ResponseEntity.status(201).body(shoppingCartService.addToCart(userId,id));
    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15  (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated; return the cart (200 OK)
    @PutMapping("products/{id}")
    public ResponseEntity<ShoppingCart> addMoreToCart(Principal principal,@PathVariable int id){

        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();


        return null;
    }

    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart  - return the (now empty) cart so the front end can refresh it (200 OK)
    @DeleteMapping
    public ResponseEntity<ShoppingCart> clearCart(Principal principal){
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        return ResponseEntity.ok(shoppingCartService.clearCart(userId));
    }
}
