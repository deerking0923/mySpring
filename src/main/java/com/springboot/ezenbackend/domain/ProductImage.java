package com.springboot.ezenbackend.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable // 값타입 객체
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {
    private String fileName;

    //이미지에 붙이는 번호, 상품 목록을 출력할때 ord값이 0번인 이미지가 대표 이미지로 출력하려고 할때
    private int ord;

    public void setOrd(int ord) {
        this.ord = ord;
    }

}
