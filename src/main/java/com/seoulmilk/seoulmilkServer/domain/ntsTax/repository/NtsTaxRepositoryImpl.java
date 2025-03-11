package com.seoulmilk.seoulmilkServer.domain.ntsTax.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.QNtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetCsvResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.QNtsTax.ntsTax;

@Repository
@RequiredArgsConstructor
public class NtsTaxRepositoryImpl implements NtsTaxRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<NtsTax> searchNtsTaxList(Agency agency, Pageable pageable, LocalDate startDate,
        LocalDate endDate, List<String> ipNameList) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 13, Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        List<NtsTax> results = jpaQueryFactory
            .selectFrom(ntsTax)
            .where(
                ntsTax.agency.eq(agency),
                betweenIssueDate(startDate, endDate),
                inIpNames(ipNameList)
            )
            .orderBy(ntsTax.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = Optional.ofNullable(
            jpaQueryFactory
                .select(ntsTax.count())
                .from(ntsTax)
                .where(
                    ntsTax.agency.eq(agency),
                    betweenIssueDate(startDate, endDate),
                    inIpNames(ipNameList)
                )
                .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public List<NtsTax> findAllById(List<Long> ids) {
        return jpaQueryFactory
            .selectFrom(QNtsTax.ntsTax)
            .where(QNtsTax.ntsTax.id.in(ids))
            .fetch();
    }

    @Override
    public Page<NtsTax> searchHometaxList(Member member, Pageable pageable, LocalDate startDate,
        LocalDate endDate, List<String> suNameList, List<String> ipNameList) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 13, Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        List<NtsTax> results = jpaQueryFactory
            .selectFrom(ntsTax)
            .where(
                ntsTax.member.eq(member),
                betweenIssueDate(startDate, endDate),
                orSearch(suNameList, ipNameList)
            )
            .orderBy(ntsTax.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = Optional.ofNullable(
            jpaQueryFactory
                .select(ntsTax.count())
                .from(ntsTax)
                .where(
                    ntsTax.member.eq(member),
                    betweenIssueDate(startDate, endDate),
                    orSearch(suNameList, ipNameList)
                )
                .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<NtsTax> searchHometaxListByAdmin(Pageable pageable, Status status,
        LocalDate startDate,
        LocalDate endDate, List<String> suNameList, List<String> ipNameList) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 13, Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        BooleanBuilder builder = new BooleanBuilder();

        if (status != null) {
            builder.and(ntsTax.status.eq(status));
        }

        builder.and(betweenIssueDate(startDate, endDate));

        if (suNameList != null && !suNameList.isEmpty()) {
            builder.and(ntsTax.suName.in(suNameList));
        }
        if (ipNameList != null && !ipNameList.isEmpty()) {
            builder.and(ntsTax.ipName.in(ipNameList));
        }

        List<NtsTax> results = jpaQueryFactory
            .selectFrom(ntsTax)
            .where(builder)
            .orderBy(ntsTax.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = Optional.ofNullable(
            jpaQueryFactory
                .select(ntsTax.count())
                .from(ntsTax)
                .where(builder)
                .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public List<GetCsvResponseDTO> getHometaxCsv(Member member, LocalDate startDate,
        LocalDate endDate, List<String> suNameList, List<String> ipNameList,
        List<NtsTax> ntsTaxList) {
        List<NtsTax> results = jpaQueryFactory
            .selectFrom(ntsTax)
            .where(
                ntsTax.member.eq(member),
                ntsTax.in(ntsTaxList),
                betweenIssueDate(startDate, endDate),
                orSearch(suNameList, ipNameList)
            )
            .orderBy(ntsTax.createdAt.desc())
            .fetch();

        return results.stream()
            .map(GetCsvResponseDTO::from)
            .collect(Collectors.toList());
    }

    private BooleanExpression betweenIssueDate(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return ntsTax.issueDate.between(startDate, endDate);
        }
        return null;
    }

    private BooleanExpression inSuNames(List<String> suNameList) {
        // null = 검색 조건 적용 X
        if (suNameList == null || suNameList.isEmpty()) {
            return null;
        }
        BooleanExpression searchList = null;

        // 첫 번째 조건 -> 저장, 두 번째 조건부터 or로 연결
        for (String suName : suNameList) {
            BooleanExpression searchSuName = ntsTax.suName.like("%" + suName + "%");
            searchList = (searchList == null) ? searchSuName : searchList.or(searchSuName);
        }
        return searchList;
    }

    private BooleanExpression inIpNames(List<String> ipNameList) {
        // null = 검색 조건 적용 X
        if (ipNameList == null || ipNameList.isEmpty()) {
            return null;
        }
        BooleanExpression searchList = null;

        // 첫 번째 조건 -> 저장, 두 번째 조건부터 or로 연결
        for (String ipName : ipNameList) {
            BooleanExpression searchSuName = ntsTax.ipName.like("%" + ipName + "%");
            searchList = (searchList == null) ? searchSuName : searchList.or(searchSuName);
        }
        return searchList;
    }

    private BooleanExpression orSearch(List<String> suNameList, List<String> ipNameList) {
        BooleanExpression suName = inSuNames(suNameList);
        BooleanExpression ipName = inIpNames(ipNameList);

        if (suName == null) {
            return ipName;
        }
        if (ipName == null) {
            return suName;
        }

        return suName.or(ipName);
    }
}
