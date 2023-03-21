package com.Urban_India.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "business_service")
public class BusinessService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private double price;
    private Long mode_id;
    private Long discount_id;
    @OneToOne
    @JoinColumn(name = "status_id",referencedColumnName = "id")
    private Status status;
    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @OneToMany(mappedBy = "businessService",cascade = CascadeType.ALL)
    private List<ChildService> childServiceList;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "service_discount",
            joinColumns = @JoinColumn(name = "business_service_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id",referencedColumnName = "id")
    )
    private List<Discount> discountList;

}
