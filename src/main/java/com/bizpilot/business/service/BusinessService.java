package com.bizpilot.business.service;

import com.bizpilot.authentication.entity.UserEntity;
import com.bizpilot.authentication.repository.UserRepository;
import com.bizpilot.business.dto.request.BusinessRegistrationRequest;
import com.bizpilot.business.dto.request.BusinessUpdateRequest;
import com.bizpilot.business.entity.BusinessEntity;
import com.bizpilot.business.mapper.BusinessMapper;
import com.bizpilot.business.model.Business;
import com.bizpilot.business.model.CategoryConfig;
import com.bizpilot.business.model.ThemeOption;
import com.bizpilot.business.repository.BusinessRepository;
import com.bizpilot.common.exception.BusinessNotFoundException;
import com.bizpilot.common.exception.DuplicateSlugException;
import com.bizpilot.common.exception.ThemeNotSelectedException;
import jakarta.validation.Valid;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;
    private final UserRepository userRepository;
    private final CategoryConfigService categoryConfigService;
    private final FileStorageService fileStorageService;
    private final ThemeConfigService themeConfigService; // constructor mein inject karo


    public BusinessService(BusinessRepository businessRepository,
                           BusinessMapper businessMapper,
                           UserRepository userRepository,
                           CategoryConfigService categoryConfigService,
                           FileStorageService fileStorageService,
                           ThemeConfigService themeConfigService) {

        this.businessRepository = businessRepository;
        this.businessMapper = businessMapper;
        this.userRepository = userRepository;
        this.categoryConfigService = categoryConfigService;
        this.fileStorageService = fileStorageService;
        this.themeConfigService = themeConfigService;
    }

    public Business register(BusinessRegistrationRequest request) {

        // 1. Create Business Model
        Business business = Business.builder()
                .businessName(request.getBusinessName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .whatsapp(request.getWhatsapp())
                .googleMap(request.getGoogleMap())
                .address(request.getAddress())
                .description(request.getDescription())
                .category(request.getCategory())
                .build();

        // 2. Generate Slug
        String slug = generateSlug(business.getBusinessName());

        if (businessRepository.existsBySlug(slug)) {
            throw new DuplicateSlugException(slug);
        }

        business.setSlug(slug);

        // 3. Load Category Config
        CategoryConfig config =
                categoryConfigService.load(business.getCategory());

        business.setTheme(config.getTemplate());

        business.setPublished(false);

        // 4. Logged In User
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        UserEntity owner = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 5. Convert Model -> Entity
        BusinessEntity entity = businessMapper.toEntity(business);

        System.out.println("business : "+business.getGoogleMap());
        System.out.println("BusinessRegistrationRequest : "+request.getGoogleMap());
        entity.setOwner(owner);

        // 6. Save
        entity = businessRepository.save(entity);

        // 7. Entity -> Model
        return businessMapper.toModel(entity);
    }

    public Business findBySlug(String slug) {

        BusinessEntity entity = businessRepository
                .findBySlugAndPublishedTrue(slug)
                .orElseThrow(() ->
                        new BusinessNotFoundException(slug));

        return businessMapper.toModel(entity);
    }

    public BusinessEntity getBusinessBySlug(String slug) {

        BusinessEntity entity = businessRepository
                .findBySlugAndPublishedTrue(slug)
                .orElseThrow(() ->
                        new BusinessNotFoundException(slug));

        return entity;
    }

//    public Business togglePublish(Long id) {
//
//        UserEntity loggedInUser = getLoggedInUser();
//
//        BusinessEntity entity = businessRepository.findById(id)
//                .orElseThrow(() -> new BusinessNotFoundException("Not Fount Business "));
//
//        if (!entity.getOwner().getId().equals(loggedInUser.getId())) {
//            throw new AccessDeniedException("You are not allowed to modify this business");
//        }
//
//        entity.setPublished(!entity.getPublished());
//
//        entity = businessRepository.save(entity);
//
//        return businessMapper.toModel(entity);
//    }


    private String generateSlug(String businessName) {

        return businessName
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }

    Optional<BusinessEntity> findByOwner(UserEntity owner){
       return businessRepository.findByOwner(owner);
    }

    public Business getMyBusiness() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        UserEntity owner = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        BusinessEntity entity =
                findByOwner(owner)
                .orElseThrow(() ->
                        new RuntimeException("Business not found"));

        return businessMapper.toModel(entity);
    }

    public BusinessEntity getCurrentBusinessEntity() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        UserEntity owner =
                userRepository.findByEmail(email)
                        .orElseThrow();

        return businessRepository
                .findByOwner(owner)
                .orElseThrow(() ->
                        new RuntimeException("Business not found"));
    }



//    public Business update(Long id, BusinessUpdateRequest request) {
//
//        UserEntity loggedInUser = getLoggedInUser();
//
//
//        BusinessEntity entity = businessRepository.findById(id)
//                .orElseThrow(() -> new BusinessNotFoundException("Business not found with slug :"));
//
//        // Ownership check — koi aur user kisi aur ka business update na kar sake
//        if (!entity.getOwner().getId().equals(loggedInUser.getId())) {
//            throw new AccessDeniedException("You are not allowed to update this business");
//        }
//
//        entity.setBusinessName(request.getBusinessName());
//        entity.setPhone(request.getPhone());
//        entity.setEmail(request.getEmail());
//        entity.setWhatsapp(request.getWhatsapp());
//        entity.setAddress(request.getAddress());
//        entity.setDescription(request.getDescription());
//
//        // category, slug, theme, published — jaan bujh kar touch nahi kiya
//
//        entity = businessRepository.save(entity);
//
//        return businessMapper.toModel(entity);
//    }

    // Helper — dono methods mein reuse ho raha hai, existing register() mein bhi isi se replace kar sakte ho
    private UserEntity getLoggedInUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Business update(Long id, BusinessUpdateRequest request) {

        UserEntity loggedInUser = getLoggedInUser();

        BusinessEntity entity = businessRepository.findById(id)
                .orElseThrow(() -> new BusinessNotFoundException("Not Fund Exception"));

        if (!entity.getOwner().getId().equals(loggedInUser.getId())) {
            throw new AccessDeniedException("You are not allowed to update this business");
        }

        entity.setBusinessName(request.getBusinessName());
        entity.setPhone(request.getPhone());
        entity.setEmail(request.getEmail());
        entity.setWhatsapp(request.getWhatsapp());
        entity.setAddress(request.getAddress());
        entity.setDescription(request.getDescription());
        entity.setGoogleMap(request.getGoogleMap());   // 👈 add kiya

        entity = businessRepository.save(entity);

        return businessMapper.toModel(entity);
    }

    public Business uploadLogo(Long id, MultipartFile file) {

        BusinessEntity entity = getOwnedBusiness(id);

        fileStorageService.delete(entity.getLogo());

        String path = fileStorageService.storeLogo(file);  // 👈 naya method
        entity.setLogo(path);

        entity = businessRepository.save(entity);
        return businessMapper.toModel(entity);
    }

    public Business uploadCoverImage(Long id, MultipartFile file) {

        BusinessEntity entity = getOwnedBusiness(id);

        fileStorageService.delete(entity.getCoverImage());

        String path = fileStorageService.storeCoverImage(file);  // 👈 naya method
        entity.setCoverImage(path);

        entity = businessRepository.save(entity);
        return businessMapper.toModel(entity);
    }

    private BusinessEntity getOwnedBusiness(Long id) {
        UserEntity loggedInUser = getLoggedInUser();

        BusinessEntity entity = businessRepository.findById(id)
                .orElseThrow(() -> new BusinessNotFoundException("Business not found exception"));

        if (!entity.getOwner().getId().equals(loggedInUser.getId())) {
            throw new AccessDeniedException("You are not allowed to modify this business");
        }

        return entity;
    }


    public List<ThemeOption> getAvailableThemes() {
        BusinessEntity business = getOwnedBusinessOfCurrentUser();
        return themeConfigService.loadThemes(business.getCategory());
    }

    public Business selectTheme(Long id, String theme) {

        BusinessEntity entity = getOwnedBusiness(id); // existing helper (ownership check karta hai)

        entity.setTheme(theme);
        entity = businessRepository.save(entity);

        return businessMapper.toModel(entity);
    }

    public Business togglePublish(Long id) {

        BusinessEntity entity = getOwnedBusiness(id);

        boolean willBePublished = !entity.getPublished();

        // Agar publish karne ja rahe hain (draft se live), theme check karo
        if (willBePublished && (entity.getTheme() == null || entity.getTheme().isBlank())) {
            throw new ThemeNotSelectedException();
        }

        entity.setPublished(willBePublished);
        entity = businessRepository.save(entity);

        return businessMapper.toModel(entity);
    }

    private BusinessEntity getOwnedBusinessOfCurrentUser() {
        UserEntity owner = getLoggedInUser();
        return businessRepository.findByOwnerId(owner.getId())
                .orElseThrow(() -> new RuntimeException("Business not found for logged in user"));
    }

    public BusinessEntity getBusinessBySlugIgnorePublished(String slug) {
        return businessRepository.getBusinessBySlug(slug);
    }

}