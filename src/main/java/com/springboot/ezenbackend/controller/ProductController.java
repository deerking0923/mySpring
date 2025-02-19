package com.springboot.ezenbackend.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.ezenbackend.dto.PageRequestDTO;
import com.springboot.ezenbackend.dto.PageResponseDTO;
import com.springboot.ezenbackend.dto.ProductDTO;
import com.springboot.ezenbackend.service.ProductService;
import com.springboot.ezenbackend.util.CustomFileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/products")
public class ProductController {
    private final CustomFileUtil fileUtil;
    private final ProductService productService;

    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO) {
        log.info("register: " + productDTO);

        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadFileNames = fileUtil.saveFiles(files);

        productDTO.setUploadFileNames(uploadFileNames);

        log.info(uploadFileNames);

        Long pno = productService.register(productDTO);

        return Map.of("result", pno);
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable(name = "fileName") String fileName) {
        return fileUtil.getFile(fileName);
    }

    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("list------------------" + pageRequestDTO);

        return productService.getList(pageRequestDTO);
    }

    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable(name = "pno") Long pno) {
        return productService.get(pno);
    }

    @PutMapping(value = "/{pno}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> modify(@PathVariable(name = "pno") Long pno, @ModelAttribute ProductDTO productDTO) {
        productDTO.setPno(pno);

        ProductDTO oldProductDTO = read(pno);
        List<String> oldFileNames = oldProductDTO.getUploadFileNames();
        List<MultipartFile> files = productDTO.getFiles();

        List<String> currentUploadFileNames = fileUtil.saveFiles(files);
        List<String> uploadeFileNames = productDTO.getUploadFileNames();
        if (currentUploadFileNames != null && currentUploadFileNames.size() > 0) {
            uploadeFileNames.addAll(currentUploadFileNames);
        }

        productService.modify(productDTO);

        if (oldFileNames != null && oldFileNames.size() > 0) {
            List<String> removeFiles = oldFileNames.stream()
                    .filter(fileName -> uploadeFileNames.indexOf(fileName) == -1)
                    .collect(Collectors.toList());
            fileUtil.deleteFiles(removeFiles);
        }

        return Map.of("RESULT", "SUCCESS");
    }

}
