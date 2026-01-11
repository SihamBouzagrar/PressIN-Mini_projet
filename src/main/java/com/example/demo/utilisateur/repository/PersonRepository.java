package com.example.demo.utilisateur.repository;




import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.utilisateur.entity.Users;

@Repository
public interface PersonRepository extends JpaRepository<Users, Integer> {

    /* ============================
       1️⃣ RECHERCHES DE BASE
       ============================ */

    Optional<Users> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Users> findByCin(String cin);

    boolean existsByCin(String cin);

    List<Users> findByFirstname(String firstname);

    List<Users> findByLastname(String lastname);

    List<Users> findByFirstnameAndLastname(String firstname, String lastname);

    /* ============================
       2️⃣ RECHERCHES AVANCÉES
       ============================ */

    List<Users> findByFirstnameIgnoreCase(String firstname);

    List<Users> findByFirstnameContaining(String keyword);

    List<Users> findByLastnameStartingWith(String prefix);

    List<Users> findByLastnameEndingWith(String suffix);

    List<Users> findByFirstnameOrLastname(String firstname, String lastname);


    /* ============================
       4️⃣ COUNT
       ============================ */

    long countByFirstname(String firstname);

    /* ============================
       5️⃣ DELETE PERSONNALISÉ
       ============================ */

    @Transactional
    @Modifying
    void deleteByCin(String cin);

    /* ============================
       6️⃣ JPQL
       ============================ */

    @Query("SELECT u FROM Users u")
    List<Users> findAllUsersJPQL();

    @Query("SELECT u FROM Users u WHERE u.firstname = :fn")
    List<Users> findByFirstnameJPQL(@Param("fn") String firstname);

    @Query("SELECT u FROM Users u WHERE u.firstname LIKE %:kw%")
    List<Users> searchByFirstname(@Param("kw") String keyword);

    /* ============================
       7️⃣ NATIVE QUERY
       ============================ */

    @Query(value = "SELECT * FROM users WHERE firstname = ?1", nativeQuery = true)
    List<Users> findByFirstnameNative(String firstname);
}
