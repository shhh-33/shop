package com.shop.entity;


import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/*
 2개만 있는 BaseTimeEntity 상속받고있음
 모두 갖고 싶으면 BaseEntity 상속받기
 */
@EntityListeners(value = {AuditingEntityListener.class}) //Auditing 적용
@MappedSuperclass //공통 매핑 정보 필요할때 사용 , 부모클래스를 상속받는 자식클래스에 매핑 정보만 제공
@Getter
public class BaseEntity extends BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regTime; //등록일

    @LastModifiedDate
    private LocalDateTime updateTime; //수정일

}
