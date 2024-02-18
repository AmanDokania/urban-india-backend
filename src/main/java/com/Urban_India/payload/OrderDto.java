package com.Urban_India.payload;

import com.Urban_India.Enum.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OrderDto {
    private Long id;
    private Long businessId;
    private String businessName;
    private Long couponId;
    private String couponName;
    private Double couponPercentage;
    private OrderStatusEnum status;
    private Long paymentId;
    private Long addressId;
    private AddressDto addressDto;
    List<OrderItemDto> orderItemDtos;

}