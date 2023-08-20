package com.Urban_India.service.impl;

import com.Urban_India.entity.Business;
import com.Urban_India.entity.Coupon;
import com.Urban_India.entity.User;
import com.Urban_India.exception.ResourceNotFoundException;
import com.Urban_India.exception.UrbanApiException;
import com.Urban_India.payload.CouponDto;
import com.Urban_India.repository.CouponRepository;
import com.Urban_India.repository.UserRepository;
import com.Urban_India.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public CouponDto addCoupon(CouponDto couponDto) {

            Business business = currentUser().getBusiness();
            if(Objects.isNull(business)){
                throw new UrbanApiException(HttpStatus.PRECONDITION_FAILED,"Current user doesn't have any active Business");
            }

            if(Objects.nonNull(couponDto.getStartTime()) && Objects.nonNull(couponDto.getEndTime()) && couponDto.getEndTime().isBefore(couponDto.getStartTime())){
                throw new UrbanApiException(HttpStatus.PRECONDITION_FAILED,"Start time of Coupon can't be before End time");
            }

            Coupon coupon = couponDto.toCoupon();
            coupon.setBusiness(business);
            return couponRepository.save(coupon).toCouponDto();
    }

    @Override
    public CouponDto updateCoupon(CouponDto couponDto) {
        Coupon coupon = this.couponRepository.findById(couponDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Course","id",couponDto.getId().toString()));
        Coupon updateCoupon = couponDto.toCoupon();
        return this.couponRepository.save(updateCoupon).toCouponDto();
    }

    @Override
    public void deleteCoupon(CouponDto couponDto) {
        this.couponRepository.deleteById(couponDto.getId());
    }

    @Override
    public List<CouponDto> getAllCoupon() {
        List<Coupon> coupons = this.couponRepository.findAll();
        return coupons.stream().map(coupon -> coupon.toCouponDto()).collect(Collectors.toList());
    }

    @Override
    public CouponDto getCouponById(Long id) {
        Coupon coupon =  this.couponRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Course","id",id.toString()));
        return coupon.toCouponDto();
    }

    private User currentUser(){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        User user =userRepository.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("user","username",username));
        return user;
    }
}
