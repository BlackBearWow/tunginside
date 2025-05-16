package uman.tunginside.domain.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostListDTO {
    private Long totalCount;
    private List<PostSummaryDTO> posts;
}
