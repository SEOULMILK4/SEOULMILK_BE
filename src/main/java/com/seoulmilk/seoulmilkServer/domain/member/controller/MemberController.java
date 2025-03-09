package com.seoulmilk.seoulmilkServer.domain.member.controller;


import com.seoulmilk.seoulmilkServer.domain.member.service.MemberService;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.ModifyNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.ModifyNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetOneNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.OcrTaxInvoiceResponseDTO;
import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employee")
@Tag(name = "[사원 - API]")
public class MemberController {

    private final MemberService memberService;

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
    public ApiResponse<OcrTaxInvoiceResponseDTO> revalidateOneNtsTaxResponse(@PathVariable("id") Long id) {
        return ApiResponse.success(memberService.revalidateOneNtsTax(id));
    }


}
