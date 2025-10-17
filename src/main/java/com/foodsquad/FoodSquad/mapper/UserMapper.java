package com.foodsquad.FoodSquad.mapper;


import com.foodsquad.FoodSquad.model.dto.UserDTO;
import com.foodsquad.FoodSquad.model.dto.UserResponseDTO;
import com.foodsquad.FoodSquad.model.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {MediaMapper.class})
public interface UserMapper extends GenericMapper<User, UserDTO> {



    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Override
    void updateEntityFromDto(UserDTO dto, @MappingTarget User entity);

    UserResponseDTO toResponseDto(User user);

}
