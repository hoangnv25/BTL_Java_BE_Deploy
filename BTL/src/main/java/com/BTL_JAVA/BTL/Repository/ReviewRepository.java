package com.BTL_JAVA.BTL.Repository;

import com.BTL_JAVA.BTL.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    // Kiểm tra xem review có tồn tại không
    boolean existsByUserId(int userId);
}
