package com.foodsquad.FoodSquad.config;

import com.foodsquad.FoodSquad.model.dto.MediaDTO;
import com.foodsquad.FoodSquad.model.entity.*;
import com.foodsquad.FoodSquad.model.entity.Currency;
import com.foodsquad.FoodSquad.repository.*;
import com.foodsquad.FoodSquad.service.declaration.MediaService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private CurrencyRepository currencyRepository ;

    @Autowired
    private TimbreRepository timbreRepository;

    @Autowired
    private MediaService mediaService ;

    @Autowired
    private MediaRepository mediaRepository  ;





    @Autowired
    public DatabaseSeeder(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @PostConstruct
    public void seedDatabase() {
        if (userRepository.count() == 0) {
            seedUsers();
        } else {
            System.out.println("Users already exist in the database, skipping user seeding.");
        }
        /*
        if(mediaRepository.count()==0){
            this.seedMedias();
        }

         */
        if (categoryRepository.count() == 0) {
            seedCategories();
        }
        if (currencyRepository.count() == 0) {
            seedCurrencies();
        }

        if (menuItemRepository.count() == 0) {
            seedMenuItems();
        } else {
            System.out.println("MenuItems already exist in the database, skipping menu item seeding.");
        }

        if (timbreRepository.count() == 0) {
            createTimbre();
        } else {
            System.out.println("default timbre already exist in the database, skipping seeding.");
        }

        if (reviewRepository.count() == 0) {

        } else {
            System.out.println("Reviews already exist in the database, skipping review seeding.");
        }

    }
/*

    private void seedMedias(){
        List<String>menuItemsImagesNames  = List.of("menu_item_image_1.png" ,"menu_item_image_2.jpg" , "menu_item_image_3.jpg" , "menu_item_image_4.jpg" , "category_image_1.jpg" , "category_image_2.jpg" , "category_image_3.webp");
        menuItemsImagesNames.forEach(this::saveMedia);
    }

 */
    /*
    private void saveMedia(String imageName) {
        try {
            Resource resource = resourceLoader.getResource("capplicalasspath:images/" + imageName);

            try (InputStream inputStream = resource.getInputStream()) {

                MultipartFile multipartFile = new MockMultipartFile(
                        imageName,
                        imageName,
                        Files.probeContentType(Paths.get(imageName)),
                        inputStream
                );

                MediaDTO uploadedMedia = mediaService.uploadFile(multipartFile);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to seed default media image: " + imageName, e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

     */




    private void seedCategories() {


        List<Category> categories = List.of(
                createCategory("Oryx Fragrance", "ORYX FRAGRANCE"),
                createCategory("Oryx Bio", "ORYX BIO"),
                createCategory("Oryx Phyto", "ORYX PHYTO")

        );

        categoryRepository.saveAll(categories);
    }
    private void seedCurrencies() {
        if (currencyRepository.count() == 0) {
            List<Currency> currencies = List.of(
                    new Currency("Tunisian Dinar", "TND", 3),
                    new Currency("US Dollar", "$", 2),
                    new Currency("Euro", "€", 2)
            );

            currencyRepository.saveAll(currencies);
        }
    }
    private Category createCategory(String name, String description) {

        Category category = new Category();

        category.setName(name);
        category.setDescription(description);

        List<Media>media = mediaRepository.findAll();
        //category.setMedias(Collections.singletonList(media.get(generateRandomNumber())));

        return  category ;

    }
    private void createTimbre() {

      Timbre timbre=new Timbre();
      timbre.setAmount(0.0);
      timbreRepository.save(timbre);
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
        Currency currency =currencyRepository.findBySymbol("TND").orElseThrow(()->new RuntimeException("Currency does not exist"));


        List<MenuItem> fragranceItems = List.of(
                createMenuItem("ORYX N°1 - 50ML", 95.0, "66192499601340", defaultUser, ORYX_FRAGRANCE,currency),
                createMenuItem("ORYX N°2 - 50ML", 98.0, "6192499601357", defaultUser, ORYX_FRAGRANCE,currency),
                createMenuItem("ORYX N°3 - 50ML", 80.0, "6192499601364", defaultUser, ORYX_FRAGRANCE,currency),
                createMenuItem("MIDNIGHT JASMIN", 35.0, "6192499600275", defaultUser, ORYX_FRAGRANCE,currency),
                createMenuItem("DEO FLOWER MOOD", 27.0, "6192499600695", defaultUser, ORYX_FRAGRANCE,currency),
                createMenuItem("MAJESTIC OUD", 30.0, "6192499600770", defaultUser, ORYX_FRAGRANCE,currency),
                createMenuItem("DEO HOMME", 27.0, "6192499600817", defaultUser, ORYX_FRAGRANCE,currency),
                createMenuItem("DEO LOVELY", 27.0, "6192499600701", defaultUser, ORYX_FRAGRANCE,currency),
                createMenuItem("ORYX N°4 - 50ML", 88.0, "6192499601371", defaultUser, ORYX_FRAGRANCE,currency),
                createMenuItem("gel acnestop", 29.0, "6192499600633", defaultUser, ORYX_FRAGRANCE,currency),
                createMenuItem("ORYX N°5 - 50ML", 79.0, "6192499601432", defaultUser, ORYX_FRAGRANCE,currency),
                createMenuItem("ORYX N°6 - 50ML", 92.0, "6192499601449", defaultUser, ORYX_FRAGRANCE,currency),
                createMenuItem("ORYX N°7 - 50ML", 85.0, "6192499601456", defaultUser, ORYX_FRAGRANCE,currency),
                createMenuItem("ROYAL OUD VANILLE", 30, "6192499601227", defaultUser, ORYX_FRAGRANCE,currency)
        );

        List<MenuItem> bioItems = List.of(
                createMenuItem("souchet 30 ml", 53.0, "6192499601166", defaultUser, ORYX_BIO,currency),
                createMenuItem("romarin 10", 22.0, "6192499601326", defaultUser, ORYX_BIO,currency),
                createMenuItem("reg 15 ml", 52.0, "6192499601302", defaultUser, ORYX_BIO,currency),
                createMenuItem("lifting seins", 45.0, "6192499600060", defaultUser, ORYX_BIO,currency),
                createMenuItem("mon laser +", 45.0, "6192499600053", defaultUser, ORYX_BIO,currency),
                createMenuItem("huile de pepin de figue de barbarie 10 ml", 35.0, "6192499600107", defaultUser, ORYX_BIO,currency),
                createMenuItem("huile de lin", 25.0, "6192499600114", defaultUser, ORYX_BIO,currency),
                createMenuItem("shampoing ultra liss", 32.0, "6192499600138", defaultUser, ORYX_BIO,currency),
                createMenuItem("SHAMPOING YNABET 250 ML", 37.0, "6192499600152", defaultUser, ORYX_BIO,currency),
                createMenuItem("serum BLANCHEUR 30 ML", 37.0, "6192499600282", defaultUser, ORYX_BIO,currency),
                createMenuItem("mycostop 30", 55.0, "6192499601159", defaultUser, ORYX_BIO,currency),
                createMenuItem("apres shampoing ultra liss 250 ml", 37.0, "6192499601180", defaultUser, ORYX_BIO,currency),
                createMenuItem("ecran luxe eternel", 65.0, "6192499601524", defaultUser, ORYX_BIO,currency),
                createMenuItem("SHAMP YNABET 400ML", 55.0, "6192499601562", defaultUser, ORYX_BIO,currency),
                createMenuItem("YNABET 150 ML", 69.0, "6192499600015", defaultUser, ORYX_BIO,currency),
                createMenuItem("YNABET 250 ML", 90.0, "6192499600039", defaultUser, ORYX_BIO,currency),
                createMenuItem("acnestop 10", 27.0, "6192499600244", defaultUser, ORYX_BIO,currency)
        );

        List<MenuItem> phytoItems = List.of(
                createMenuItem("moringa", 30.0, "6192499601517", defaultUser, ORYX_PHYTO,currency),
                createMenuItem("TISANE BRULE GRAISSE 200 GR", 52.0, "6192499601265", defaultUser, ORYX_PHYTO,currency),
                createMenuItem("PACK librasoke", 140.0, "6192499600268", defaultUser, ORYX_PHYTO,currency),
                createMenuItem("sirop librasmoke", 55.0, "6192499601111", defaultUser, ORYX_PHYTO,currency),
                createMenuItem("gelule librasmoke", 20.0, "6192499600961", defaultUser, ORYX_PHYTO,currency),
                createMenuItem("tisane LIBRASMOKE", 65.0, "6192499600527", defaultUser, ORYX_PHYTO,currency)
        );

        List<MenuItem> allItems = new ArrayList<>();
        allItems.addAll(fragranceItems);
        allItems.addAll(bioItems);
        allItems.addAll(phytoItems);

        menuItemRepository.saveAll(allItems);
    }




    private MenuItem createMenuItem(String title, double price, String barCode, User user, Category category,Currency currency ) {

        MenuItem item = new MenuItem();
        item.setTitle(title) ;
        item.setDescription(title);
        item.setPrice(price);
        item.setBarCode(barCode);
        item.setUser(user) ;
        item.setCategories(List.of(category));
        item.setCurrency(currency);
        item.setCreatedOn(LocalDateTime.now());

       // List<Media>medias = mediaRepository.findAll();
        //item.setMedias(Collections.singletonList(medias.get(generateRandomNumber())));
        return item ;
    }
    public  int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(4);
    }


    private void seedUsers() {

        List<User> users = List.of(
                createUser("OrixBio", "orixbio@gmail.com", "123123", "+359 899 78 7878", UserRole.NORMAL),
                createUser("Admin User", "admin@example.com", "123123", "+359 899 78 7878", UserRole.ADMIN)
        );

        userRepository.saveAll(users);
        System.out.println("Users seeded successfully.");
    }



/*
    private void seedOrders() {

        List<MenuItem> allMenuItems = menuItemRepository.findAll();
        List<Order> orders = new ArrayList<>();
githgi

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
 */



    private User createUser(String name, String email, String password, String phoneNumber, UserRole role) {

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhoneNumber(phoneNumber);
        user.setRole(role);
        return user;
    }

    private MenuItem createMenuItem(String title, String description, String imageUrl, double price, boolean defaultItem, User user,Currency currency) {

        MenuItem menuItem = new MenuItem();
        menuItem.setTitle(title);
        menuItem.setDescription(description);
        menuItem.setPrice(price);
        menuItem.setUser(user);
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
