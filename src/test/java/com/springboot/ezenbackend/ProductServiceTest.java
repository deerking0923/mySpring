package com.springboot.ezenbackend;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.ezenbackend.dto.PageRequestDTO;
import com.springboot.ezenbackend.dto.PageResponseDTO;
import com.springboot.ezenbackend.dto.ProductDTO;
import com.springboot.ezenbackend.service.ProductService;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    // @Test
    // public void testRegister() {

    //     ProductDTO productDTO = ProductDTO.builder()
    //             .pname("새로운 상품")
    //             .pdesc("신규 추가 상품입니다")
    //             .price(1000)
    //             .build();

    //     productDTO.setUploadFileNames(
    //             List.of(
    //                     UUID.randomUUID()
    //                             + "_" + "Test1.jpg",
    //                     UUID.randomUUID() +
    //                             "_" + "Test2.jpg"));

    //     productService.register(productDTO);
    // }

    @Test
    public void testRead(){
        Long pno = 12L;

        ProductDTO productDTO = productService.get(pno);
        
        log.info(productDTO);
        log.info(productDTO.getUploadFileNames());

    }

}