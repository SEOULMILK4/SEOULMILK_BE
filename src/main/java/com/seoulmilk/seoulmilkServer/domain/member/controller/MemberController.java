package com.seoulmilk.seoulmilkServer.domain.member.controller;


import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.member.service.MemberAuthService;
import com.seoulmilk.seoulmilkServer.domain.member.service.MemberService;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.ModifyNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.ModifyNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetHometaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetOneNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.OcrTaxInvoiceResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.service.NtsTaxQueryService;
import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import com.seoulmilk.seoulmilkServer.global.service.S3Downloader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employee")
@Tag(name = "[사원 - API]")
public class MemberController {

    private final MemberService memberService;
    private final MemberAuthService memberAuthService;
    private final NtsTaxQueryService ntsTaxQueryService;
    private final S3Downloader s3Downloader;

    @Operation(summary = "개별 세금계산서 조회")
    @PostMapping("/nts-tax/{id}")
    public ApiResponse<GetOneNtsTaxResponseDTO> getOneNtsTaxResponse(@PathVariable("id") Long id) {
        return ApiResponse.success(memberService.getOneNtsTax(id));
    }

    @Operation(summary = "개별 세금계산서 수정")
    @PutMapping("/nts-tax/{id}")
    public ApiResponse<ModifyNtsTaxResponseDTO> modifyOneNtsTaxResponse(@PathVariable("id") Long id,
        @RequestBody @Valid ModifyNtsTaxRequestDTO requestDTO) {
        return ApiResponse.success(memberService.modifyOneNtsTax(id, requestDTO));
    }

    @Operation(summary = "개별 세금계산서 재검증")
    @PostMapping("/nts-tax/{id}/revalidate")
    public ApiResponse<OcrTaxInvoiceResponseDTO> revalidateOneNtsTaxResponse(
        @PathVariable("id") Long id) {
        return ApiResponse.success(memberService.revalidateOneNtsTax(id));
    }

    @Operation(summary = "세금 계산서 이번 달 내역 조회")
    @GetMapping("/nts-tax/view-hometax/recent")
    public ApiResponse<GetHometaxResponseDTO.GetHometaxListResponseDTO> getHometaxList(
        @RequestParam(name = "page") Integer page,
        @RequestParam(name = "isSuccess") Status status) {
        Member member = memberAuthService.getCurrentMember();

        return ApiResponse.success(ntsTaxQueryService.getHometaxList(member, page, status));
    }

    @Operation(summary = "세금 계산서 전체 내역 통합 조회")
    @GetMapping("/nts-tax/view-hometax/history")
    public ApiResponse<GetHometaxResponseDTO.GetHometaxListResponseDTO> getHometaxHistory(
        @RequestParam(name = "page") Integer page,
        @RequestParam(required = false) Status status) {
        Member member = memberAuthService.getCurrentMember();

        return ApiResponse.success(ntsTaxQueryService.getHometaxHistory(member, page, status));
    }

    @Operation(summary = "세금 계산서 내역 검색")
    @GetMapping("/nts-tax/search-hometax")
    public ApiResponse<GetHometaxResponseDTO.GetHometaxListResponseDTO> searchHometaxList(
        @RequestParam(name = "page") Integer page,
        @RequestParam(required = false) LocalDate startMonth,
        @RequestParam(required = false) LocalDate endMonth,
        @RequestParam(required = false) List<String> suNameList,
        @RequestParam(required = false) List<String> ipNameList) {
        Member member = memberAuthService.getCurrentMember();

        Page<NtsTax> searchHometaxList = ntsTaxQueryService.searchHometaxList(member, page,
            startMonth, endMonth, suNameList, ipNameList);

        return ApiResponse.success(GetHometaxResponseDTO.from(searchHometaxList));
    }


    @Operation(summary = "세금 계산서 다운로드")
    @GetMapping("/nts-tax/download-image")
    public ResponseEntity<byte[]> downloadImage(@RequestParam("imageUrl") String imageUrl) {
        Member member = memberAuthService.getCurrentMember();
        return s3Downloader.downloadImage(imageUrl);
    }



}
