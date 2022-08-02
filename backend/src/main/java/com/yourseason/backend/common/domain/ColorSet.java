package com.yourseason.backend.common.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "color_set_id"))
@Entity
public class ColorSet extends BaseTimeEntity {

    @OneToMany(mappedBy = "colorSet")
    private List<Color> colors = new ArrayList<>();

    @Builder
    public ColorSet(Long id, LocalDateTime createdDate, LocalDateTime lastModifiedDate, LocalDateTime deletedDate, List<Color> colors) {
        super(id, createdDate, lastModifiedDate, deletedDate, true);
        this.colors = colors;
    }
}
