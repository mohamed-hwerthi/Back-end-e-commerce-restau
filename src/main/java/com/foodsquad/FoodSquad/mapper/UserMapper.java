package com.foodsquad.FoodSquad.mapper;


import com.foodsquad.FoodSquad.model.dto.UserDTO;
import com.foodsquad.FoodSquad.model.dto.UserResponseDTO;
import com.foodsquad.FoodSquad.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {MediaMapper.class})
public interface UserMapper  extends   GenericMapper<User , UserDTO> {


    @Mapping(target = "id", ignore = true)
    @Override
    default void updateEntityFromDto(UserDTO dto, User entity) {
    }
    UserResponseDTO toResponseDto(User user);

}
