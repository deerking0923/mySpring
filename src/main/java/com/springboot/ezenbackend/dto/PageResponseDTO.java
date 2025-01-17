package com.springboot.ezenbackend.dto;

import java.util.List;
import java.util.stream.Collectors; // Stream API의 Collectors 사용
import java.util.stream.IntStream; // Stream API의 IntStream 사용

import lombok.Builder; // Lombok의 @Builder 사용
import lombok.Data; // Lombok의 @Data 사용 (getter, setter, equals 등 자동 생성)

@Data // Lombok: getter, setter, toString, equals 등을 자동으로 생성
public class PageResponseDTO<E> { // 페이징 응답 데이터를 담는 DTO 클래스
    private List<E> dtoList; // 실제 데이터 리스트
    private List<Integer> pageNumList; // 화면에 보여줄 페이지 번호 리스트
    private PageRequestDTO pageRequestDTO; // 요청 정보를 담은 DTO
    private boolean prev, next; // 이전 페이지 여부, 다음 페이지 여부
    private int totalCount; // 총 데이터 개수
    private int prevPage, nextPage; // 이전 페이지 번호, 다음 페이지 번호
    private int totalPage, current; // 총 페이지 수, 현재 페이지 번호

    /**
     * @Builder: 객체 생성 시 Builder 패턴을 사용
     *           builderMethodName = "withAll"으로 메서드 이름 설정
     */
    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long totalCount) {
        this.dtoList = dtoList; // 실제 데이터 리스트 설정
        this.pageRequestDTO = pageRequestDTO; // 요청 정보를 저장
        this.totalCount = (int) totalCount; // 총 데이터 개수를 long에서 int로 변환 후 저장

        // 현재 페이지를 기준으로 끝 페이지 번호 계산 (10개씩 묶어서 처리)
        int end = (int) (Math.ceil(pageRequestDTO.getPage() / 10.0) * 10); // ex: 11페이지 -> 20
        int start = end - 9; // 시작 페이지 번호는 끝 페이지 - 9 (10개씩 묶음) ex: 20 -> 11

        // 총 페이지 수 계산 (총 데이터 개수를 페이지 크기로 나누고 올림 처리)
        int last = (int) (Math.ceil(totalCount / (double) pageRequestDTO.getSize()));

        // 끝 페이지 번호가 총 페이지 수보다 크다면 총 페이지 수를 끝 페이지로 설정
        end = end > last ? last : end;

        // 이전 페이지 여부 설정 (시작 페이지가 1보다 크다면 이전 페이지 존재)
        this.prev = start > 1;

        // 다음 페이지 여부 설정 (전체 데이터 개수가 현재 끝 페이지의 마지막 데이터보다 많으면 다음 페이지 존재)
        this.next = totalCount > end * pageRequestDTO.getSize();

        // 페이지 번호 리스트 생성 (시작 페이지부터 끝 페이지까지 숫자 생성)
        this.pageNumList = IntStream.rangeClosed(start, end) // start부터 end까지 범위
                .boxed() // int -> Integer로 박싱
                .collect(Collectors.toList()); // 리스트로 수집

        // 이전 페이지 번호 설정
        if (prev) {
            prevPage = start - 1; // 이전 페이지는 현재 시작 페이지 - 1
        }

        // 다음 페이지 번호 설정
        if (next) {
            nextPage = end + 1; // 다음 페이지는 현재 끝 페이지 + 1
        }

        // 총 페이지 수 설정 (페이지 번호 리스트 크기)
        this.totalPage = this.pageNumList.size();

        // 현재 페이지 설정
        this.current = pageRequestDTO.getPage();
    }
}
