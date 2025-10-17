package com.BTL_JAVA.BTL.Repository;

import com.BTL_JAVA.BTL.Entity.Address;
import com.BTL_JAVA.BTL.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> {
        List<Address> findByUser(User user);
        Optional<Address> findByUserAndIsDefaultTrue(User user);
}
