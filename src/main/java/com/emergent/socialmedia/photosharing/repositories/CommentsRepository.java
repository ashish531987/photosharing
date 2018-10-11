package com.emergent.socialmedia.photosharing.repositories;

import com.emergent.socialmedia.photosharing.domain.Comments;
import com.emergent.socialmedia.photosharing.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
    Optional<Comments> findOneByUserIdAndMediaId(Long userId, Long mediaId);
}
