package com.emergent.socialmedia.photosharing.repositories;

import com.emergent.socialmedia.photosharing.domain.Comments;
import com.emergent.socialmedia.photosharing.domain.Likes;
import com.emergent.socialmedia.photosharing.domain.Media;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
    Optional<Comments> findOneByUserIdAndMediaId(Long userId, Long mediaId);
    List<Comments> findAllByMediaIdAndCommentedAtBefore(Long mediaId, Date date, Pageable pageable);
    List<Comments> findAllByMediaId(Long mediaId, Pageable pageable);

}
