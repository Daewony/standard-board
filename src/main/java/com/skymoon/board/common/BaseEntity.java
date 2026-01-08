package com.skymoon.board.common;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 이 클래스는 진짜 테이블이 아니고, 부모 역할만 한다
@EntityListeners(AuditingEntityListener.class) // 자동으로 시간을 넣어주는 리스너
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false) // 생성일은 수정되면 안 됨
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


    // 핵심: @Setter를 붙이지 않습니다! (외부에서 setStatus() 호출 금지)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntityStatus status = EntityStatus.ACTIVE;

    public void activate() {
        this.status = EntityStatus.ACTIVE;
    }

    public boolean isActive() {
        return this.status == EntityStatus.ACTIVE;
    }

    public void delete() {
        this.status = EntityStatus.DELETED;
    }

    public boolean isDeleted() {
        return this.status == EntityStatus.DELETED;
    }

}
