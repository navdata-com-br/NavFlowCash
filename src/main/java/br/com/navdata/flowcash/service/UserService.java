package br.com.navdata.flowcash.service;

import br.com.navdata.flowcash.dto.response.UserProfileResponse;
import br.com.navdata.flowcash.entity.UserEntity;
import br.com.navdata.flowcash.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CategoryService categoryService;

    @Transactional
    public UserProfileResponse getOrCreateProfile(String userId, String email, String displayName, String photoUrl) {
        UserEntity user = userRepository.findById(userId).orElseGet(() -> {
            UserEntity newUser = UserEntity.builder()
                    .id(userId)
                    .email(email)
                    .displayName(displayName)
                    .photoUrl(photoUrl)
                    .role("user")
                    .accessUntil(LocalDateTime.now().plusDays(7))
                    .categoriesSeeded(false)
                    .build();
            return userRepository.save(newUser);
        });

        categoryService.seedDefaultCategories(userId);
        user.setLastAccess(LocalDateTime.now());
        if (displayName != null) user.setDisplayName(displayName);
        if (photoUrl != null) user.setPhotoUrl(photoUrl);

        return UserProfileResponse.fromEntity(userRepository.save(user));
    }

    @Transactional
    public UserProfileResponse invitePartner(String userId, String partnerEmail, String partnerName) {
        UserEntity owner = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String emailTrimmed = partnerEmail.toLowerCase().trim();
        owner.setPartnerEmail(emailTrimmed);
        owner.setPartnerName(partnerName != null && !partnerName.isBlank() ? partnerName.trim() : "Cônjuge / Parceiro(a)");

        // Check if partner already registered
        userRepository.findByEmail(emailTrimmed).ifPresent(partner -> {
            owner.setPartnerUid(partner.getId());
            partner.setInvitedBy(owner.getId());
            partner.setPrimaryOwnerEmail(owner.getEmail());
            partner.setPrimaryOwnerName(owner.getDisplayName());
            partner.setAccessUntil(owner.getAccessUntil());
            userRepository.save(partner);
        });

        return UserProfileResponse.fromEntity(userRepository.save(owner));
    }

    @Transactional
    public UserProfileResponse removePartner(String userId) {
        UserEntity owner = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (owner.getPartnerUid() != null) {
            userRepository.findById(owner.getPartnerUid()).ifPresent(partner -> {
                partner.setInvitedBy(null);
                partner.setPrimaryOwnerEmail(null);
                partner.setPrimaryOwnerName(null);
                userRepository.save(partner);
            });
        }

        owner.setPartnerEmail(null);
        owner.setPartnerName(null);
        owner.setPartnerUid(null);

        return UserProfileResponse.fromEntity(userRepository.save(owner));
    }
}
