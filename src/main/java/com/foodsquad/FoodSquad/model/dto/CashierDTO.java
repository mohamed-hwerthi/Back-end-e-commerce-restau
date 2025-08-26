package com.foodsquad.FoodSquad.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder

public class CashierDTO extends EmployeeDTO {
}
