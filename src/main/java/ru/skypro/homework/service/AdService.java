package ru.skypro.homework.service;

import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.config.security.CustomUserPrincipal;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;

import java.util.List;

public interface AdService {
    List<Ad> getAllAds();

    @Transactional
    Ad createAd(Ad ad, Image image, CustomUserPrincipal user);

    Ad findById(Long id);

    void deleteAd(Long id);

    Ad updateAd(Long id, CreateOrUpdateAd createOrUpdateAd);

    List<Ad> getAuthUserAds(User user);

    @Transactional
    Ad updateImage(Long id, Image image);
}
