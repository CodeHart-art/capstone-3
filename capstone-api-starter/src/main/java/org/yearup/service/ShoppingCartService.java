package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

import java.util.List;

@Service
public class ShoppingCartService {
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId) {

        ShoppingCart cart = new ShoppingCart();

        List<CartItem> userCart = shoppingCartRepository.findByUserId(userId);

        for (CartItem c : userCart) {
            Product product = productService.getById(c.getProductId());
            ShoppingCartItem shoppingCartItem = new ShoppingCartItem();

            shoppingCartItem.setProduct(product);
            shoppingCartItem.setQuantity(c.getQuantity());

            cart.add(shoppingCartItem);
        }

        return cart;
    }

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

    @Transactional
    public ShoppingCart clearCart(int userId) {

        shoppingCartRepository.deleteByUserId(userId);

        return  getByUserId(userId);
    }
}
