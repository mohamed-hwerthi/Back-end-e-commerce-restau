package com.foodsquad.FoodSquad.mapper;


import org.mapstruct.MappingTarget;

import java.util.List;

public interface GenericMapper<E, D> {

    D toDto(E entity);

    E toEntity(D dto);

    List<D> toDtoList(List<E> entities);

    List<E> toEntityList(List<D> dtos);

    void updateEntityFromDto(D dto, @MappingTarget E entity);

}
