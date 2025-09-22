package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.MenuMapper;
import com.foodsquad.FoodSquad.mapper.ProductMapper;
import com.foodsquad.FoodSquad.model.dto.MenuDTO;
import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.entity.Menu;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.repository.MenuRepository;
import com.foodsquad.FoodSquad.service.declaration.ProductService;
import com.foodsquad.FoodSquad.service.declaration.MenuService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;


@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;
    private final ProductService ProductService;
    private final ProductMapper productMapper;


    public MenuServiceImpl(MenuRepository menuRepository, MenuMapper menuMapper, ProductService ProductService, ProductMapper productMapper) {
        this.menuRepository = menuRepository;
        this.menuMapper = menuMapper;
        this.ProductService = ProductService;
        this.productMapper = productMapper;
    }

    @Override
    public MenuDTO getMenuById(Long id) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Menu not found  with id " + id));
        return menuMapper.toDto(menu);
    }

    @Override
    public List<MenuDTO> getAllMenus() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(menuMapper::toDto)
                .toList();
    }

    @Override
    public MenuDTO createMenu(MenuDTO menuDTO) {
        Menu menu = menuMapper.toEntity(menuDTO);
        if (!ObjectUtils.isEmpty(menuDTO.getProductsIds())) {
            menuDTO.getProductsIds().forEach(
                    ProductId -> {
                        ProductDTO foundedProduct = ProductService.getProductById(ProductId);
                        Product product = productMapper.toEntity(foundedProduct);
                        menu.getProducts().add(product);
                    }
            );

        }

        Menu savedMenu = menuRepository.save(menu);
        return menuMapper.toDto(savedMenu);
    }

    @Override
    public MenuDTO updateMenu(Long id, MenuDTO menuDTO) {
        if (!menuRepository.existsById(id)) {
            throw new EntityNotFoundException("Menu not found  with id  " + id);
        }
        Menu menu = menuMapper.toEntity(menuDTO);
        menu.setId(id);
        Menu updatedMenu = menuRepository.save(menu);
        return menuMapper.toDto(updatedMenu);
    }

    @Override
    public void deleteMenu(Long id) {
        if (!menuRepository.existsById(id)) {
            throw new EntityNotFoundException("Menu not found  with id  " + id);
        }
        menuRepository.deleteById(id);
    }
}
