package com.foodsquad.FoodSquad.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodsquad.FoodSquad.model.entity.*;
import com.foodsquad.FoodSquad.repository.CategoryRepository;
import com.foodsquad.FoodSquad.repository.MenuItemRepository;
import com.foodsquad.FoodSquad.repository.OrderRepository;
import com.foodsquad.FoodSquad.repository.ReviewRepository;
import com.foodsquad.FoodSquad.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DatabaseSeeder {
    private final ResourceLoader resourceLoader;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CategoryRepository categoryRepository ;


    @Autowired
    public DatabaseSeeder(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @PostConstruct
    public void seedDatabase() {
        // Check if the database is empty before seeding
        if (userRepository.count() == 0) {
            seedUsers();
        } else {
            System.out.println("Users already exist in the database, skipping user seeding.");
        }
        if (categoryRepository.count() == 0) {
            seedCategories();
        }

        if (menuItemRepository.count() == 0) {
            seedMenuItems();
        } else {
            System.out.println("MenuItems already exist in the database, skipping menu item seeding.");
        }

        if (orderRepository.count() == 0) {
            seedOrders();
        } else {
            System.out.println("Orders already exist in the database, skipping order seeding.");
        }
        if (reviewRepository.count() == 0) {

        } else {
            System.out.println("Reviews already exist in the database, skipping review seeding.");
        }
    }

    private void seedCategories() {

        List<Category> categories = List.of(
                createCategory("ORYX FRAGRANCE", "ORYX FRAGRANCE"),
                createCategory("ORYX BIO", "ORYX BIO"),
                createCategory("ORYX PHYTO", "ORYX PHYTO")

        );

        categoryRepository.saveAll(categories);
    }

    private Category createCategory(String name, String description) {

        Category category = new Category();

        category.setName(name);
        category.setDescription(description);
        return category;
    }

    private void seedMenuItems() {
        Optional<User> userOpt = userRepository.findAll().stream().findFirst();
        if (userOpt.isEmpty()) {
            System.out.println("No users found to associate with MenuItems.");
            return;
        }
        User defaultUser = userOpt.get();

        Category ORYX_FRAGRANCE = categoryRepository.findByName("ORYX FRAGRANCE").orElseThrow(()->new RuntimeException("category does not be founded"));
        Category ORYX_BIO = categoryRepository.findByName("ORYX BIO").orElseThrow(()->new RuntimeException("category does not be founded"));
        Category ORYX_PHYTO = categoryRepository.findByName("ORYX PHYTO").orElseThrow(()->new RuntimeException("category does not be founded"));


        List<MenuItem> fragranceItems = List.of(
                createMenuItem("ORYX N°1 - 50ML", 95.0, "66192499601340", defaultUser, ORYX_FRAGRANCE),
                createMenuItem("ORYX N°2 - 50ML", 98.0, "6192499601357", defaultUser, ORYX_FRAGRANCE),
                createMenuItem("ORYX N°4 - 50ML", 88.0, "6192499601371", defaultUser, ORYX_FRAGRANCE),
                createMenuItem("ORYX N°3 - 50ML", 80.0, "6192499601364", defaultUser, ORYX_FRAGRANCE),
                createMenuItem("MIDNIGHT JASMIN", 35.0, "6192499600275", defaultUser, ORYX_FRAGRANCE),
                createMenuItem("DEO FLOWER MOOD", 27.0, "6192499600695", defaultUser, ORYX_FRAGRANCE),
                createMenuItem("MAJESTIC OUD", 30.0, "6192499600770", defaultUser, ORYX_FRAGRANCE),
                createMenuItem("DEO HOMME", 27.0, "6192499600817", defaultUser, ORYX_FRAGRANCE),
                createMenuItem("DEO LOVELY", 27.0, "6192499600701", defaultUser, ORYX_FRAGRANCE),
                createMenuItem("gel acnestop", 29.0, "6192499600633", defaultUser, ORYX_FRAGRANCE),
                createMenuItem("ORYX N°5 - 50ML", 79.0, "6192499601432", defaultUser, ORYX_FRAGRANCE),
                createMenuItem("ORYX N°6 - 50ML", 92.0, "6192499601449", defaultUser, ORYX_FRAGRANCE),
                createMenuItem("ORYX N°7 - 50ML", 85.0, "6192499601456", defaultUser, ORYX_FRAGRANCE),
                createMenuItem("ROYAL OUD VANILLE", 30, "6192499601227", defaultUser, ORYX_FRAGRANCE)
        );

        List<MenuItem> bioItems = List.of(
                createMenuItem("souchet 30 ml", 53.0, "6192499601166", defaultUser, ORYX_BIO),
                createMenuItem("romarin 10", 22.0, "6192499601326", defaultUser, ORYX_BIO),
                createMenuItem("reg 15 ml", 52.0, "6192499601302", defaultUser, ORYX_BIO),
                createMenuItem("lifting seins", 45.0, "6192499600060", defaultUser, ORYX_BIO),
                createMenuItem("mon laser +", 45.0, "6192499600053", defaultUser, ORYX_BIO),
                createMenuItem("huile de pepin de figue de barbarie 10 ml", 35.0, "6192499600107", defaultUser, ORYX_BIO),
                createMenuItem("huile de lin", 25.0, "6192499600114", defaultUser, ORYX_BIO),
                createMenuItem("shampoing ultra liss", 32.0, "6192499600138", defaultUser, ORYX_BIO),
                createMenuItem("SHAMPOING YNABET 250 ML", 37.0, "6192499600152", defaultUser, ORYX_BIO),
                createMenuItem("serum BLANCHEUR 30 ML", 37.0, "6192499600282", defaultUser, ORYX_BIO),
                createMenuItem("creme mon laser", 35.0, "6192499600985", defaultUser, ORYX_BIO),
                createMenuItem("mycostop 30", 55.0, "6192499601159", defaultUser, ORYX_BIO),
                createMenuItem("apres shampoing ultra liss 250 ml", 37.0, "6192499601180", defaultUser, ORYX_BIO),
                createMenuItem("ecran luxe eternel", 65.0, "6192499601524", defaultUser, ORYX_BIO),
                createMenuItem("SHAMP YNABET 400ML", 55.0, "6192499601562", defaultUser, ORYX_BIO),
                createMenuItem("YNABET 150 ML", 69.0, "6192499600015", defaultUser, ORYX_BIO),
                createMenuItem("YNABET 250 ML", 90.0, "6192499600039", defaultUser, ORYX_BIO),
                createMenuItem("acnestop 10", 27.0, "6192499600244", defaultUser, ORYX_BIO)
                // Add more items similarly
        );

        List<MenuItem> phytoItems = List.of(
                createMenuItem("moringa", 30.0, "6192499601517", defaultUser, ORYX_PHYTO),
                createMenuItem("TISANE BRULE GRAISSE 200 GR", 52.0, "6192499601265", defaultUser, ORYX_PHYTO),
                createMenuItem("PACK librasoke", 140.0, "6192499600268", defaultUser, ORYX_PHYTO),
                createMenuItem("sirop librasmoke", 55.0, "6192499601111", defaultUser, ORYX_PHYTO),
                createMenuItem("gelule librasmoke", 20.0, "6192499600961", defaultUser, ORYX_PHYTO),
                createMenuItem("tisane LIBRASMOKE", 65.0, "6192499600527", defaultUser, ORYX_PHYTO)
        );

        // Save all MenuItems in the database
        List<MenuItem> allItems = new ArrayList<>();
        allItems.addAll(fragranceItems);
        allItems.addAll(bioItems);
        allItems.addAll(phytoItems);

        menuItemRepository.saveAll(allItems);
    }



    private MenuItem createMenuItem(String title, double price, String barCode, User user, Category category) {

        MenuItem item = new MenuItem();
        item.setTitle(title) ;
        item.setDescription(title);
        item.setPrice(price);
        item.setBarCode(barCode);
        item.setUser(user) ;
        item.setCategories(List.of(category));
        item.setCreatedOn(LocalDateTime.now());
        return item ;
    }


    private void seedUsers() {

        List<User> users = List.of(
                createUser("John Doe", "med@gmail.com", "123123", "+359 899 78 7878", UserRole.NORMAL),
                createUser("Jane Smith", "jane.smith@example.com", "123123", "+359 899 78 7878", UserRole.NORMAL),
                createUser("Admin User", "admin@example.com", "123123", "+359 899 78 7878", UserRole.ADMIN),
                createUser("Moderator User", "moderator@example.com", "123123", "+359 899 78 7878", UserRole.MODERATOR)
        );

        userRepository.saveAll(users);
        System.out.println("Users seeded successfully.");
    }

    /*
        private void seedMenuItems() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource("menu_batch.json");
            InputStream inputStream = resource.getInputStream();

            // Check if the file exists
            if (!resource.exists()) {
                System.out.println("File not found: menu_batch.json");
                return;
            }

            // Read the JSON array from the file
            JsonNode rootNode = objectMapper.readTree(inputStream);
            List<MenuItem> menuItems = new ArrayList<>();

            // Retrieve users
            User adminUser = userRepository.findByEmail("admin@example.com").orElse(null);
            User moderator = userRepository.findByEmail("moderator@example.com").orElse(null);

            if (adminUser == null || moderator == null) {
                System.out.println("Admin or Moderator user not found. Cannot seed menu items.");
                return;
            }

            // Iterate over each element in the JSON array
            for (JsonNode node : rootNode) {
                String title = node.get("title").asText();
                String description = node.get("description").asText();
                String imageUrl = node.get("imageUrl").asText();
                double price = node.get("price").asDouble();
                boolean defaultItem = node.get("defaultItem").asBoolean();
                String categoryText = node.get("category").asText();
                String creatorField = node.get("creator").asText();

                MenuItemCategory category;
                try {
                    category = MenuItemCategory.valueOf(categoryText.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid category: " + categoryText);
                    continue; // Skip this item if category is invalid
                }

                User creator = creatorField.equalsIgnoreCase("adminUser") ? adminUser : moderator;

                MenuItem menuItem = createMenuItem(title, description, imageUrl, price, defaultItem, creator);
                menuItems.add(menuItem);
            }

            // Save all menu items to the repository
            Collections.reverse(menuItems); // Reverse the menuItems list before saving
            menuItemRepository.saveAll(menuItems);
            System.out.println("Menu items seeded successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     */




    private void seedOrders() {

        List<MenuItem> allMenuItems = menuItemRepository.findAll();
        List<Order> orders = new ArrayList<>();

        // Example data: Creating orders with quantities for seeding
        Map<Long, Integer> order1Items = new HashMap<>();
        order1Items.put(58L, 5); // 5 of MenuItem with ID 58
        order1Items.put(64L, 4); // 4 of MenuItem with ID 64

        Map<Long, Integer> order2Items = new HashMap<>();
        order2Items.put(56L, 3); // 3 of MenuItem with ID 56
        order2Items.put(47L, 21); // 21 of MenuItem with ID 47

        Map<Long, Integer> order3Items = new HashMap<>();
        order3Items.put(61L, 1); // 1 of MenuItem with ID 61
        order3Items.put(47L, 3); // 3 of MenuItem with ID 47


        orderRepository.saveAll(orders);
        System.out.println("Orders seeded successfully.");
    }

    private User createUser(String name, String email, String password, String phoneNumber, UserRole role) {

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhoneNumber(phoneNumber);
        user.setRole(role);
        return user;
    }

    private MenuItem createMenuItem(String title, String description, String imageUrl, double price, boolean defaultItem, User user) {

        MenuItem menuItem = new MenuItem();
        menuItem.setTitle(title);
        menuItem.setDescription(description);
        menuItem.setImageUrl(imageUrl);
        menuItem.setPrice(price);
        menuItem.setUser(user);
        menuItem.setDefaultItem(defaultItem);
        return menuItem;
    }

    /*

    private Order createOrder(OrderStatus status, LocalDateTime createdOn, User user, boolean paid, Map<Long, Integer> menuItemQuantities) {

        Order order = new Order();
        order.setStatus(status);
        order.setCreatedOn(createdOn);
        order.setUser(user);
        order.setPaid(paid);

        Map<MenuItem, Integer> menuItemsWithQuantity = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : menuItemQuantities.entrySet()) {
            MenuItem menuItem = menuItemRepository.findById(entry.getKey()).orElseThrow(() -> new IllegalArgumentException("Invalid menu item ID"));
            menuItemsWithQuantity.put(menuItem, entry.getValue());
        }

        order.setMenuItemsWithQuantity(menuItemsWithQuantity);
        double totalCost = menuItemsWithQuantity.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice().doubleValue() * entry.getValue())
                .sum();
        // Round the totalCost to 2 decimal places
        BigDecimal roundedTotalCost = BigDecimal.valueOf(totalCost).setScale(2, RoundingMode.HALF_UP);
        order.setTotalCost(roundedTotalCost.doubleValue());
        return order;
    }

    private void seedReviews() {

        List<MenuItem> menuItems = menuItemRepository.findAll();
        List<User> users = userRepository.findAll();
        Random random = new Random();

        List<Review> reviews = new ArrayList<>();
        String[] comments = {"Great taste!", "Could be better.", "I loved it!", "Not my favorite.", "Amazing quality!"};

        for (MenuItem menuItem : menuItems) {
            for (int i = 0; i < 3; i++) {  // Each item gets 3 reviews
                User user = users.get(random.nextInt(users.size())); // Random user
                String comment = comments[random.nextInt(comments.length)]; // Random comment
                int rating = random.nextInt(5) + 1; // Rating between 1 and 5

                Review review = new Review();
                review.setMenuItem(menuItem);
                review.setUser(user);
                review.setComment(comment);
                review.setRating(rating);
                review.setCreatedOn(LocalDateTime.now().minusDays(random.nextInt(30)));

                reviews.add(review);
            }
        }

        reviewRepository.saveAll(reviews);
        System.out.println("Reviews seeded successfully.");
    }

     */

}
