package com.emergent.socialmedia.photosharing.repositories;

import com.emergent.socialmedia.photosharing.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findOneByUserIdAndMediaId(Long userId, Long mediaId);
}
