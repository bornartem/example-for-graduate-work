package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.config.security.CustomUserPrincipal;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;

    public Ad create(Ad ad){
        log.info("Создание объявления: {} ", ad.getId());
        return adRepository.save(ad);
    }

    @Override
    public List<Ad> getAllAds() {
        log.info("Получение всех объявлений");
        return adRepository.findAll();
    }

    @Override
    @Transactional
    public Ad createAd(Ad ad, Image image, CustomUserPrincipal user) {
        log.info(
                "Сохранение объявления: {}",
                ad.getTitle() + ", " + ad.getPrice() + ", " + ", " + ad.getDescription()
        );
        ad.setUser(user.getUser());
        ad.setImage(image);
        return create(ad);
    }

    @Override
    public Ad findById(Long id) {
        return adRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Объявление номером: {}", id + " не найдено");
                    return new AdNotFoundException("Такого объявления не найдено");
                });
    }

    @Override
    public void deleteAd(Long id) {
        if (adRepository.findById(id).isEmpty()) {
            log.error("Объявление с номером: {}", id + " не найдено");
            throw new AdNotFoundException("Такого объявления не найдено");
        }
        log.info("Удаление объявления по номеру: {}", id);
        adRepository.deleteById(id);
    }

    @Override
    public Ad updateAd(Long id, CreateOrUpdateAd createOrUpdateAd) {
        log.info("Обновление объявления: {}", id);
        Ad ad = findById(id);
        ad.setTitle(createOrUpdateAd.getTitle());
        ad.setPrice(createOrUpdateAd.getPrice());
        ad.setDescription(createOrUpdateAd.getDescription());
        return create(ad);
    }

    @Override
    public List<Ad> getAuthUserAds(User user) {
        log.info("Поиск всех объявлений: {}", user.getUsername());
        return adRepository.findByUser(user);
    }

    @Override
    @Transactional
    public Ad updateImage(Long id, Image image) {
        Ad ads = findById(id);
        ads.setImage(image);
        return create(ads);
    }
}
