package com.emergent.socialmedia.photosharing.repositories;

import com.emergent.socialmedia.photosharing.domain.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comments, Long> {
}
