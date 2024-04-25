package com.testehan.grpc.spring.user.repository;

import com.testehan.grpc.spring.user.entity.Userr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Userr, String> {
}
