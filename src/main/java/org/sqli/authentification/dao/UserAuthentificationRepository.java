package org.sqli.authentification.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sqli.authentification.entitie.User;

import java.util.Optional;

@Repository
public interface UserAuthentificationRepository extends JpaRepository<User,Long> {


    Optional<User> findByLogin(String login);
    Optional<User> findUserByPassword(String password);


}
