package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
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

    /**
     * Adds a product to the authenticated user's shopping cart.
     *
     * This endpoint retrieves the currently authenticated user from the Principal,
     * finds the user's ID, and adds the specified product to their shopping cart.
     * If the product already exists in the cart, its quantity is increased.
     *
     * @param principal the authenticated user's security information
     * @param id the ID of the product to add to the cart
     * @return a ResponseEntity containing the updated ShoppingCart with a 201 Created status
     */
    @PostMapping("products/{id}")
    public ResponseEntity<ShoppingCart> addToCart(Principal principal,@PathVariable int id){

        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();


        return ResponseEntity.status(201).body(shoppingCartService.addToCart(userId,id));
    }


    /**
     * Updates the quantity of a product in the authenticated user's shopping cart.
     *
     * This endpoint retrieves the authenticated user's ID from the Principal,
     * updates the quantity of the specified product using the provided
     * ShoppingCartItem, and returns the updated shopping cart.
     *
     * @param principal the authenticated user's security information
     * @param productId the ID of the product to update
     * @param item the ShoppingCartItem containing the updated quantity
     * @return a ResponseEntity containing the updated ShoppingCart
     */
    @PutMapping("products/{productId}")
    public ResponseEntity<ShoppingCart> addMoreToCart(Principal principal, @PathVariable int productId,@RequestBody ShoppingCartItem item){

        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();


        return ResponseEntity.ok(shoppingCartService.updateCart(userId,productId,item));
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
