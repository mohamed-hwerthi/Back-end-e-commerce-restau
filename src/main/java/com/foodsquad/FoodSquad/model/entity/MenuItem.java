package com.foodsquad.FoodSquad.model.entity;

import com.foodsquad.FoodSquad.model.Menu;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "menu_items")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(name = "image_url", nullable = false, length = 512)
    private String imageUrl = "https://w0.peakpx.com/wallpaper/97/150/HD-wallpaper-mcdonalds-double-cheese-burger-double-mcdonalds-cheese-burger-thumbnail.jpg";

    @Column(nullable = false)
    private Boolean defaultItem = false;

    @Column(nullable = false)
    private Double price = 1.0;
    @Column(nullable = true , name = "codeBar" , unique = true)
    private String barCode ;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MenuItemCategory category = MenuItemCategory.BURGER;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;
    @ManyToMany(mappedBy = "menuItems")
    private List<Menu> menus;
    @ManyToMany
    @JoinTable(
            name = "menu_item_categories",
            joinColumns = @JoinColumn(name = "menu_item_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "menu_item_medias" ,
            joinColumns = @JoinColumn(name = "menu_item_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    private List<Media> medias = new ArrayList<>()  ;

    @PrePersist
    protected void onCreate() {
        this.createdOn = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getDefaultItem() {
        return defaultItem;
    }

    public void setDefaultItem(Boolean defaultItem) {
        this.defaultItem = defaultItem;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public MenuItemCategory getCategory() {
        return category;
    }

    public void setCategory(MenuItemCategory category) {
        this.category = category;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public List<Review> getReviews() {

        return reviews;
    }

    public void setReviews(List<Review> reviews) {

        this.reviews = reviews;
    }

    public List<Menu> getMenus() {

        return menus;
    }

    public void setMenus(List<Menu> menus) {

        this.menus = menus;
    }

    public List<Category> getCategories() {

        return categories;
    }

    public void setCategories(List<Category> categories) {

        this.categories = categories;
    }

    public String getBarCode() {

        return barCode;
    }

    public void setBarCode(String barCode) {

        this.barCode = barCode;
    }

    public List<Media> getMedias() {

        return medias;
    }

    public void setMedias(List<Media> medias) {

        this.medias = medias;
    }

    @Override
    public String toString() {

        return "MenuItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", defaultItem=" + defaultItem +
                ", price=" + price +
                ", barCode='" + barCode + '\'' +
                ", category=" + category +
                ", user=" + user +
                ", reviews=" + reviews +
                ", createdOn=" + createdOn +
                ", menus=" + menus +
                ", categories=" + categories +
                ", medias=" + medias +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return Objects.equals(id, menuItem.id) && Objects.equals(title, menuItem.title) && Objects.equals(description, menuItem.description) && Objects.equals(imageUrl, menuItem.imageUrl) && Objects.equals(defaultItem, menuItem.defaultItem) && Objects.equals(price, menuItem.price) && Objects.equals(barCode, menuItem.barCode) && category == menuItem.category && Objects.equals(user, menuItem.user) && Objects.equals(reviews, menuItem.reviews) && Objects.equals(createdOn, menuItem.createdOn) && Objects.equals(menus, menuItem.menus) && Objects.equals(categories, menuItem.categories) && Objects.equals(medias, menuItem.medias);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, description, imageUrl, defaultItem, price, barCode, category, user, reviews, createdOn, menus, categories, medias);
    }

}
