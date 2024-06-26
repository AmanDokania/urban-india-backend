package com.Urban_India.entity;

import com.Urban_India.payload.BusinessServiceDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "business_service")
public class BusinessService extends BaseEntity{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private double price;

    @Column(name = "average_rating")
    @Builder.Default
    private Double averageRating = 0.0;

    @Column(name = "total_reviews")
    @Builder.Default
    private Long totalReviews = 0L;

    private Long mode_id;

    @OneToOne
    @JoinColumn(name = "discount_id",referencedColumnName = "id")
    private Coupon coupon;

    @OneToOne
    @JoinColumn(name = "status_id",referencedColumnName = "id")
    private Status status;

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "service_id")
    @JsonBackReference("serviceProviderEntityAReference")
    private ServiceProviderEntitiy service;


    @ManyToOne
    @JoinColumn(name = "business_id")
    @JsonBackReference("businessEntityAReference")
    private Business business;

    @OneToMany(mappedBy = "businessService",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<ChildService> childServiceList;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "service_discount",
            joinColumns = @JoinColumn(name = "business_service_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id",referencedColumnName = "id")
    )
    private List<Coupon> couponList;

    @OneToMany(mappedBy = "businessService",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    List<Review> reviewsList;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @OneToMany(mappedBy = "businessService",fetch = FetchType.LAZY)
    @JsonManagedReference(value = "businessServiceCartItemsReference")
    private List<CartItem> cartItems;

    public BusinessServiceDto toBusinessServiceDto(){
        return BusinessServiceDto.builder()
                .id(this.getId())
                .title(this.title)
                .description(this.description)
                .price(this.price)
                .mode_id(this.mode_id)
                .rating(this.averageRating)
                .image(Objects.nonNull(this.image) ? this.image.getName() : null)
                .businessName(this.business.getName())
                .businessId(this.business.getId())
                .serviceTypeId(this.service.getId())
                .serviceTypeName(this.service.getTitle())
                .statusId(Objects.nonNull(this.status) ? this.status.getId() : null)
                .addressModel(this.address.addressModel)
                .addressID(this.address.getId())
                .build();
    }
}
