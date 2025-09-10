//package com.foodsquad.FoodSquad.service;
//
//import com.foodsquad.FoodSquad.mapper.StoreMapper;
//import com.foodsquad.FoodSquad.model.dto.StoreDTO;
//import com.foodsquad.FoodSquad.model.dto.UserDTO;
//import com.foodsquad.FoodSquad.model.dto.MediaDTO;
//import com.foodsquad.FoodSquad.model.entity.Store;
//import com.foodsquad.FoodSquad.model.entity.User;
//import com.foodsquad.FoodSquad.repository.StoreRepository;
//import com.foodsquad.FoodSquad.service.declaration.AdminService;
//import com.foodsquad.FoodSquad.service.declaration.MediaService;
//
//import com.foodsquad.FoodSquad.service.impl.StoreServiceImpl;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//import static org.assertj.core.api.Assertions.*;
//
//
//@ExtendWith(MockitoExtension.class)
//class StoreServiceImplTest {
//
//    @Mock
//    private StoreRepository storeRepository;
//
//    @Mock
//    private StoreMapper storeMapper;
//
//    @Mock
//    private MediaService mediaService;
//
//    @Mock
//    private AdminService adminService;
//
//    @InjectMocks
//    private StoreServiceImpl storeService;
//
//    private Store store;
//
//    private StoreDTO storeDTO;
//
//    private MediaDTO mediaDTO;
//
//    private MediaDTO savedMediaDTO;
//
//    private Store storeWithOwner;
//
//    private User owner;
//
//    private UserDTO userDTO;
//
//    @BeforeEach
//    void setUp() {
//
//        storeDTO = StoreDTO.builder()
//                .name("Test Store")
//                .email("owner@test.com")
//                .password("password123")
//                .mediaDTO(MediaDTO.builder().id(1L).build())
//                .build();
//
//        mediaDTO = MediaDTO.builder().id(1L).build();
//        savedMediaDTO = MediaDTO.builder().id(2L).build();
//
//
//        owner = User.builder()
//                .id("owner1")
//                .email("owner@test.com")
//                .build();
//
//        userDTO = UserDTO.builder()
//                .email("owner@test.com")
//                .password("password123")
//                .build();
//
//        store = Store.builder()
//                .id("store1")
//                .name("Test Store")
//                .active(true)
//                .owner(owner)
//                .build();
//
//        storeWithOwner = Store.builder()
//                .id("store1")
//                .name("Test Store")
//                .active(true)
//                .owner(owner)
//                .build();
//    }
//
//    @Test
//    void testCreateStore_Success() {
//        when(storeRepository.existsByName("Test Store")).thenReturn(false);
//        when(mediaService.saveMedia(storeDTO.getMediaDTO())).thenReturn(savedMediaDTO);
//        when(storeMapper.toEntity(any(StoreDTO.class))).thenReturn(store);
//        when(adminService.createStoreOwner(any(UserDTO.class))).thenReturn(owner);
//        when(storeRepository.save(any(Store.class))).thenReturn(store);
//        when(storeMapper.toDto(any(Store.class))).thenReturn(storeDTO);
//
//        StoreDTO result = storeService.createStore(storeDTO);
//
//        assertNotNull(result);
//        assertEquals("Test Store", result.getName());
//        verify(storeRepository).existsByName("Test Store");
//        verify(storeRepository).save(any(Store.class));
//    }
//
//    @Test
//    void testCreateStore_DuplicateName() {
//
//        when(storeRepository.existsByName("Test Store")).thenReturn(true);
//
//        IllegalArgumentException ex = assertThrows(
//                IllegalArgumentException.class,
//                () -> storeService.createStore(storeDTO)
//        );
//        assertTrue(ex.getMessage().contains("already exists"));
//        verify(storeRepository).existsByName("Test Store");
//        verifyNoMoreInteractions(storeRepository, mediaService, adminService, storeMapper);
//    }
//
//    @Test
//    void testGetStoreById_Success() {
//
//        when(storeRepository.findById("store1")).thenReturn(Optional.of(store));
//        when(storeMapper.toDto(store)).thenReturn(storeDTO);
//
//        StoreDTO result = storeService.getStoreById("store1");
//
//        assertNotNull(result);
//        assertEquals("Test Store", result.getName());
//        verify(storeRepository).findById("store1");
//    }
//
//    @Test
//    void testGetStoreById_NotFound() {
//
//        when(storeRepository.findById("noid")).thenReturn(Optional.empty());
//        assertThrows(EntityNotFoundException.class, () -> storeService.getStoreById("noid"));
//    }
//
//    @Test
//    void testGetAllStores() {
//
//        List<Store> stores = Arrays.asList(store);
//        List<StoreDTO> dtos = Arrays.asList(storeDTO);
//
//        when(storeRepository.findAll()).thenReturn(stores);
//        when(storeMapper.toDto(store)).thenReturn(storeDTO);
//
//        List<StoreDTO> result = storeService.getAllStores();
//        assertEquals(1, result.size());
//        assertEquals("Test Store", result.get(0).getName());
//    }
//
//    @Test
//    void testGetStoresByUserId() {
//
//        when(storeRepository.findByOwnerId("owner1")).thenReturn(List.of(store));
//        when(storeMapper.toDto(store)).thenReturn(storeDTO);
//
//        List<StoreDTO> dtos = storeService.getStoresByUserId("owner1");
//        assertEquals(1, dtos.size());
//        assertEquals("Test Store", dtos.get(0).getName());
//    }
//
//    @Test
//    void testUpdateStore_Success() {
//
//        StoreDTO updateDTO = StoreDTO.builder().name("Updated Store").build();
//        Store updatedStore = Store.builder().name("Updated Store").build();
//        StoreDTO updatedDTO = StoreDTO.builder().name("Updated Store").build();
//
//        when(storeRepository.findById("store1")).thenReturn(Optional.of(store));
//        when(storeRepository.existsByName("Updated Store")).thenReturn(false);
//        doNothing().when(storeMapper).updateStoreFromDto(updateDTO, store);
//        when(storeRepository.save(store)).thenReturn(updatedStore);
//        when(storeMapper.toDto(updatedStore)).thenReturn(updatedDTO);
//
//        StoreDTO result = storeService.updateStore("store1", updateDTO);
//
//        assertNotNull(result);
//        assertEquals("Updated Store", result.getName());
//        verify(storeRepository).findById("store1");
//        verify(storeRepository).save(store);
//    }
//
//    @Test
//    void testUpdateStore_DuplicateName() {
//        StoreDTO updateDTO = StoreDTO.builder().name("Another Store").build();
//
//        when(storeRepository.findById("store1")).thenReturn(Optional.of(store));
//        when(storeRepository.existsByName("Another Store")).thenReturn(true);
//
//        assertThrows(IllegalArgumentException.class, () -> storeService.updateStore("store1", updateDTO));
//    }
//
//    @Test
//    void testDeleteStore_Success() {
//
//        when(storeRepository.findById("store1")).thenReturn(Optional.of(store));
//        doNothing().when(storeRepository).delete(store);
//
//        assertDoesNotThrow(() -> storeService.deleteStore("store1"));
//        verify(storeRepository).delete(store);
//    }
//
//    @Test
//    void testDeleteStore_NotFound() {
//
//        when(storeRepository.findById("invalid")).thenReturn(Optional.empty());
//
//        assertThrows(EntityNotFoundException.class, () -> storeService.deleteStore("invalid"));
//    }
//
//}
