package com.authservice.auth_service.repositories;

import com.authservice.auth_service.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {

}
