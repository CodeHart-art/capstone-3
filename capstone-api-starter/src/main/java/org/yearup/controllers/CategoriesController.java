package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin("*")
public class CategoriesController {
    private CategoryService categoryService;
    private ProductService productService;

    @Autowired
    public CategoriesController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    /**
     * Retrieves all categories from the system.
     *
     * This endpoint handles HTTP GET requests and returns a list of all available
     * Category objects by delegating the call to the CategoryService.
     *
     * @return a list of all Category entities
     */
    @GetMapping
    public List<Category> getAll() {
        // find and return all categories
        return categoryService.getAllCategories();
    }

    /**
     * Retrieves a category by its ID.
     *
     * This endpoint handles HTTP GET requests with a category ID path variable.
     * It delegates the lookup to the CategoryService and returns the matching
     * Category if found.
     *
     * If no category exists with the given ID, a 404 Not Found response is returned.
     *
     * @param id the ID of the category to retrieve
     * @return a ResponseEntity containing the Category if found, or a 404 status if not
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable int id) {
        // get the category by id
        Category category = categoryService.getById(id);

        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(category);
    }

    /**
     * Retrieves all products that belong to a specific category.
     *
     * This endpoint handles HTTP GET requests using a category ID path variable.
     * It returns a list of Product objects that are associated with the given category.
     *
     * @param categoryId the ID of the category whose products are being retrieved
     * @return a list of products that belong to the specified category
     */
    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId) {
        return productService.listByCategoryId(categoryId);
    }

    /**
     * Creates a new category.
     *
     * This endpoint is restricted to users with the ADMIN role. It accepts a
     * Category object in the request body, saves it, and returns the newly
     * created category with a 201 Created status.
     *
     * @param category the category to create
     * @return a ResponseEntity containing the newly created Category
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        return ResponseEntity.status(201).body(categoryService.create(category));
    }

    /**
     * Updates an existing category.
     *
     * This endpoint is restricted to users with the ADMIN role. It updates the
     * category with the specified ID using the data provided in the request body
     * and returns the updated Category.
     *
     * @param id the ID of the category to update
     * @param category the updated category information
     * @return the updated Category
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Category updateCategory(@PathVariable int id, @RequestBody Category category) {
        return categoryService.update(id,category);
    }


    /**
     * Deletes a category by its ID.
     *
     * This endpoint is restricted to users with the ADMIN role. It removes the
     * specified category from the system and returns a 204 No Content response
     * upon successful deletion.
     *
     * @param id the ID of the category to delete
     * @return a ResponseEntity with a 204 No Content status
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        // delete the category by id and return status 204 No Content
        categoryService.delete(id);
        return ResponseEntity.status(204).build();
    }
}
