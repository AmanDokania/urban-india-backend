package com.Urban_India.entity;

import com.Urban_India.payload.CartItemDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quantity;

    /**
     * It was giving error because of bidirectional mapping.It was giving error of Circular Reference.
     */
    @ManyToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    @JsonBackReference
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "business_service_id", referencedColumnName = "id")
    @JsonBackReference(value = "businessServiceCartItemsReference")
    private BusinessService businessService;

//    @PreRemove
//    public void dismissCart() {
//        this.cart.dismissCartItem(this); //SYNCHRONIZING THE OTHER SIDE OF RELATIONSHIP
//        this.cart = null;
//    }

    public CartItemDto toCartItemDto(){
        return CartItemDto.builder()
                .cartId(Objects.isNull(this.cart) ? null :this.cart.getId())
                .id(this.id)
                .businessServiceId(Objects.isNull(this.businessService) ? null :this.businessService.getId())
                .quantity(this.quantity)
                .businessServiceDto(Objects.nonNull(this.businessService) ? this.businessService.toBusinessServiceDto() : null)
//                .cartDto(Objects.isNull(this.cart) ? null :this.cart.toCartDto())
                .build();
    }

}
