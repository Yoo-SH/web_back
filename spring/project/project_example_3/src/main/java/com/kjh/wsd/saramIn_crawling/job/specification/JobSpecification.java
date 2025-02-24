package com.kjh.wsd.saramIn_crawling.job.specification;

import com.kjh.wsd.saramIn_crawling.job.model.Job;
import org.springframework.data.jpa.domain.Specification;

/**
 * 채용 공고 검색 조건을 정의하는 클래스
 */
public class JobSpecification {

    /**
     * 제목 필터 조건
     *
     * @param title 검색할 제목 키워드
     * @return 제목에 키워드가 포함된 조건
     */
    public static Specification<Job> containsTitle(String title) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("title")), "%" + title.toLowerCase() + "%"
        );
    }

    /**
     * 회사 이름 필터 조건
     *
     * @param company 검색할 회사 이름 키워드
     * @return 회사 이름에 키워드가 포함된 조건
     */
    public static Specification<Job> containsCompany(String company) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("company")), "%" + company.toLowerCase() + "%"
        );
    }

    /**
     * 근무 위치 필터 조건
     *
     * @param location 검색할 위치 키워드
     * @return 근무 위치에 키워드가 포함된 조건
     */
    public static Specification<Job> containsLocation(String location) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("location")), "%" + location.toLowerCase() + "%"
        );
    }

    /**
     * 요구 경력 필터 조건
     *
     * @param experience 검색할 경력 키워드
     * @return 경력에 키워드가 포함된 조건
     */
    public static Specification<Job> containsExperience(String experience) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("experience")), "%" + experience.toLowerCase() + "%"
        );
    }

    /**
     * 급여 필터 조건
     *
     * @param salary 검색할 급여 키워드
     * @return 급여에 키워드가 포함된 조건
     */
    public static Specification<Job> containsSalary(String salary) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("salary")), "%" + salary.toLowerCase() + "%"
        );
    }

    /**
     * 고용 형태 필터 조건
     *
     * @param employment_type 검색할 고용 형태 키워드
     * @return 고용 형태에 키워드가 포함된 조건
     */
    public static Specification<Job> containsEmployment_type(String employment_type) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("employment_type")), "%" + employment_type.toLowerCase() + "%"
        );
    }


    /**
     * 요구 사항 필터 조건
     *
     * @param requirements 검색할 요구 사항 키워드
     * @return 요구 사항에 키워드가 포함된 조건
     */
    public static Specification<Job> containsRequirements(String requirements) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("requirements")), "%" + requirements.toLowerCase() + "%"
        );
    }

    /**
     * 산업 분야 필터 조건
     *
     * @param sector 검색할 산업 분야 키워드
     * @return 산업 분야에 키워드가 포함된 조건
     */
    public static Specification<Job> containsSector(String sector) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("sector")), "%" + sector.toLowerCase() + "%"
        );
    }
}
