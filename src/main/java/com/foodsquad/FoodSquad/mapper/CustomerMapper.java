package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.CustomerDTO;
import com.foodsquad.FoodSquad.model.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper   extends GenericMapper<Customer, CustomerDTO> {

    /**
     * Convert CustomerDTO to Customer entity.
     *
     * @param dto the DTO to convert
     * @return the Customer entity
     */
    Customer toEntity(CustomerDTO dto);

    /**
     * Convert Customer entity to CustomerDTO.
     *
     * @param entity the entity to convert
     * @return the DTO
     */
    CustomerDTO toDto(Customer entity);

    /**
     * Update Customer entity from DTO.
     *
     * @param dto    the DTO with updated fields
     * @param entity the entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(CustomerDTO dto, @MappingTarget Customer entity);
}
