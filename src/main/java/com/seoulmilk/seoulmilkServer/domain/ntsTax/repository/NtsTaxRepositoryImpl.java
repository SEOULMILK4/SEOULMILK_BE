package com.seoulmilk.seoulmilkServer.domain.ntsTax.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.QNtsTax;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    private BooleanExpression betweenIssueDate(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return ntsTax.issueDate.between(startDate, endDate);
        }
        return null;
    }

    private BooleanExpression inIpNames(List<String> ipNameList) {
        return (ipNameList != null && !ipNameList.isEmpty()) ? ntsTax.ipName.in(ipNameList) : null;
    }

    @Override
    public List<NtsTax> findAllById(List<Long> ids) {
        return jpaQueryFactory
            .selectFrom(QNtsTax.ntsTax)
            .where(QNtsTax.ntsTax.id.in(ids))
            .fetch();
    }
}
