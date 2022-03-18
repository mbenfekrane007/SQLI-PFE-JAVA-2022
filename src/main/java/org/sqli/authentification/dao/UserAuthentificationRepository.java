package org.sqli.authentification.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.sqli.authentification.entitie.User;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface UserAuthentificationRepository extends JpaRepository<User,Long> {


    Optional<User> findByLogin(String login);

    @Query("UPDATE User u SET u.loginattempts = ?1 WHERE u.login = ?2")
    @Modifying
    void updateFailedAttempts(int loginattempts, String login);
}
