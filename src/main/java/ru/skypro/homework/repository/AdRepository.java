package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;

import java.util.List;
import java.util.Optional;

public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findByUser(User user);
}
