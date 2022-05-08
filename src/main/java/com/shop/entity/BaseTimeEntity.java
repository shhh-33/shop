package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

/**
 * 보통 테이블에 등록일,수정일,등록자,수정자를 모두 다 넣어주지만,,아닌 것 도있으니
 * BaseTimeEntity만 상속받을 수 있도록 이걸 작성
 */
@EntityListeners(value = {AuditingEntityListener.class}) //Auditing 적용
@MappedSuperclass //공통 매핑 정보 필요할때 사용 , 부모클래스를 상속받는 자식클래스에 매핑 정보만 제공
@Getter @Setter
public abstract class BaseTimeEntity {

    @CreatedBy //엔티티가 생성되어 저장될때 시간 자동으로 저장
    @Column(updatable = false)
    private String createdBy; //등록자

    @LastModifiedBy //변경시 자동저장
    private String modifiedBy; //수정자

}