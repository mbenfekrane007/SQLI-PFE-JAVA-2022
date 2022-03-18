package org.sqli.authentification.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sqli.authentification.entitie.Group;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface GroupRepository extends JpaRepository<Group,Long> {

    Optional<Group> findGroupByName(String name);
}
