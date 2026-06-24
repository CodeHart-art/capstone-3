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

    /**
     * Retrieves a shopping cart for a specific user by user ID.
     * This method pulls all CartItem records associated with the given user,
     * converts each CartItem into a ShoppingCartItem by loading the full Product
     * details, and assembles them into a ShoppingCart object.
     *
     * @param userId the ID of the user whose cart is being retrieved
     * @return a ShoppingCart containing all items and quantities for the user
     */
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

    /**
     * Adds a product to the user's shopping cart.
     * If the product already exists in the user's cart, its quantity is incremented by 1.
     * Otherwise, a new CartItem is created and saved with an initial quantity.
     * After updating the cart, the method returns the user's updated ShoppingCart.
     *
     * @param userId the ID of the user adding the item to their cart
     * @param id the product ID being added to the cart
     * @return the updated ShoppingCart for the user
     */
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

    /**
     * Updates the quantity of a specific product in a user's shopping cart.
     *
     * This method finds the existing CartItem for the given user and product,
     * updates its quantity based on the provided ShoppingCartItem, and saves the change.
     *
     * After updating, it returns the user's refreshed ShoppingCart.
     *
     * @param userId the ID of the user who owns the cart
     * @param productId the ID of the product being updated
     * @param item the ShoppingCartItem containing the new quantity
     * @return the updated ShoppingCart for the user
     */
    public ShoppingCart updateCart(int userId,int productId,ShoppingCartItem item){

        CartItem existingCartItem =
                shoppingCartRepository.findByUserIdAndProductId(userId,productId);

        existingCartItem.setQuantity(item.getQuantity());
        shoppingCartRepository.save(existingCartItem);

        return getByUserId(userId);
    }

    /**
     * Removes all items from a user's shopping cart.
     *
     * This method deletes all CartItem records associated with the given user ID,
     * effectively clearing the user's cart. After deletion, it returns an updated
     * (now empty) ShoppingCart.
     *
     * The operation is transactional to ensure the cart is fully cleared or not
     * modified at all in case of an error.
     *
     * @param userId the ID of the user whose cart will be cleared
     * @return an empty ShoppingCart for the user
     */
    @Transactional
    public ShoppingCart clearCart(int userId) {

        shoppingCartRepository.deleteByUserId(userId);

        return  getByUserId(userId);
    }
}
