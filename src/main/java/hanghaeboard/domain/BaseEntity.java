package hanghaeboard.domain;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    private LocalDateTime createdDatetime;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime lastModifiedDatetime;

    @LastModifiedBy
    private String lastModifiedBy;

    @Column(columnDefinition = "TIMESTAMP NULL")
    private LocalDateTime deletedDatetime;


    private String deletedBy;

    public void delete(LocalDateTime deletedDatetime) {
        this.deletedDatetime = deletedDatetime;
    }

    public boolean isDeleted() {
        return this.deletedDatetime != null;
    }
}