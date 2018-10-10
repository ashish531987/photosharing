package com.emergent.socialmedia.photosharing.repositories;

import com.emergent.socialmedia.photosharing.domain.Media;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findAllByCreatedAtBefore(Date date, Pageable pageable);
    List<Media> findAllByOrderByCreatedAtDesc();
}
